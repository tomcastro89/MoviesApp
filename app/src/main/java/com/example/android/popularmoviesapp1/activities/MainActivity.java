package com.example.android.popularmoviesapp1.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmoviesapp1.R;
import com.example.android.popularmoviesapp1.adapters.MoviesAdapter;
import com.example.android.popularmoviesapp1.data.MovieData;
import com.example.android.popularmoviesapp1.data.MovieDataListContract;
import com.example.android.popularmoviesapp1.data.MovieDataListDBHelper;
import com.example.android.popularmoviesapp1.data.ReviewData;
import com.example.android.popularmoviesapp1.data.VideoData;
import com.example.android.popularmoviesapp1.networking.NetworkUtils;
import com.example.android.popularmoviesapp1.networking.OpenMovieDataJsonUtils;
import com.google.gson.Gson;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.ListItemClickListener {

    private RecyclerView mRecyclerViewMovies;
    private MoviesAdapter mMoviesAdapter;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;
    private SQLiteDatabase mMovieDB;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerViewMovies = (RecyclerView) findViewById(R.id.recyclerview_movies);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        mRecyclerViewMovies.setLayoutManager(gridLayoutManager);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mMoviesAdapter = new MoviesAdapter(this);
        mRecyclerViewMovies.setAdapter(mMoviesAdapter);

        MovieDataListDBHelper dbHelper = new MovieDataListDBHelper(this);
        mMovieDB = dbHelper.getReadableDatabase();

        setTitle("Movies App (sorted by Rating)");
        loadMovieData("byRating");
    }

    private void loadMovieData(String rankingOption){
        showMovieDataView();
        new FetchMovieDataTask().execute(rankingOption);
    }

    public class FetchMovieDataTask extends AsyncTask<String, Void, Map<String,MovieData>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Map<String,MovieData> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            String rankingOption = params[0];

            if(rankingOption.equals("localFavorites")){

                try{
                    //Get DB entries sorted by ID
                    Cursor cursor = getContentResolver().query(MovieDataListContract.MovieDataEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            MovieDataListContract.MovieDataEntry.COLUMN_MOVIE_ID);

                    return getMovieMapFromCursor(cursor);

                }catch(Exception e){
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }else{
                URL movieRequestUrl = NetworkUtils.buildUrl(rankingOption,"");

                try {
                    String jsonMovieDataResponse = NetworkUtils
                            .getResponseFromHttpUrl(movieRequestUrl);

                    return OpenMovieDataJsonUtils.getMovieDataFromJson(jsonMovieDataResponse);

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

        @Override
        protected void onPostExecute(Map<String,MovieData> movieDataMap) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieDataMap != null) {
                showMovieDataView();
                mMoviesAdapter.setMovieDataList(movieDataMap);
            } else {
                showErrorMessage();
            }
        }
    }

    private Map<String,MovieData> getMovieMapFromCursor(Cursor cursor) {
        Map<String,MovieData> moviesMap = new LinkedHashMap<>();

        if (cursor.moveToFirst()){
            do{
                String imageUrl = cursor.getString(cursor.getColumnIndex("movieImageURL"));
                String rating = cursor.getString(cursor.getColumnIndex("movieRating"));
                String popularity = cursor.getString(cursor.getColumnIndex("moviePopularity"));
                String overview = cursor.getString(cursor.getColumnIndex("movieOverview"));
                String title = cursor.getString(cursor.getColumnIndex("movieTitle"));
                String releaseDate = cursor.getString(cursor.getColumnIndex("movieReleaseDate"));
                String movieID = cursor.getString(cursor.getColumnIndex("movieID"));

                ArrayList<VideoData> trailers = new ArrayList<>();
                ArrayList<ReviewData> reviews = new ArrayList<>();

                String trailersJson = cursor.getString(cursor.getColumnIndex("movieVideos"));

                Gson gson = new Gson();
                trailers = gson.fromJson(trailersJson, ArrayList.class);

                String reviewsJson = cursor.getString(cursor.getColumnIndex("movieReviews"));
                reviews = gson.fromJson(reviewsJson, ArrayList.class);

                MovieData movieData = new MovieData(
                        imageUrl,
                        Double.parseDouble(rating),
                        Double.parseDouble(popularity),
                        overview,
                        title,
                        releaseDate,
                        movieID,
                        reviews,
                        trailers);

                moviesMap.put(movieID,movieData);

            }while(cursor.moveToNext());
        }
        cursor.close();
        return moviesMap;
    }

    private void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerViewMovies.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mRecyclerViewMovies.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sortByPopularity) {
            setTitle("Movies App (sorted by Popularity)");
            loadMovieData("byPopularity");
            return true;
        }

        if (id == R.id.action_sortByRating) {
            setTitle("Movies App (sorted by Rating)");
            loadMovieData("byRating");
            return true;
        }

        if(id == R.id.action_favorite){
            setTitle("Movies App (your Favorites)");
            loadMovieData("localFavorites");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Context context = getApplicationContext();
        Class destinationClass = MovieDetailsActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        Map<String,MovieData> movieDataMap = mMoviesAdapter.getMovieDataMap();
        String[] keySet =  movieDataMap.keySet().toArray(new String[movieDataMap.size()]);
        intentToStartDetailActivity.putExtra("MovieData",movieDataMap.get(keySet[clickedItemIndex]));
        startActivity(intentToStartDetailActivity);
    }
}

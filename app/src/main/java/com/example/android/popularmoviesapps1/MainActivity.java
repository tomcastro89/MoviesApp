package com.example.android.popularmoviesapps1;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.ListItemClickListener {

    private RecyclerView mRecyclerViewMovies;
    private MoviesAdapter mMoviesAdapter;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;

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

        setTitle("Movies App (sorted by Rating)");
        loadMovieData("byRating");
    }

    private void loadMovieData(String rankingOption){
        showMovieDataView();
        new FetchMovieDataTask().execute(rankingOption);
    }

    public class FetchMovieDataTask extends AsyncTask<String, Void, ArrayList<MovieData>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<MovieData> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            String rankingOption = params[0];
            URL movieRequestUrl = NetworkUtils.buildUrl(rankingOption);

            try {
                String jsonMovieDataResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieRequestUrl);

                return OpenMovieDataJsonUtils.getMovieDataFromJson(jsonMovieDataResponse);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<MovieData> movieDataArrayList) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieDataArrayList != null) {
                showMovieDataView();
                mMoviesAdapter.setMovieDataList(movieDataArrayList);
            } else {
                showErrorMessage();
            }
        }
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Context context = getApplicationContext();
        Class destinationClass = MovieDetails.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        String movieDataString = generateMovieDataString(mMoviesAdapter.getMovieDataList().get(clickedItemIndex));
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT,movieDataString);
        startActivity(intentToStartDetailActivity);
    }

    /**
     * This method creates a String out of a MovieData - Object that represents
     * all movie information separated by an unique separator
     *
     * @param movieData The MovieData- Object
     * @return A String containing all Movie information seperated by an unique separator
     */
    private String generateMovieDataString(MovieData movieData) {
        StringBuilder builder = new StringBuilder();
        String uniqueSeparator = ";X&;";
        builder.append(movieData.getMovieOriginalTitle())
                .append(uniqueSeparator)
                .append(movieData.getMovieImageURL())
                .append(uniqueSeparator)
                .append(movieData.getMovieRating())
                .append(uniqueSeparator)
                .append(movieData.getMoviePopularity())
                .append(uniqueSeparator)
                .append(movieData.getMovieOverview())
                .append(uniqueSeparator)
                .append(movieData.getMovieReleaseDate());
        return builder.toString();
    }
}

/*
 * Author: Tommaso Castrovillari
 */
package com.example.android.popularmoviesapp1.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesapp1.R;
import com.example.android.popularmoviesapp1.adapters.TrailersAdapter;
import com.example.android.popularmoviesapp1.data.MovieData;
import com.example.android.popularmoviesapp1.data.MovieDataListContract;
import com.example.android.popularmoviesapp1.data.MovieDataListDBHelper;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    private TextView mOriginalTitle;
    private ImageView mMoviePoster;
    private TextView mRating;
    private TextView mPopularity;
    private TextView mOverview;
    private TextView mReleaseDate;
    private ListView mTrailers;
    private MovieData mMovieData;
    private Intent mIntentThatStartedThisActivity;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        setTitle(R.string.MovieDetailsTitle);

        mOriginalTitle = (TextView) findViewById(R.id.textview_originalTitle);
        mMoviePoster = (ImageView) findViewById(R.id.imageview_details_movie_poster);
        mRating = (TextView) findViewById(R.id.textview_rating);
        mPopularity = (TextView) findViewById(R.id.textview_popularity);
        mOverview = (TextView) findViewById(R.id.textview_overview);
        mReleaseDate = (TextView) findViewById(R.id.textview_releasedate);
        mTrailers = (ListView) findViewById(R.id.listview_videos);


        mIntentThatStartedThisActivity = getIntent();

        if (mIntentThatStartedThisActivity != null) {
            if (mIntentThatStartedThisActivity.hasExtra("MovieData")) {

                mMovieData = (MovieData) mIntentThatStartedThisActivity.getSerializableExtra("MovieData");
                MovieDataListDBHelper dbHelper = new MovieDataListDBHelper(this);

                mOriginalTitle.setText(mMovieData.getMovieOriginalTitle());
                Picasso.with(getApplicationContext()).load(mMovieData.getMovieImageURL()).into(mMoviePoster);
                mRating.setText(Double.toString(mMovieData.getMovieRating()));
                mPopularity.setText(Double.toString(mMovieData.getMoviePopularity()));
                mOverview.setText(mMovieData.getMovieOverview());
                mReleaseDate.setText(mMovieData.getMovieReleaseDate());

                TrailersAdapter trailersAdapter = new TrailersAdapter(this,mMovieData.getVideos());
                setListViewHeightBasedOnItems(mTrailers);
                mTrailers.setAdapter(trailersAdapter);
            }
        }
    }

    //Helper function to fix ListView in ScrollView Issue
    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie_details_favorite_action, menu);
        int filledStar = R.drawable.ic_favorite_black_24dp;
        int emptyStar = R.drawable.ic_favorite_border_black_24dp;
        if(mMovieData.isFavorite()){
            menu.findItem(R.id.action_favorite).setIcon(filledStar);
        }else{
            menu.findItem(R.id.action_favorite).setIcon(emptyStar);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                if(mMovieData.isFavorite()){
                    mMovieData.setIsFavorite(false);
                    item.setIcon(R.drawable.ic_favorite_border_black_24dp);
                    removeFromFavoriteDB();
                }else{
                    mMovieData.setIsFavorite(true);
                    item.setIcon(R.drawable.ic_favorite_black_24dp);
                    saveToFavoriteDB();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void removeFromFavoriteDB() {
        Uri uri = MovieDataListContract.MovieDataEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(mMovieData.getMovieID()).build();
        getContentResolver().delete(uri, null, null);
        if(mToast!= null){
            mToast.cancel();
        }
        mToast = Toast.makeText(getBaseContext(),"Removed from Favorites",Toast.LENGTH_LONG);
        mToast.show();
    }

    private void saveToFavoriteDB() {
        ContentValues cv = new ContentValues();
        cv.put(MovieDataListContract.MovieDataEntry.COLUMN_MOVIE_ID,mMovieData.getMovieID());
        cv.put(MovieDataListContract.MovieDataEntry.COLUMN_MOVIE_IMAGE_URL,mMovieData.getMovieImageURL());
        cv.put(MovieDataListContract.MovieDataEntry.COLUMN_MOVIE_RATING,mMovieData.getMovieRating());
        cv.put(MovieDataListContract.MovieDataEntry.COLUMN_MOVIE_POPULARITY,mMovieData.getMoviePopularity());
        cv.put(MovieDataListContract.MovieDataEntry.COLUMN_MOVIE_OVERVIEW,mMovieData.getMovieOverview());
        cv.put(MovieDataListContract.MovieDataEntry.COLUMN_MOVIE_TITLE,mMovieData.getMovieOriginalTitle());
        cv.put(MovieDataListContract.MovieDataEntry.COLUMN_MOVIE_RELEASE_DATE,mMovieData.getMovieReleaseDate());
        Gson gson = new Gson();
        String jsonReviews = gson.toJson(mMovieData.getReviews());
        cv.put(MovieDataListContract.MovieDataEntry.COLUMN_MOVIE_REVIEWS,jsonReviews);
        String jsonVideos = gson.toJson(mMovieData.getVideos());
        cv.put(MovieDataListContract.MovieDataEntry.COLUMN_MOVIE_VIDEOS,jsonVideos);

        Uri uri = getContentResolver().insert(MovieDataListContract.MovieDataEntry.CONTENT_URI,cv);

        if(uri != null){
            if(mToast!= null){
                mToast.cancel();
            }
            mToast = Toast.makeText(getBaseContext(),"Added to Favorites",Toast.LENGTH_SHORT);
            mToast.show();
        }
    }

    public void onReviewsClick(View view){
        Context context = getApplicationContext();
        Class destinationClass = ReviewsActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra("MovieData",mMovieData);
        startActivity(intentToStartDetailActivity);
    }
}

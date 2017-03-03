/*
 * Author: Tommaso Castrovillari
 */
package com.example.android.popularmoviesapps1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;

public class MovieDetails extends AppCompatActivity {

    private TextView mOriginalTitle;
    private ImageView mMoviePoster;
    private TextView mRating;
    private TextView mPopularity;
    private TextView mOverview;
    private TextView mReleaseDate;

    private String MOVIE_ID;

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

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("MovieData")) {

                MovieData movieData = (MovieData) intentThatStartedThisActivity.getSerializableExtra("MovieData");

                mOriginalTitle.setText(movieData.getMovieOriginalTitle());
                Picasso.with(getApplicationContext()).load(movieData.getMovieImageURL()).into(mMoviePoster);
                mRating.setText(Double.toString(movieData.getMovieRating()));
                mPopularity.setText(Double.toString(movieData.getMoviePopularity()));
                mOverview.setText(movieData.getMovieOverview());
                mReleaseDate.setText(movieData.getMovieReleaseDate());
                MOVIE_ID = movieData.getMovieID();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie_details_favorite_action, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                //TODO
                item.setIcon(R.drawable.ic_favorite_black_24dp);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClickReviews(View view) {
        //TODO
    }

    public void onClickTrailer(View view) {
        //TODO
    }
}

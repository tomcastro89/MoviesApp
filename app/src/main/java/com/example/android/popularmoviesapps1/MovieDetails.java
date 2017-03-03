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
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetails extends AppCompatActivity {

    private TextView mOriginalTitle;
    private ImageView mMoviePoster;
    private TextView mRating;
    private TextView mPopularity;
    private TextView mOverview;
    private TextView mReleaseDate;

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
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {

                String mMovieDataString = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);

                String[] movieInformation = mMovieDataString.split(";X&;");
                //Should look like this: "TITLE;X&;IMG_URL;X&;RATING;X&;POPULARITY;X&;OVERVIEW;X&;RELEASEDATE

                mOriginalTitle.setText(movieInformation[0]);
                Picasso.with(getApplicationContext()).load(movieInformation[1]).into(mMoviePoster);
                mRating.setText(movieInformation[2]);
                mPopularity.setText(movieInformation[3]);
                mOverview.setText(movieInformation[4]);
                mReleaseDate.setText(movieInformation[5]);
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
                item.setIcon(R.drawable.ic_favorite_border_black_24dp);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

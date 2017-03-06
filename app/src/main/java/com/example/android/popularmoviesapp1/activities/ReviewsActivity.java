package com.example.android.popularmoviesapp1.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.android.popularmoviesapp1.R;
import com.example.android.popularmoviesapp1.adapters.ReviewsAdapter;
import com.example.android.popularmoviesapp1.data.MovieData;

public class ReviewsActivity extends AppCompatActivity {

    private ListView mReviews;
    private MovieData mMovieData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        mReviews = (ListView) findViewById(R.id.listview_reviews);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("MovieData")) {

                mMovieData = (MovieData) intentThatStartedThisActivity.getSerializableExtra("MovieData");

                ReviewsAdapter reviewsAdapter = new ReviewsAdapter(this, mMovieData.getReviews());
                mReviews.setAdapter(reviewsAdapter);
            }
        }
    }
}

package com.example.android.popularmoviesapp1.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Tcastrovillari on 03.03.2017.
 */

public class MovieDataListContract {

    public static final String AUTHORITY = "com.example.android.popularmoviesapp1";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);

    public static final String PATH_MOVIE_DATA_LIST = "MovieDataList";

    public static final class MovieDataEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE_DATA_LIST).build();

        public static final String TABLE_NAME = "MovieDataList";
        public static final String COLUMN_MOVIE_ID = "movieID";
        public static final String COLUMN_MOVIE_IMAGE_URL = "movieImageURL";
        public static final String COLUMN_MOVIE_RATING = "movieRating";
        public static final String COLUMN_MOVIE_POPULARITY = "moviePopularity";
        public static final String COLUMN_MOVIE_OVERVIEW = "movieOverview";
        public static final String COLUMN_MOVIE_TITLE = "movieTitle";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "movieReleaseDate";
        public static final String COLUMN_MOVIE_REVIEWS = "movieReviews";
        public static final String COLUMN_MOVIE_VIDEOS = "movieVideos";
    }

}

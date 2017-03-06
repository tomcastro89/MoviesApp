package com.example.android.popularmoviesapp1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.popularmoviesapp1.data.MovieDataListContract.*;

/**
 * Created by Tcastrovillari on 03.03.2017.
 */

public class MovieDataListDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "movieDB.db";

    private static final int DATABASE_VERSION = 1;

    // Constructor
    public MovieDataListDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieDataEntry.TABLE_NAME + " (" +
                MovieDataEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieDataEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                MovieDataEntry.COLUMN_MOVIE_IMAGE_URL + " TEXT NOT NULL, " +
                MovieDataEntry.COLUMN_MOVIE_RATING + " TEXT NOT NULL, " +
                MovieDataEntry.COLUMN_MOVIE_POPULARITY + " TEXT NOT NULL, " +
                MovieDataEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, " +
                MovieDataEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                MovieDataEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieDataEntry.COLUMN_MOVIE_REVIEWS + " TEXT NOT NULL, " +
                MovieDataEntry.COLUMN_MOVIE_VIDEOS + " TEXT NOT NULL " +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieDataEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

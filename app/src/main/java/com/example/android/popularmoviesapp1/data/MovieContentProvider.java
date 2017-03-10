package com.example.android.popularmoviesapp1.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import static com.example.android.popularmoviesapp1.data.MovieDataListContract.MovieDataEntry.TABLE_NAME;

public class MovieContentProvider extends ContentProvider {

    private MovieDataListDBHelper mMovieDataListDBHelper;

    //ID to match against whole table
    private static final int MOVIES = 100;
    //ID to match against single Movie
    public static final int MOVIE_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        //match with directory
        uriMatcher.addURI(MovieDataListContract.AUTHORITY,MovieDataListContract.PATH_MOVIE_DATA_LIST, MOVIES);
        //match with single movie
        uriMatcher.addURI(MovieDataListContract.AUTHORITY,MovieDataListContract.PATH_MOVIE_DATA_LIST + "/#",MOVIE_WITH_ID);

        return uriMatcher;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mMovieDataListDBHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        int moviesDeleted;

        switch (match) {
            case MOVIE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                moviesDeleted = db.delete(TABLE_NAME, "movieID=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (moviesDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return moviesDeleted;
    }

    @Override
    public String getType(Uri uri) {
        int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIES:
                return "vnd.android.cursor.dir" + "/" + MovieDataListContract.AUTHORITY + "/" + MovieDataListContract.PATH_MOVIE_DATA_LIST;
            case MOVIE_WITH_ID:
                return "vnd.android.cursor.item" + "/" + MovieDataListContract.AUTHORITY + "/" + MovieDataListContract.PATH_MOVIE_DATA_LIST;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mMovieDataListDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        Uri returnUri;

        switch(match){
            case MOVIES:
                long id  = db.insert(TABLE_NAME,null,values);
                if(id>0){
                    returnUri = ContentUris.withAppendedId(MovieDataListContract.MovieDataEntry.CONTENT_URI, id);
                }else{
                    throw new android.database.SQLException("Failed to insert row into "+uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mMovieDataListDBHelper = new MovieDataListDBHelper(context);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mMovieDataListDBHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);

        Cursor returnCursor;

        switch (match){
            case MOVIES:
                returnCursor  = db.query(TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case MOVIE_WITH_ID:
                String id = uri.getPathSegments().get(1);

                String mSelection = "movieID=?";
                String[] mSelectionArgs = new String[]{id};

                returnCursor =  db.query(TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);
        }

        returnCursor.setNotificationUri(getContext().getContentResolver(),uri);

        return returnCursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int moviesUpdated;

        int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                moviesUpdated = mMovieDataListDBHelper.getWritableDatabase().update(TABLE_NAME, values, "movieID=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (moviesUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return moviesUpdated;
    }
}

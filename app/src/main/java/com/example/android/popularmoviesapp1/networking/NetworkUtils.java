/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Source: Udacity.com - Associate Android Developer Fast Track
 * Modified by: Tommaso Castrovillari
 */
package com.example.android.popularmoviesapp1.networking;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String THEMOVIEDB_ORG_POPULAR = "http://api.themoviedb.org/3/movie/popular";
    private static final String THEMOVIEDB_ORG_TOP_RATED = "http://api.themoviedb.org/3/movie/top_rated";
    private static final String THEMOVIEDB_API_KEY = "xxx";
    private static final String THEMOVIEDB_ORG_PREFIX = "http://api.themoviedb.org/3/movie/";
    private static final String TRAILERS_POSTFIX = "/videos";
    private static final String REVIEWS_POSTFIX = "/reviews";

    /**
     * This method builds and returns the URL according to the provided selection
     *
     * @param selection "byPopularity" for movies sorted by popularity and
     *                  "byRating" for movies sorted by rating
     *                  "videos" for movie videos
     *                  "reviews" for movie reviews
     *
     *  @param movieID id of the movie, only relevant for trailers and reviews selection
     *
     * @return The corresponding URL
     */
    public static URL buildUrl(String selection,String movieID) {
        Uri builtUri;

        switch (selection) {
            case "byPopularity":
                builtUri =
                        Uri.parse(THEMOVIEDB_ORG_POPULAR)
                                .buildUpon()
                                .appendQueryParameter("api_key",THEMOVIEDB_API_KEY)
                                .build();
                break;
            case "byRating":
                builtUri =
                        Uri.parse(THEMOVIEDB_ORG_TOP_RATED)
                                .buildUpon()
                                .appendQueryParameter("api_key",THEMOVIEDB_API_KEY)
                                .build();
                break;

            case "videos":
                builtUri =
                        Uri.parse(THEMOVIEDB_ORG_PREFIX
                                +movieID
                                +TRAILERS_POSTFIX)
                                .buildUpon()
                                .appendQueryParameter("api_key",THEMOVIEDB_API_KEY)
                                .build();
                break;
            case "reviews":
                builtUri =
                        Uri.parse(THEMOVIEDB_ORG_PREFIX
                                 +movieID
                                 +REVIEWS_POSTFIX)
                                .buildUpon()
                                .appendQueryParameter("api_key",THEMOVIEDB_API_KEY)
                                .build();
                break;
            default:
                Log.e(TAG, "Building URI with selection " + selection + " failed.\nCaused by: Unsupported selection");
                return null;
        }


        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "Building URL " + url + " failed.\nCaused by: " + e);
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
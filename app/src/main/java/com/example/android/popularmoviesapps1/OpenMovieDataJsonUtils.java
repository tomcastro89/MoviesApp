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
package com.example.android.popularmoviesapps1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;


public final class OpenMovieDataJsonUtils {

    public static String IMAGE_TMDB_ORG_T_P = "http://image.tmdb.org/t/p/";
    public static String POSTER_SIZE_KEY = "w185";

    /**
     * This method gets a JSON - String, extracts the relevant information
     * and returns it as an ArrayList of MovieData - Objects containing:
     * - Movie Poster Image URL
     * - Overview
     * - Release Date
     * - Original Title
     * - Popularity
     * - Rating
     *
     * @param movieDataJsonString An JSON - String
     * @return An ArrayList of MovieData - Objects
     * @throws JSONException
     */
    public static ArrayList<MovieData> getMovieDataFromJson(String movieDataJsonString)
            throws JSONException {

        final String OWM_MESSAGE_CODE = "cod";
        final String OWM_LIST = "results";

        JSONObject movieJson = new JSONObject(movieDataJsonString);

        /* Is there an error? */
        if (movieJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = movieJson.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray movieArray = movieJson.getJSONArray(OWM_LIST);

        ArrayList<MovieData> parsedMovieData = new ArrayList<>();

        for (int i = 0; i < movieArray.length(); i++) {

            JSONObject movieInformation = movieArray.getJSONObject(i);

            //Generate full Movie Image URL
            String movieImageURL = IMAGE_TMDB_ORG_T_P + POSTER_SIZE_KEY + "/";
            String poster_path = movieInformation.getString("poster_path");
            movieImageURL += poster_path;

            String movieOverview =  movieInformation.getString("overview");
            String movieReleaseDate = movieInformation.getString("release_date");
            String movieOriginalTitle = movieInformation.getString("original_title");
            double moviePopularity = movieInformation.getDouble("popularity");
            double movieRating = movieInformation.getDouble("vote_average");

            MovieData movieData = new MovieData(
                    movieImageURL,
                    movieRating,
                    moviePopularity,
                    movieOverview,
                    movieOriginalTitle,
                    movieReleaseDate);

            parsedMovieData.add(movieData);
        }

        return parsedMovieData;
    }
}
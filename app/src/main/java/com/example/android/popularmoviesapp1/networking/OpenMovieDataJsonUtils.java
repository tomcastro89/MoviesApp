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

import android.util.Log;

import com.example.android.popularmoviesapp1.data.MovieData;
import com.example.android.popularmoviesapp1.data.ReviewData;
import com.example.android.popularmoviesapp1.data.VideoData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;


public final class OpenMovieDataJsonUtils {

    private static final String TAG = OpenMovieDataJsonUtils.class.getSimpleName();

    public static String IMAGE_TMDB_ORG_T_P = "http://image.tmdb.org/t/p/";
    public static String POSTER_SIZE_KEY = "w185";
    private static String OWM_MESSAGE_CODE = "cod";
    private static String OWM_LIST = "results";

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
    public static Map<String,MovieData> getMovieDataFromJson(String movieDataJsonString)
            throws JSONException {

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

        Map<String,MovieData> parsedMovieData = new LinkedHashMap<>();

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
            String movieID = movieInformation.getString("id");
            ArrayList<ReviewData> reviews = parseReviewJSON(movieID);
            ArrayList<VideoData> videos = parseVideoJSON(movieID);

            MovieData movieData = new MovieData(
                    movieImageURL,
                    movieRating,
                    moviePopularity,
                    movieOverview,
                    movieOriginalTitle,
                    movieReleaseDate,
                    movieID,
                    reviews,
                    videos);

            parsedMovieData.put(movieID,movieData);
        }

        return parsedMovieData;
    }

    private static ArrayList<VideoData> parseVideoJSON(String movieID) throws JSONException {
        URL url = NetworkUtils.buildUrl("videos",movieID);
        ArrayList<VideoData> videos = new ArrayList<>();

        String jsonVideoDataResponse;
        try {
            jsonVideoDataResponse = NetworkUtils
                    .getResponseFromHttpUrl(url);
        } catch (Exception e) {
            Log.e(TAG, "Could not gather videos from " + url +"\nCaused by: " + e);
            return videos;
        }

        JSONObject videoJson = new JSONObject(jsonVideoDataResponse);

        /* Is there an error? */
        if (videoJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = videoJson.getInt(OWM_MESSAGE_CODE);

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

        JSONArray videoArray = videoJson.getJSONArray(OWM_LIST);

        for (int i = 0; i < videoArray.length(); i++) {

            JSONObject videoInformation = videoArray.getJSONObject(i);

            String videoID =  videoInformation.getString("id");
            String iso_639_1 = videoInformation.getString("iso_639_1");
            String iso_3166_1 = videoInformation.getString("iso_3166_1");
            String key = videoInformation.getString("key");
            String name = videoInformation.getString("name");
            String site = videoInformation.getString("site");
            int size = videoInformation.getInt("size");
            String type = videoInformation.getString("type");

            VideoData videoData = new VideoData(
                    videoID,
                    iso_639_1,
                    iso_3166_1,
                    key,
                    name,
                    site,
                    size,
                    type
            );

            //Just add Trailers
            if(type.equals("Trailer")){
                videos.add(videoData);
            }
        }

        return videos;
    }

    private static ArrayList<ReviewData> parseReviewJSON(String movieID) throws JSONException {
        URL url = NetworkUtils.buildUrl("reviews",movieID);
        ArrayList<ReviewData> reviews = new ArrayList<>();

        String jsonReviewDataResponse;
        try {
            jsonReviewDataResponse = NetworkUtils
                    .getResponseFromHttpUrl(url);
        } catch (Exception e) {
            Log.e(TAG, "Could not gather reviews from " + url +"\nCaused by: " + e);
            return reviews;
        }

        JSONObject reviewJson = new JSONObject(jsonReviewDataResponse);

        /* Is there an error? */
        if (reviewJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = reviewJson.getInt(OWM_MESSAGE_CODE);

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

        JSONArray reviewArray = reviewJson.getJSONArray(OWM_LIST);

        for (int i = 0; i < reviewArray.length(); i++) {

            JSONObject reviewInformation = reviewArray.getJSONObject(i);

            String reviewID =  reviewInformation.getString("id");
            String author = reviewInformation.getString("author");
            String content = reviewInformation.getString("content");
            String reviewUrl = reviewInformation.getString("url");

            ReviewData reviewData = new ReviewData(
                    reviewID,
                    author,
                    content,
                    reviewUrl
                    );

            reviews.add(reviewData);
        }

        return reviews;
    }
}
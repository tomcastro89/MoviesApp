/*
 * Author: Tommaso Castrovillari
 */
package com.example.android.popularmoviesapp1.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This Class provides an Object that represents a Movie and all the relevant information
 */
public class MovieData implements Serializable{

    private String movieImageURL;
    private double movieRating;
    private double moviePopularity;
    private String movieOverview;
    private String movieOriginalTitle;
    private String movieReleaseDate;
    private String movieID;
    private ArrayList<ReviewData> reviews = new ArrayList<>();
    private ArrayList<VideoData> videos = new ArrayList<>();
    private boolean isFavorite;

    public MovieData(
            String movieImageURL,
            double movieRating,
            double moviePopularity,
            String movieOverview,
            String movieOriginalTitle,
            String movieReleaseDate,
            String movieID,
            ArrayList reviews,
            ArrayList videos){

        this.movieImageURL = movieImageURL;
        this.movieRating = movieRating;
        this.moviePopularity = moviePopularity;
        this.movieOverview = movieOverview;
        this.movieOriginalTitle = movieOriginalTitle;
        this.movieReleaseDate = movieReleaseDate;
        this.movieID = movieID;
        this.reviews = reviews;
        this.videos = videos;
        this.isFavorite = false;
    }

    public void setMovieImageURL(String movieImage){
        this.movieImageURL = movieImage;
    }

    public void setMovieRating(int movieRating){
        this.movieRating = movieRating;
    }

    public void setMoviePopularity(int moviePopularity){
        this.moviePopularity = moviePopularity;
    }

    public void setMovieOverwiew(String movieOverview){
        this.movieOverview = movieOverview;
    }

    public void setMovieOriginalTitle(String movieOriginalTitle){
        this.movieOriginalTitle = movieOriginalTitle;
    }

    public void setMovieReleaseDate(String movieReleaseDate){
        this.movieReleaseDate = movieReleaseDate;
    }

    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }

    public void setVideos(ArrayList<VideoData> videos){ this.videos = videos;}

    public void setReviews(ArrayList<ReviewData> reviews){ this.reviews = reviews;}

    public void setIsFavorite(boolean isFavorite){this.isFavorite=isFavorite;}

    public String getMovieImageURL() {
        return movieImageURL;
    }

    public double getMovieRating() {
        return movieRating;
    }

    public double getMoviePopularity() {
        return moviePopularity;
    }

    public String getMovieOverview() {
        return movieOverview;
    }

    public String getMovieOriginalTitle() {
        return movieOriginalTitle;
    }

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public String getMovieID() {return movieID;}

    public ArrayList<VideoData> getVideos(){ return videos;}

    public ArrayList<ReviewData> getReviews(){return reviews;}

    public boolean isFavorite(){return isFavorite;}
}

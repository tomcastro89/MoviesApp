/*
 * Author: Tommaso Castrovillari
 */
package com.example.android.popularmoviesapps1;

/**
 * This Class provides an Object that represents a Movie and all the relevant information
 */
public class MovieData {

    private String movieImageURL;
    private double movieRating;
    private double moviePopularity;
    private String movieOverview;
    private String movieOriginalTitle;
    private String movieReleaseDate;

    public MovieData(
            String movieImageURL,
            double movieRating,
            double moviePopularity,
            String movieOverview,
            String movieOriginalTitle,
            String movieReleaseDate){

        this.movieImageURL = movieImageURL;
        this.movieRating = movieRating;
        this.moviePopularity = moviePopularity;
        this.movieOverview = movieOverview;
        this.movieOriginalTitle = movieOriginalTitle;
        this.movieReleaseDate = movieReleaseDate;
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
}

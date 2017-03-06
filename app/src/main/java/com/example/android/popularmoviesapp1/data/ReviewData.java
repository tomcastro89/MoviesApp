/*
 * Author: Tommaso Castrovillari
 */
package com.example.android.popularmoviesapp1.data;

import java.io.Serializable;

/**
 * This Class provides an Object that represents a Review and all the relevant information
 */
public class ReviewData implements Serializable{

    private String id;
    private String author;
    private String content;
    private String url;

    public ReviewData(
            String id,
            String author,
            String content,
            String url

    ) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.url = url;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }


    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }
}

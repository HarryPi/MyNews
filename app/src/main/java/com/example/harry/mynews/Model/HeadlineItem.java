package com.example.harry.mynews.Model;

/**
 * Created by harry on 15/03/2018.
 */

public class HeadlineItem {
    private String title;
    private String description;
    private String imgUrl;
    private String urlToArticle;
    private String sourceName;
    private Float rating;
    private String id;

    public HeadlineItem(String title, String description, String imgUrl, String urlToArticle, String sourceName, String id, Float rating) {
        this.title = title;
        this.description = description;
        this.imgUrl = imgUrl;
        this.urlToArticle = urlToArticle;
        this.sourceName = sourceName;
        this.id = id;
        this.rating = rating;
    }

    public String getSourceName() {
        return sourceName;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUrlToArticle() {
        return urlToArticle;
    }

    public void setUrlToArticle(String urlToArticle) {
        this.urlToArticle = urlToArticle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

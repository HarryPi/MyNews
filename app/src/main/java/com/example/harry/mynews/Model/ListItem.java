package com.example.harry.mynews.Model;

/**
 * Created by harry on 15/03/2018.
 */

public class ListItem {
    private String title;
    private String description;
    private String imgUrl;
    private String urlToArticle;

    public ListItem(String title, String description, String imgUrl, String url) {
        this.title = title;
        this.description = description;
        this.imgUrl = imgUrl;
        this.urlToArticle = url;
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
}

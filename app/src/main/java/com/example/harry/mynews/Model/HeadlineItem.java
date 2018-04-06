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

    public HeadlineItem(String title, String description, String imgUrl, String urlToArticle, String sourceName) {
        this.title = title;
        this.description = description;
        this.imgUrl = imgUrl;
        this.urlToArticle = urlToArticle;
        this.sourceName = sourceName;
    }

    public String getSourceName() {
        return sourceName;
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
}

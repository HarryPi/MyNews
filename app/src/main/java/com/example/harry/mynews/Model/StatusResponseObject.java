package com.example.harry.mynews.Model;

import java.util.List;

/**
 * Created by harry on 22/03/2018.
 */

public class StatusResponseObject {
    private String status;
    private int totalResults;
    private List<NewsHeadlinesResponseObject> articles;

    public StatusResponseObject(String status, int totalResults, List<NewsHeadlinesResponseObject> articles) {
        this.status = status;
        this.totalResults = totalResults;
        this.articles = articles;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<NewsHeadlinesResponseObject> getArticles() {
        return articles;
    }

    public void setArticles(List<NewsHeadlinesResponseObject> articles) {
        this.articles = articles;
    }
}

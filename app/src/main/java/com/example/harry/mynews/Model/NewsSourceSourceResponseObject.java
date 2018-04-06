package com.example.harry.mynews.Model;

import java.lang.reflect.Array;
import java.util.List;

public class NewsSourceSourceResponseObject {
    private String status;
    private List<NewsSourcesSourceObject> sources;

    public NewsSourceSourceResponseObject(String status, List<NewsSourcesSourceObject> sources) {
        this.status = status;
        this.sources = sources;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<NewsSourcesSourceObject> getSources() {
        return sources;
    }

    public void setSources(List<NewsSourcesSourceObject> sources) {
        this.sources = sources;
    }
}


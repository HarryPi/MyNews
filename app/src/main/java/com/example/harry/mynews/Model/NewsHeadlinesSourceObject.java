package com.example.harry.mynews.Model;

import android.support.annotation.Nullable;

/**
 * Created by harry on 21/03/2018.
 * Iternal object of a @{@link NewsHeadlinesResponseObject}
 */

public class NewsHeadlinesSourceObject {
    private @Nullable String id;
    private String name;

    public NewsHeadlinesSourceObject(@Nullable String id, String name) {
        this.id = id;
        this.name = name;
    }


    @Nullable
    public String getId() {
        return id;
    }

    public void setId(@Nullable String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

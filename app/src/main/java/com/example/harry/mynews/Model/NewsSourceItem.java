package com.example.harry.mynews.Model;

public class NewsSourceItem {
    private String id;
    private String language;
    private String name;
    private String country;
    private String description;

    public NewsSourceItem(String id, String language, String name, String country) {
        this.id = id;
        this.language = language;
        this.name = name;
        this.country = country;
    }

    public NewsSourceItem(String id, String language, String name, String country, String description) {
        this.id = id;
        this.language = language;
        this.name = name;
        this.country = country;
        this.description = description;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

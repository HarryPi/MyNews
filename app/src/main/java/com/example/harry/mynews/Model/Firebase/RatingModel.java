package com.example.harry.mynews.Model.Firebase;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class RatingModel {
    private String comment;
    private String user;
    private Float rating;
    private String userId;

    public RatingModel() {
    }

    public RatingModel(String comment, String user, Float rating, String userId) {
        this.comment = comment;
        this.user = user;
        this.rating = rating;
        this.userId = userId;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("comment", comment);
        result.put("user", user);
        result.put("rating", rating);
        result.put("userId", userId);

        return result;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

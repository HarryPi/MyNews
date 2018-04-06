package com.example.harry.mynews.Model.Firebase;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
public class UserModel {
    public List<String> preferredSources = new ArrayList<>();

    public UserModel() {
    }

    public UserModel(List<String> newsSources) {
        this.preferredSources = newsSources;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("preferredSources", preferredSources);

        return result;
    }
}

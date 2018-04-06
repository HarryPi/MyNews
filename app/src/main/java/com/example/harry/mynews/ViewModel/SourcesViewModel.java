package com.example.harry.mynews.ViewModel;

import android.support.annotation.Nullable;
import android.util.SparseBooleanArray;

import com.example.harry.mynews.Data.INewsApi;
import com.example.harry.mynews.Model.NewsSourceItem;
import com.example.harry.mynews.Model.NewsSourceSourceResponseObject;

import java.util.ArrayList;
import java.util.List;


import io.reactivex.Observable;

public class SourcesViewModel {
    private INewsApi api;
    private List<NewsSourceItem> allNewsSources = new ArrayList<>();
    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    public SourcesViewModel(INewsApi api) {
        this.api = api;
    }

    public Observable<NewsSourceSourceResponseObject>
    getNewsSources(@Nullable String country, @Nullable String language) {
        return this.api.getNewsSources(country, language, INewsApi.API_KEY);
    }

    public List<NewsSourceItem> getAllNewsSources() {
        return allNewsSources;
    }

    public void setAllNewsSources(List<NewsSourceItem> allNewsSources) {
        this.allNewsSources = allNewsSources;
    }

    public SparseBooleanArray getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(SparseBooleanArray selectedItems) {
        this.selectedItems = selectedItems;
    }
}

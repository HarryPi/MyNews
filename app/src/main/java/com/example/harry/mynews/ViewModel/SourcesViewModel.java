package com.example.harry.mynews.ViewModel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.SparseBooleanArray;

import com.example.harry.mynews.Data.INewsApi;
import com.example.harry.mynews.Model.HeadlineItem;
import com.example.harry.mynews.Model.NewsSourceItem;
import com.example.harry.mynews.Model.NewsSourceSourceResponseObject;
import com.example.harry.mynews.Model.StatusResponseObject;
import com.example.harry.mynews.Util.CountryLocUtil;

import java.util.ArrayList;
import java.util.List;


import io.reactivex.Emitter;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.schedulers.Schedulers;

public class SourcesViewModel {
    private INewsApi api;
    private List<NewsSourceItem> allNewsSources = new ArrayList<>();
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    private CountryLocUtil countryUtil;
    private List<HeadlineItem> headlines;
    private String currentCountry;
    private static final String TAG = SourcesViewModel.class.getSimpleName();


    public SourcesViewModel(INewsApi api, CountryLocUtil countryUtil) {
        this.api = api;
        this.countryUtil = countryUtil;
    }

    public Observable<NewsSourceSourceResponseObject>
    getNewsSources(@Nullable String country, @Nullable String language) {
        return this.api.getNewsSources(country, language, INewsApi.API_KEY);
    }

    public List<NewsSourceItem> getAllNewsSources() {
        return allNewsSources;
    }

    public SparseBooleanArray getSelectedItems() {
        return selectedItems;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("CheckResult")
    public Observable<List<HeadlineItem>> getUserHeadlines(String sources, boolean fromMemoryIfAvailable) {
        return Observable.create(sub -> {
            if (fromMemoryIfAvailable) {
                if (headlines != null) {
                    sub.onNext(headlines);
                    sub.onComplete();

                } else getHeadlinesFromDatabase(sources, sub);
            } else {
                getHeadlinesFromDatabase(sources, sub);
            }
        });
    }

    @SuppressLint("CheckResult")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getHeadlinesFromDatabase(String sources, ObservableEmitter<List<HeadlineItem>> sub) {
        List<HeadlineItem> items = new ArrayList<>();
        api.topHeadlines(null, sources, INewsApi.API_KEY)
                .subscribeOn(Schedulers.newThread())
                .doOnComplete(() -> {
                    headlines = items;
                    sub.onNext(items);
                    sub.onComplete();
                })
                .subscribe(statusResponseObject ->
                        statusResponseObject.getArticles().forEach(a -> items.add(new HeadlineItem(a.getTitle(),
                                a.getDescription(),
                                a.getUrlToImage(),
                                a.getUrl(),
                                a.getSource().getName(),
                                a.getSource().getId()))));

    }

    @SuppressLint("MissingPermission") // Check performed internally
    public void loadCountryWithPermissions(Activity activity) {
        this.currentCountry = countryUtil.getUserCountryOrDefault(activity);
    }

    /**
     * This function is for testing and when no activity is provided
     * thus marked deprecated
     */
    @Deprecated
    @SuppressLint("MissingPermission")
    public void loadCountryWithNoPermissions() {
        this.currentCountry = countryUtil.getUserCountryOrDefault();
    }
    public void resetModelMemory() {
        this.headlines = null;
    }
}

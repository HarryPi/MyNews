package com.example.harry.mynews.ViewModel;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.example.harry.mynews.Constants.Countries;
import com.example.harry.mynews.Data.INewsApi;
import com.example.harry.mynews.Model.StatusResponseObject;
import com.example.harry.mynews.Util.CountryLocUtil;

import io.reactivex.Observable;

/**
 * Created by harry on 22/03/2018.
 */

public class NewsViewModel {
    // Injected at constructor
    private INewsApi api;
    private CountryLocUtil countryUtil;

    private static final String TAG = NewsViewModel.class.getSimpleName();

    private String currentCountry;
    private Observable<StatusResponseObject> loadedHeadlines;

    public NewsViewModel(INewsApi api, CountryLocUtil countryUtil) {
        this.api = api;
        this.countryUtil = countryUtil;
    }


    public void loadTopHeadlinesByCountry() {
        if (!Countries.allCountries.contains(this.currentCountry)) {
            this.setCurrentCountry("gb");
        }
        loadedHeadlines = api.topHeadlines(currentCountry,null, INewsApi.API_KEY);
    }
    public void loadTopHeadlinesBySources(String sources) {
        loadedHeadlines = api.topHeadlines(null, sources, INewsApi.API_KEY);
    }

    @SuppressLint("MissingPermission") // Check performed internally
    public void loadCountryWithPermissions(Activity activity) {
        this.currentCountry = countryUtil.getUserCountryOrDefault(activity);
    }

    /**
     * This function is for testing and when no activity is provided
     * thus marked deprecated
     * */
    @Deprecated
    @SuppressLint("MissingPermission")
    public void loadCountryWithNoPermissions() {
        this.currentCountry = countryUtil.getUserCountryOrDefault();
    }

    public void setCurrentCountry(String currentCountry) {
        this.currentCountry = currentCountry;
    }

    public Observable<StatusResponseObject> getLoadedHeadlines() {
        return loadedHeadlines;
    }

}

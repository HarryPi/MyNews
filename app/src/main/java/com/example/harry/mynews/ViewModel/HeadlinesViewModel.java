package com.example.harry.mynews.ViewModel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.RequiresPermission;

import com.example.harry.mynews.Constants.Countries;
import com.example.harry.mynews.Data.INewsApi;
import com.example.harry.mynews.Manifest;
import com.example.harry.mynews.Model.StatusResponseObject;
import com.example.harry.mynews.Util.CountryLocUtil;

import io.reactivex.Observable;

/**
 * Created by harry on 22/03/2018.
 */

public class HeadlinesViewModel {
    // Injected at constructor
    private INewsApi api;
    private CountryLocUtil countryUtil;

    private static final String TAG = HeadlinesViewModel.class.getSimpleName();

    private String currentCountry;
    private Observable<StatusResponseObject> loadedHeadlines;

    @RequiresPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
    public HeadlinesViewModel(INewsApi api, CountryLocUtil countryUtil) {
        this.api = api;
        this.countryUtil = countryUtil;
    }

    public void loadTopHeadlines() {
        if (!Countries.allCountries.contains(this.currentCountry)) {
            this.setCurrentCountry("gb");
        }
        loadedHeadlines = api.topHeadlinesByCountry(currentCountry, INewsApi.API_KEY);
    }

    @SuppressLint("MissingPermission") // Check performed internally
    public void loadCountryWithPermissions(Activity activity) {
        this.currentCountry = countryUtil.getUserCountryOrDefault(activity);
    }

    /**
     * This function is for testing and when no activity is provided
     * */
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

package com.example.harry.mynews.Data;

import com.example.harry.mynews.Model.StatusResponseObject;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by harry on 22/03/2018.
 */

public interface INewsApi {
    String API_KEY = "6d3bd75e063c40079ad367e3a3c56e9f"; // todo: somehow obfuscate this check ProGuard
    @GET("top-headlines")
    public Observable<StatusResponseObject> topHeadlinesByCountry(@Query("country") String country, @Header("Authorization") String key);
}

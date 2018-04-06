package com.example.harry.mynews.Data;

import com.example.harry.mynews.Model.NewsSourceSourceResponseObject;
import com.example.harry.mynews.Model.StatusResponseObject;


import javax.annotation.Nullable;

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
    public Observable<StatusResponseObject>
    topHeadlines(@Nullable @Query("country") String country,
                 @Nullable @Query("sources") String sources,
                 @Header("Authorization") String key);

    @GET("sources")
    public Observable<NewsSourceSourceResponseObject>
    getNewsSources(@Nullable @Query("country") String country,
                   @Nullable @Query("language") String language,
                   @Header("Authorization") String key);
}

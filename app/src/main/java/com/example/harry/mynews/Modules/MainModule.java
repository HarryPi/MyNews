package com.example.harry.mynews.Modules;

import android.app.Application;
import android.util.Log;

import com.example.harry.mynews.Data.INewsApi;
import com.example.harry.mynews.Util.CountryLocUtil;
import com.example.harry.mynews.ViewModel.NewsViewModel;
import com.example.harry.mynews.ViewModel.SourcesViewModel;
import com.example.harry.mynews.ViewModel.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by harry on 20/03/2018.
 * Holds the main dependencies that need to be injected
 */
@Module
public class MainModule {

    @Singleton
    @Provides
    public DatabaseReference providesDatabaseReference(FirebaseDatabase database) {
        return database.getReference();
    }
    @Singleton
    @Provides
    public FirebaseDatabase providesDatabase() {
        return FirebaseDatabase.getInstance();
    }
    @Singleton
    @Provides
    public FirebaseAuth providesFireBaseAuth() {
        return FirebaseAuth.getInstance();
    }
    @Provides
    public UserViewModel providesUserViewModel(FirebaseAuth auth, DatabaseReference reference) {
        return new UserViewModel(auth.getCurrentUser(), reference);
    }
    @Singleton
    @Provides
    public CountryLocUtil provideCountryLocUtil(Application app){
        return new CountryLocUtil(app);
    }

    @Singleton
    @Provides
    public Retrofit retrofit(OkHttpClient okHttpClient, GsonConverterFactory gsonConverterFactory, Gson gson) {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://newsapi.org/v2/")
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
    @Provides
    @Singleton
    public SourcesViewModel providesSourcesViewModel(INewsApi api) {
        return new SourcesViewModel(api);
    }
    @Provides
    public File providesFile(Application application) {
        File file = new File(application.getApplicationContext().getCacheDir(), "HttpCache");
        file.mkdir();
        return file;
    }

    @Provides
    public Cache providesCache(File cacheFile) {
        return new Cache(cacheFile, 10 * 1000 * 1000); // 10MB
    }

    @Singleton
    @Provides
    public OkHttpClient providesOkHttpClient(Cache cache, HttpLoggingInterceptor httpLoggingInterceptor) {
        return new OkHttpClient()
                .newBuilder()
                .cache(cache)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @Singleton
    @Provides
    public OkHttp3Downloader providesOkHttp3Downloader(OkHttpClient okHttpClient) {
        return new OkHttp3Downloader(okHttpClient);
    }

    @Singleton
    @Provides
    public Picasso providesPicasso(Application application, OkHttp3Downloader okHttp3Downloader) {
        return new Picasso.Builder(application.getApplicationContext())
                .downloader(okHttp3Downloader)
                .build();
    }

    @Provides
    @Singleton
    public HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor =
                new HttpLoggingInterceptor(message -> Log.d("Logger", message));
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }
    @Singleton
    @Provides
    NewsViewModel providesNewsViewModel(INewsApi api, CountryLocUtil util) {
        return new NewsViewModel(api, util);
    }

    @Singleton
    @Provides
    Gson providesGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.IDENTITY);
        return gsonBuilder.create();
    }

    @Singleton
    @Provides
    public GsonConverterFactory gsonConverterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }

    @Singleton
    @Provides
    public INewsApi provideINewsApi(Retrofit retrofit) {
        return retrofit.create(INewsApi.class);
    }
}

package com.example.harry.mynews.Modules;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by harry on 20/03/2018.
 */
@Module
public class AppModule {
    public Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application providesApplication(){
        return this.application;
    }
}

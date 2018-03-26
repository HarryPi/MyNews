package com.example.harry.mynews.Components;

import com.example.harry.mynews.Activities.MainActivity;
import com.example.harry.mynews.Modules.AppModule;
import com.example.harry.mynews.Modules.MainModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by harry on 20/03/2018.
 */
@Singleton
@Component(modules = {AppModule.class, MainModule.class})
public interface MainComponent {
    void inject(MainActivity activity);
}

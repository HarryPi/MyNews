package com.example.harry.mynews.Components;

import com.example.harry.mynews.Activities.BaseActivity;
import com.example.harry.mynews.Activities.LogInActivity;
import com.example.harry.mynews.Activities.MainActivity;
import com.example.harry.mynews.Activities.SourceSelectionActivity;
import com.example.harry.mynews.Modules.AppModule;
import com.example.harry.mynews.Modules.MainModule;

import javax.inject.Singleton;
import javax.xml.transform.Source;

import dagger.Component;

/**
 * Created by harry on 20/03/2018.
 */
@Singleton
@Component(modules = {AppModule.class, MainModule.class})
public interface MainComponent {
    void inject(MainActivity activity);
    void inject(SourceSelectionActivity activity);
    void inject(LogInActivity activity);
    void inject(BaseActivity activity);
}

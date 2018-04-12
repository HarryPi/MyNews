package com.example.harry.mynews;

import android.app.Application;

import com.example.harry.mynews.Components.DaggerMainComponent;
import com.example.harry.mynews.Components.MainComponent;
import com.example.harry.mynews.Modules.AppModule;
import com.example.harry.mynews.Modules.MainModule;

/**
 * Created by harry on 20/03/2018.
 */

public class App extends Application {
    private MainComponent mainComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mainComponent = DaggerMainComponent.builder()
                .appModule(new AppModule(this))
                .mainModule(new MainModule())
                .build();
    }

    public MainComponent getMainComponent(){
        return this.mainComponent;
    }
}

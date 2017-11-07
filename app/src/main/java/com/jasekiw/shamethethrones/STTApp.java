package com.jasekiw.shamethethrones;


import android.app.Application;

import com.jasekiw.shamethethrones.providers.AppComponent;
import com.jasekiw.shamethethrones.providers.AppModule;
import com.jasekiw.shamethethrones.providers.DaggerAppComponent;
import com.jasekiw.shamethethrones.providers.location.LocationModule;
import com.jasekiw.shamethethrones.providers.map.MapModule;

/**
 * The Shame The Thrones Application object
 */
public class STTApp extends Application {
    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .locationModule(new LocationModule())
                .mapModule(new MapModule())
                .build();
        // in order to add a module you must add it to the AppModule interface first
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}

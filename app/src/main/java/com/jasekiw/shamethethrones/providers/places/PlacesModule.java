package com.jasekiw.shamethethrones.providers.places;


import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PlacesModule {
    @Provides
    @Singleton
    PlacesWebApiSearch providePlacesWebApiSearch(Application app) {
        return new PlacesWebApiSearch(app);
    }
}

package com.jasekiw.shamethethrones.providers.location;

import dagger.Module;
import dagger.Provides;

@Module
public class LocationModule {

    @Provides
    LocationController provideLocationController() {
        return new LocationController();
    }

}

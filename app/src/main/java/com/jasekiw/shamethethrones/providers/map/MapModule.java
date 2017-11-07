package com.jasekiw.shamethethrones.providers.map;

import android.app.Application;

import com.jasekiw.shamethethrones.providers.map.util.MarkerAnimator;

import dagger.Module;
import dagger.Provides;

@Module
public class MapModule {

    @Provides
    MapController provideMapController(Application context) {
        return new MapController(new MarkerAnimator(context));
    }


    @Provides
    AddRestroomMarkerController provideAddRestroomMarkerController() {
        return new AddRestroomMarkerController();
    }

}

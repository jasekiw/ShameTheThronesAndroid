package com.jasekiw.shamethethrones.providers.map;

import android.app.Application;

import com.jasekiw.shamethethrones.providers.map.util.MarkerAnimator;
import com.jasekiw.shamethethrones.providers.api.RestroomApi;

import dagger.Module;
import dagger.Provides;

@Module
public class MapModule {

    @Provides
    MapController provideMapController(Application context, RestroomApi api) {
        return new MapController(new MarkerAnimator(context), new MapRestroomMarkersController(api));
    }


    @Provides
    AddRestroomMarkerController provideAddRestroomMarkerController() {
        return new AddRestroomMarkerController();
    }

}

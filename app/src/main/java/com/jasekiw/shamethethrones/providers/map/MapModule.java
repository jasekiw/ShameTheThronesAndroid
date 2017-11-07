package com.jasekiw.shamethethrones.providers.map;

import com.jasekiw.shamethethrones.providers.map.util.MarkerAnimation;
import com.jasekiw.shamethethrones.providers.util.BitmapUtilities;

import dagger.Module;
import dagger.Provides;

@Module
public class MapModule {

    @Provides
    MapController provideMapController() {
        return new MapController();
    }


    @Provides
    AddRestroomMarkerController provideAddRestroomMarkerController() {
        return new AddRestroomMarkerController();
    }

    @Provides
    MarkerAnimation provideMarkerAnimation(BitmapUtilities bmu) {
        return new MarkerAnimation(bmu);
    }

    @Provides
    ManagedMarker provideManagedMarker(ManagedMarker mm) {
        return new ManagedMarker();
    }
}

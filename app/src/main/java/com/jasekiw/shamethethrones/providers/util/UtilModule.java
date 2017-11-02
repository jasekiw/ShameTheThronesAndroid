package com.jasekiw.shamethethrones.providers.util;

import android.app.Application;

import dagger.Module;
import dagger.Provides;

@Module
public class UtilModule {

    @Provides
    public BitmapUtilities provideBitmapUtilities(Application app) {
        return new BitmapUtilities(app);
    }
}

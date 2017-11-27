package com.jasekiw.shamethethrones.providers.restroom;

import android.app.Application;

import dagger.Module;
import dagger.Provides;

@Module
public class RestroomModule {
    @Provides
    AddRestroomController provideAddRestroomController() {
        return new AddRestroomController();
    }
}

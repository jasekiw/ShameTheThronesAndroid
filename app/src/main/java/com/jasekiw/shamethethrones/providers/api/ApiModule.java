package com.jasekiw.shamethethrones.providers.api;

import android.app.Application;

import com.android.volley.RequestQueue;

import dagger.Module;
import dagger.Provides;

@Module
public class ApiModule {
    @Provides
    RatingApi provideRatingApi(RequestQueue rq, Application app) {
        return new RatingApi(rq, app);
    }

    @Provides
    RestroomApi provideRestroomApi(RequestQueue rq, Application app) {
        return new RestroomApi(rq, app);
    }
}

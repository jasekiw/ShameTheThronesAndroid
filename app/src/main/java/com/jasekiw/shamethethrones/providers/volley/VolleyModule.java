package com.jasekiw.shamethethrones.providers.volley;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class VolleyModule {

    @Provides
    @Singleton
    public RequestQueue provideRequestQueue(Application app) {
        return Volley.newRequestQueue(app);
    }
}

package com.jasekiw.shamethethrones.providers;

import com.jasekiw.shamethethrones.activites.MainActivity;
import com.jasekiw.shamethethrones.providers.location.LocationModule;
import com.jasekiw.shamethethrones.providers.map.MapModule;
import com.jasekiw.shamethethrones.providers.places.PlacesModule;
import com.jasekiw.shamethethrones.providers.restroom.RestroomModule;
import com.jasekiw.shamethethrones.providers.volley.VolleyModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Add modules to the app here
 */
@Singleton
@Component(modules = {
        AppModule.class,
        LocationModule.class,
        MapModule.class,
        RestroomModule.class,
        VolleyModule.class,
        PlacesModule.class
})
public interface AppComponent {
    void inject(MainActivity activity);
}

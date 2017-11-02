package com.jasekiw.shamethethrones.providers;

import com.jasekiw.shamethethrones.activites.MainActivity;
import com.jasekiw.shamethethrones.activites.SplashActivity;
import com.jasekiw.shamethethrones.providers.location.LocationModule;
import com.jasekiw.shamethethrones.providers.map.MapModule;
import com.jasekiw.shamethethrones.providers.util.UtilModule;

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
        UtilModule.class
})
public interface AppComponent {
    void inject(MainActivity activity);

    void inject(SplashActivity activity);

}

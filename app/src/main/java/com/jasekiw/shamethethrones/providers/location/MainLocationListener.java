package com.jasekiw.shamethethrones.providers.location;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

public class MainLocationListener implements android.location.LocationListener {

    private LocationHandler mlocationHandler;

    public MainLocationListener(LocationHandler locationHandler) {
        mlocationHandler = locationHandler;
    }

    @Override
    public void onLocationChanged(Location location) {

//        Log.d("location", location.getLatitude() + "," + location.getLongitude());
        mlocationHandler.onLocationChanged(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Log.d("location", "enabled");
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.d("location", "disabled");
    }
}

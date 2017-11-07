package com.jasekiw.shamethethrones.activites;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.jasekiw.shamethethrones.R;
import com.jasekiw.shamethethrones.STTApp;
import com.jasekiw.shamethethrones.providers.location.LocationController;
import com.jasekiw.shamethethrones.providers.location.LocationHandler;
import com.jasekiw.shamethethrones.providers.map.AddRestroomMarkerController;
import com.jasekiw.shamethethrones.providers.map.MapController;

import javax.inject.Inject;

public class MainActivity extends FragmentActivity implements LocationHandler {

    private GoogleMap mMap;

    @Inject
    public LocationController mLocationController;

    @Inject
    public MapController mMapController;


    @Inject
    public AddRestroomMarkerController mTouchController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        ((STTApp)getApplication()).getAppComponent().inject(this);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);



        mTouchController = new AddRestroomMarkerController();
        mTouchController.initialize(findViewById(R.id.animation_view),findViewById(R.id.mainTopLayout));


        mMapController.initialize(mapFragment, this, mTouchController);
        mLocationController.setActivity(this);
        mLocationController.setLocationHandler(this);
        mLocationController.initializeLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d("location", "permission event");
        switch (requestCode) {
            case LocationController.MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                mLocationController.onRequestPermissionsResult(grantResults);
                return;
            }
        }
    }

    @Override
    public void onLocationChanged(final Location location) {
        Log.d("location", "handling location change");
        OnMapReadyCallback callback = new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                changeLocation(location);
            }
        };
        if(!mMapController.isMapReady())
            mMapController.setOnMapReady(callback);
        else
            changeLocation(location);
    }

    private void changeLocation(Location location) {
        Log.d("location", "adding marker");
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        mMapController.changeCurrentLocation(latLng);
    }


}

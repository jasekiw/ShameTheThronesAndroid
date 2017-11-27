package com.jasekiw.shamethethrones.activites;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
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
import com.jasekiw.shamethethrones.providers.places.AndroidPlacesApi;
import com.jasekiw.shamethethrones.providers.places.PlacesWebApiSearch;
import com.jasekiw.shamethethrones.providers.restroom.AddRestroomController;

import javax.inject.Inject;
import com.google.android.gms.location.places.Places;
import com.jasekiw.shamethethrones.providers.restroom.OnAddRestroomCancelledListener;

public class MainActivity extends FragmentActivity implements LocationHandler, OnAddRestroomCancelledListener {

    private static final String NEW_RESTROOM_TAG = "NEW_RESTROOM_FRAGMENT";
    private GoogleMap mMap;

    @Inject
    public LocationController mLocationController;

    @Inject
    public MapController mMapController;


    @Inject
    public AddRestroomMarkerController mTouchController;

    @Inject
    public AddRestroomController mAddRestroomController;


    @Inject
    public PlacesWebApiSearch mPlacesWebApiSearch;

    private AddRestroomFragment mAddRestroomFragment;
    private AndroidPlacesApi mAndroidPlacesApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        ((STTApp)getApplication()).getAppComponent().inject(this);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mAndroidPlacesApi = new AndroidPlacesApi(AndroidPlacesApi.getGoogleApiClient(this));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        Log.d("Main", "Thread: " + android.os.Process.myTid());

        mTouchController.initialize(findViewById(R.id.animation_view), findViewById(R.id.mainTopLayout), (latLng) ->
            mPlacesWebApiSearch.getPlaceIdFromLatLng(this, latLng, result -> showAddRestroomFragment(result.placeId, latLng))
        );

        mMapController.initialize(mapFragment, this, mTouchController, poi -> showAddRestroomFragment(poi.placeId, poi.latLng));
        mLocationController.setActivity(this);
        mLocationController.setLocationHandler(this);
        mLocationController.initializeLocation();
        if(savedInstanceState != null) // device was rotated so we need to grab the new restroom fragment if it exists
            mAddRestroomFragment = (AddRestroomFragment) getFragmentManager().findFragmentByTag(NEW_RESTROOM_TAG);

    }


    public void showAddRestroomFragment(String placeId, LatLng latLng) {
        mTouchController.hideMarker();
        mAddRestroomFragment =  AddRestroomFragment.newInstance(placeId, latLng, mAndroidPlacesApi);
        FragmentManager fm = getFragmentManager();
            fm.beginTransaction()
                    .setCustomAnimations(R.animator.slide_in_top, R.animator.slide_out_top)
                    .add(R.id.add_restroom_layout, mAddRestroomFragment,NEW_RESTROOM_TAG)
                    .commit();

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
        OnMapReadyCallback callback = googleMap -> changeLocation(location);
        if(!mMapController.isMapReady())
            mMapController.setOnMapReady(callback);
        else
            changeLocation(location);
    }

    private void changeLocation(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        mMapController.changeCurrentLocation(latLng);
    }


    @Override
    public void onAddRestroomCancelled() {
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .setCustomAnimations(R.animator.slide_in_top, R.animator.slide_out_top)
                .remove(mAddRestroomFragment)
                .commit();
    }
}

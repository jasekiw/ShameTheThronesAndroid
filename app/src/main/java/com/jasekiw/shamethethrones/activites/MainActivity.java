package com.jasekiw.shamethethrones.activites;

import android.app.FragmentManager;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.jasekiw.shamethethrones.R;
import com.jasekiw.shamethethrones.STTApp;
import com.jasekiw.shamethethrones.models.restroom.PlaceInformation;
import com.jasekiw.shamethethrones.models.restroom.RestroomModel;
import com.jasekiw.shamethethrones.providers.location.LocationController;
import com.jasekiw.shamethethrones.providers.location.LocationHandler;
import com.jasekiw.shamethethrones.providers.map.AddRestroomMarkerController;
import com.jasekiw.shamethethrones.providers.map.MapController;
import com.jasekiw.shamethethrones.providers.places.AndroidPlacesApi;
import com.jasekiw.shamethethrones.providers.places.PlacesWebApiSearch;
import com.jasekiw.shamethethrones.providers.restroom.AddRestroomFragmentController;

import javax.inject.Inject;

import com.jasekiw.shamethethrones.models.restroom.AddRestroomModel;
import com.jasekiw.shamethethrones.providers.restroom.RestroomFragmentController;

public class MainActivity extends FragmentActivity implements LocationHandler,
        AddRestroomFragmentController.OnAddRestroomCancelledListener,
        RestroomFragmentController.OnRestroomCancelledListener{

    private static final String NEW_RESTROOM_TAG = "NEW_RESTROOM_FRAGMENT";
    private static final String VIEW_RESTROOM_TAG = "VIEW_RESTROOM_FRAGMENT";
    @Inject
    public LocationController mLocationController;
    @Inject
    public MapController mMapController;
    @Inject
    public AddRestroomMarkerController mTouchController;
    @Inject
    public AddRestroomFragmentController mAddRestroomController;
    @Inject
    public PlacesWebApiSearch mPlacesWebApiSearch;
    private AndroidPlacesApi mAndroidPlacesApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.onStart();
        Log.d("MainActivity", "Thread#: " + android.os.Process.myTid());
        // hide navigation bar
        if(getActionBar() != null)
            getActionBar().hide();
        // perform DI
        ((STTApp)getApplication()).getAppComponent().inject(this);
        setContentView(R.layout.activity_main);
        // create android places api
        mAndroidPlacesApi = new AndroidPlacesApi(AndroidPlacesApi.getGoogleApiClient(this));
//        handlePlaceStuff();
        mTouchController.initialize(findViewById(R.id.animation_view), findViewById(R.id.mainTopLayout), (latLng) -> {
            int radius = mMapController.getCurrentCameraPosition().zoom < 17 ? 200 : 50;
            PlacesWebApiSearch.SearchParams params = new PlacesWebApiSearch.SearchParams(latLng, radius);
            mPlacesWebApiSearch.getPlaceIdFromLatLng(this, params, result -> showAddRestroomFragment(result.placeId, result.name, latLng));
        });
        // initialize the map controller
        mMapController.initialize(
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map),
                this,
                mTouchController, poi -> showAddRestroomFragment(poi.placeId, poi.name, poi.latLng),
                this::showViewRestroomFragment
        );
        // location
        mLocationController.setActivity(this);
        mLocationController.setLocationHandler(this);
        mLocationController.initializeLocation();
        if(savedInstanceState != null)
            onRestore(savedInstanceState);
    }

    /**
     * Restore data when the screen is restored
     * @param savedInstanceState
     */
    protected void onRestore(Bundle savedInstanceState) {

        mMapController.restore(savedInstanceState);
    }


    /**
     * Save data when the screen is rotated
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapController.save(outState);
    }



    public void showViewRestroomFragment(RestroomModel restroomModel) {
        mTouchController.hideMarker();
        mAndroidPlacesApi.getPlaceById(place -> {
            mAndroidPlacesApi.getPlacePhotos(restroomModel.getPlaceId(), photo -> {
                restroomModel.setPlaceInformation(new PlaceInformation(place.getName().toString(), photo));
                ViewRestroomFragment viewRestroomFragment =  ViewRestroomFragment.newInstance(restroomModel);
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction()
                        .setCustomAnimations(R.animator.slide_in_top, R.animator.slide_out_top)
                        .add(R.id.restroom_container, viewRestroomFragment, VIEW_RESTROOM_TAG)
                        .commit();

            });
        }, restroomModel.getPlaceId());

    }


    /**
     * Show the add restroom screen
     * @param placeId
     * @param latLng
     */
    public void showAddRestroomFragment(String placeId, String placeName, LatLng latLng) {
        mTouchController.hideMarker();

        mAndroidPlacesApi.getPlacePhotos(placeId, photo -> {
            AddRestroomFragment addRestroomFragment =  AddRestroomFragment.newInstance(new AddRestroomModel(placeName, photo, placeId, latLng));
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction()
                    .setCustomAnimations(R.animator.slide_in_top, R.animator.slide_out_top)
                    .add(R.id.restroom_container, addRestroomFragment,NEW_RESTROOM_TAG)
                    .commit();

        });
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
        if(!mMapController.isMapReady())
            mMapController.addOnMapReady(googleMap -> changeLocation(location));
        else
            changeLocation(location);
    }

    /**
     * Handle location updates and assign the current location to it
     * @param location
     */
    private void changeLocation(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        mMapController.changeCurrentLocation(latLng);
    }

    /**
     * Remove the add restroom fragment
     */
    @Override
    public void onAddRestroomCancelled() {
        // device was rotated so we need to grab the new restroom fragment if it exists. This will return null if the fragment wasn't loaded
        AddRestroomFragment addRestroomFragment = (AddRestroomFragment) getFragmentManager().findFragmentByTag(NEW_RESTROOM_TAG);
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .setCustomAnimations(R.animator.slide_in_top, R.animator.slide_out_top)
                .remove(addRestroomFragment)
                .commit();
    }

    @Override
    public void onRestroomCancelled() {
        ViewRestroomFragment viewRestroomFragment = (ViewRestroomFragment) getFragmentManager().findFragmentByTag(VIEW_RESTROOM_TAG);
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .setCustomAnimations(R.animator.slide_in_top, R.animator.slide_out_top)
                .remove(viewRestroomFragment)
                .commit();
    }
}

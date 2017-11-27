package com.jasekiw.shamethethrones.providers.map;

// main context import
import android.content.Context;
import android.util.Log;

// google map imports
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
// application imports

import com.jasekiw.shamethethrones.R;
import com.jasekiw.shamethethrones.providers.map.util.MarkerAnimator;

public class MapController {

    private GoogleMap mMap;
    private Marker mCurrentLocationMarker;
    private Context mContext;
    private OnMapReadyCallback mReadyCallback;
    private AddRestroomMarkerController mTouchController;
    private MarkerAnimator mAnimator;
    public boolean isMapReady() {
        return mMap != null;
    }


    public MapController(MarkerAnimator animator) {
        mAnimator = animator;
    }

    /**
     * Initialize the map controller with the given map, context, and add restroom marker controller.
     * @param mapFragment
     * @param context
     * @param touchController
     */
    public void initialize(SupportMapFragment mapFragment, Context context, AddRestroomMarkerController touchController, GoogleMap.OnPoiClickListener onPoiClickListener) {
        mContext = context;
        mTouchController = touchController;
        mapFragment.getMapAsync(googleMap -> {
            Log.d("map", "handle map ready");
            mMap = googleMap;
            mMap.setOnMapClickListener(latLng -> {
                Log.d("map", "onMapClick");
                mTouchController.toggle(latLng);
            });
            mMap.setOnCameraMoveListener(() -> mTouchController.hideMarker());
            mMap.setOnPoiClickListener((poi) -> {
                Log.d("map", "Point Of Interest");
                onPoiClickListener.onPoiClick(poi);
            });
            googleMap.setMaxZoomPreference(20);
            googleMap.setMinZoomPreference(17);
            if(mReadyCallback != null)
                mReadyCallback.onMapReady(googleMap);
        });
    }

    public void setOnMapReady(OnMapReadyCallback callback) {
        mReadyCallback = callback;
    }


    /**
     * Adjust current location marker to the latitude and longitude given
     * @param latLng The now current location
     */
    public void changeCurrentLocation(LatLng latLng) {
        if(mCurrentLocationMarker != null)
            mCurrentLocationMarker.setPosition(latLng);
        else
            mCurrentLocationMarker = addMarker(latLng, true, true, R.drawable.current_location_marker, 0.5f, 0.5f, -1 );
    }



    public Marker addMarker(LatLng latLng) {
        return addMarker(latLng, false, false);
    }

    public Marker addMarker(LatLng latLng, boolean zoomTo) {
        return addMarker(latLng, zoomTo, false);
    }

    public Marker addMarker(LatLng latLng, boolean zoomTo, boolean moveTo) {
        return addMarker(latLng, zoomTo, moveTo, -1, 0, 0, -1);
    }

    public Marker addMarker(LatLng latLng, boolean zoomTo, boolean moveTo, int resourceID, float anchorX, float anchorY, int size) {
        MarkerOptions options = new MarkerOptions();
        options.position(latLng);
        if(anchorX != 0 && anchorY != 0)
            options.anchor(anchorX, anchorY);

        if(resourceID != -1) {
            if(size != -1)
                options.icon(BitmapDescriptorFactory.fromBitmap(mAnimator.resize(resourceID, size,size)));
            else
                options.icon(BitmapDescriptorFactory.fromResource(resourceID));

        }

        Marker marker =  mMap.addMarker(options);
        if(moveTo)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        if(zoomTo)
            mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
        return marker;
    }


}

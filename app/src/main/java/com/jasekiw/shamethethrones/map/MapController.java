package com.jasekiw.shamethethrones.map;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jasekiw.shamethethrones.R;

public class MapController implements GoogleMap.OnMapClickListener, GoogleMap.OnCameraMoveListener {

    private GoogleMap mMap;
    private Marker mCurrentLocationMarker;
    private Marker mAddRestroomMarker;

    public MapController(GoogleMap map) {
        mMap = map;
        initialize();
    }

    private void initialize() {
        mMap.setOnMapClickListener(this);
        mMap.setOnCameraMoveListener(this);
    }


    @Override
    public void onMapClick(LatLng latLng) {
        createAddRestroomPopup(latLng);
    }

    private void createAddRestroomPopup(LatLng latLng) {
        if(mAddRestroomMarker != null) {
            mAddRestroomMarker.remove();
            mAddRestroomMarker = null;
        }
        else
            mAddRestroomMarker = addMarker(latLng);
    }


    public void changeCurrentLocation(LatLng latLng) {

        if(mCurrentLocationMarker != null)
            mCurrentLocationMarker.setPosition(latLng);
        else
            mCurrentLocationMarker = addMarker(latLng, true, true, R.drawable.current_location_marker, 0.5f, 0.5f );
    }


    public Marker addMarker(LatLng latLng) {
        return addMarker(latLng, false, false);
    }

    public Marker addMarker(LatLng latLng, boolean zoomTo) {
        return addMarker(latLng, zoomTo, false);
    }

    public Marker addMarker(LatLng latLng, boolean zoomTo, boolean moveTo) {
        return addMarker(latLng, zoomTo, moveTo, -1, 0, 0);
    }

    public Marker addMarker(LatLng latLng, boolean zoomTo, boolean moveTo, int resourceID, float anchorX, float anchorY) {
        MarkerOptions options = new MarkerOptions();
        options.position(latLng);
        if(anchorX != 0 && anchorY != 0)
            options.anchor(anchorX, anchorY);
        if(resourceID != -1)
            options.icon(BitmapDescriptorFactory.fromResource(resourceID));
        Marker marker =  mMap.addMarker(options);
        if(moveTo)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        if(zoomTo)
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        return marker;
    }

    @Override
    public void onCameraMove() {
        if(mAddRestroomMarker != null)
            mAddRestroomMarker.remove();
        mAddRestroomMarker = null;
    }
}

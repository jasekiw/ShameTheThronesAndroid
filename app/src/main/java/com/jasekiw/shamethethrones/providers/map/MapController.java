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
import com.jasekiw.shamethethrones.providers.map.util.MarkerAnimation;
import com.jasekiw.shamethethrones.providers.util.BitmapUtilities;

public class MapController implements GoogleMap.OnMapClickListener, GoogleMap.OnCameraMoveListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Marker mCurrentLocationMarker;
    private ManagedMarker mAddRestroomMarker;
    private Context mContext;
    private OnMapReadyCallback mReadyCallback;

    public MapController(ManagedMarker managedMarker) {
        mAddRestroomMarker = managedMarker;
    }

    public boolean isMapReady() {
        return mMap != null;
    }


    public void initialize(SupportMapFragment fragment, Context context) {
        mContext = context;
        fragment.getMapAsync(this);
    }

    public void setOnMapReady(OnMapReadyCallback callback) {
        mReadyCallback = callback;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("map", "handle map ready");
        mMap.setOnMapClickListener(this);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnMarkerClickListener(this);
        if(mReadyCallback != null)
            mReadyCallback.onMapReady(googleMap);

    }


    @Override
    public void onMapClick(LatLng latLng) {

//        createAddRestroomPopup(latLng);
    }

    private void createAddRestroomPopup(LatLng latLng) {
        Log.d("map", "onMapClick");
        if(mAddRestroomMarker.isMarkerNotNull()) {
            mAddRestroomMarker.getMarker().remove();
            mAddRestroomMarker.setMarker(null);
        }
        else {
            mAddRestroomMarker.setMarker(addMarker(latLng, false, false, R.drawable.marker, 0.5f, 1f, 1));
            new MarkerAnimation(new BitmapUtilities(mContext)).applyScaleEffect(mAddRestroomMarker, 244);
        }

    }


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
                options.icon(BitmapDescriptorFactory.fromBitmap(new BitmapUtilities(mContext).resize(resourceID, size,size)));
            else
                options.icon(BitmapDescriptorFactory.fromResource(resourceID));

        }

        Marker marker =  mMap.addMarker(options);
        if(moveTo)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        if(zoomTo)
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        return marker;
    }

    @Override
    public void onCameraMove() {
        if(mAddRestroomMarker.isMarkerNotNull())
            mAddRestroomMarker.getMarker().remove();
        mAddRestroomMarker.setMarker(null);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d("map", "onMarkerClick");
        if(marker == mAddRestroomMarker.getMarker())
        {
            Log.d("map", "Removing Marker");
            if(mAddRestroomMarker.isMarkerNotNull())
                mAddRestroomMarker.removeMarker();

        }
        return false;
    }
}

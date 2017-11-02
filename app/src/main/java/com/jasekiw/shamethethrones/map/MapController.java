package com.jasekiw.shamethethrones.map;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

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
    private ManagedMarker mAddRestroomMarker;
    private Context mContext;

    public MapController(GoogleMap map, Context context) {
        mMap = map;
        mContext = context;
        mAddRestroomMarker = new ManagedMarker();
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
        if(mAddRestroomMarker.isMarkerNotNull()) {
            mAddRestroomMarker.getMarker().remove();
            mAddRestroomMarker.setMarker(null);
        }
        else {
            mAddRestroomMarker.setMarker(addMarker(latLng, false, false, R.drawable.marker, 0.5f, 1f, 1));
            dropPinEffect(mAddRestroomMarker);
        }

    }


    public void changeCurrentLocation(LatLng latLng) {

        if(mCurrentLocationMarker != null)
            mCurrentLocationMarker.setPosition(latLng);
        else
            mCurrentLocationMarker = addMarker(latLng, true, true, R.drawable.current_location_marker, 0.5f, 0.5f, -1 );
    }



    public Bitmap resizeMapIcons(int resourceID, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(mContext.getResources(),resourceID);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    private void dropPinEffect(final ManagedMarker marker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 200;

        final Interpolator interpolator = new DecelerateInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                if(marker.getMarker() == null)
                    return;

                long elapsed = SystemClock.uptimeMillis() - start;
                float t = Math.max(
                        1 - interpolator.getInterpolation((float) elapsed
                                / duration), 0);
//                marker.setAnchor(0.5f, 1.0f + 2 * t);
                int width = (int) (244.0 - (50.0 * t));
                int height = (int) (244.0 - (50.0 * t));
                marker.getMarker().setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(R.drawable.marker, width, height)));
                Log.d("animation", "" + t);
                if (t > .1) {
                    // Post again 15ms later.
                    handler.postDelayed(this, 30);
                } else {
                    marker.getMarker().setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(R.drawable.marker, 244, 244)));
//                    marker.getMarker().showInfoWindow();

                }
            }
        });
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
                options.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(resourceID, size,size)));
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
}

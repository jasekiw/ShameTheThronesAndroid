package com.jasekiw.shamethethrones.gps;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Jason on 11/1/2017.
 */

public class LocationController {
    private Activity mContext;
    private LocationHandler mLocationHandler;
    public static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 334;
    LocationManager mLocationManager;
    MainLocationListener mLocationListener;

    public LocationController(Activity context, LocationHandler locationHandler) {
        mContext = context;
        mLocationHandler = locationHandler;
    }


    public void initializeLocation() {


        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            if (ActivityCompat.shouldShowRequestPermissionRationale(mContext,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                Log.d("location", "why you turn down request?");
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                Log.d("location", "requesting permission");
                ActivityCompat.requestPermissions(mContext,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

            Log.d("location", "permission not accepted");
            return;
        }
        else
        {
            Log.d("location", "permission accepted");
            activateLocation();
        }


    }

    private void activateLocation() {
        Log.d("location", "permission granted");
        // permission was granted, yay! do the
        // calendar task you need to do.

        mLocationManager = (LocationManager) this.mContext.getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new MainLocationListener(mLocationHandler);

        if ( !mLocationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }
        //noinspection MissingPermission Already requested it duhh!
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
        Log.d("location", "requesting updates");
        @SuppressWarnings("MissingPermission")
        Location lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(lastKnownLocation == null)
        {
            Log.d("location", "provider disabled!");
        }
        else
        {
            mLocationHandler.onLocationChanged(lastKnownLocation);
        }
    }


    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        mContext.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            activateLocation();
        } else {
            Log.d("location", "permission denied");
            // permission denied, boo! Disable the
            // functionality that depends on this permission.
        }
    }


}

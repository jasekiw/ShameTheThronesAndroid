package com.jasekiw.shamethethrones.providers.location;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;


public class LocationController {

    private Activity mActivity;
    private LocationHandler mLocationHandler;
    LocationManager mLocationManager;
    MainLocationListener mLocationListener;
    public static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 334;

    public LocationHandler getmLocationHandler() {
        return mLocationHandler;
    }

    public void setLocationHandler(LocationHandler locationHandler) {
        mLocationHandler = locationHandler;
    }

    public Activity getActivity() {
        return mActivity;
    }

    public void setActivity(Activity activity) {
        this.mActivity = activity;
    }


    public void initializeLocation() {

        mLocationManager = (LocationManager) this.mActivity.getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new MainLocationListener(mLocationHandler);

        if (ActivityCompat.checkSelfPermission(mActivity,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            activateLocation();
            return;
        }


        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.ACCESS_FINE_LOCATION ))
            builtAlertAppCannotFunction();
        else {
            ActivityCompat.requestPermissions(
                    mActivity,
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION
            );
        }



    }

    private void activateLocation() {
        Log.d("location", "permission granted");
        // permission was granted, yay! do the
        // calendar task you need to do.

        if ( !mLocationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) )
            buildAlertMessageNoGPS();

        //noinspection MissingPermission Already requested it duhh!
        if (ActivityCompat.checkSelfPermission(mActivity,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
            Log.d("location", "requesting updates");
            @SuppressWarnings("MissingPermission")
            Location lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastKnownLocation == null)
                Log.d("location", "provider disabled!");
            else
                mLocationHandler.onLocationChanged(lastKnownLocation);
        }

    }


    private void buildAlertMessage(String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> mActivity.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        builder.create().show();
    }

    private void buildAlertMessageNoGPS() {
        buildAlertMessage("Your GPS seems to be disabled, do you want to enable it?");
    }

    private void builtAlertAppCannotFunction() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage("Shame the thrones cannot work correctly without gps at this time. Do you want to enable it GPS?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> mActivity.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton("Quit App", (dialog, id) -> System.exit(1));
        builder.create().show();
    }


    private void buildAlertMessageAppDoesNotWorkWithoutGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage("Shame the thrones can not fully function without gps")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> mActivity.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        builder.create().show();
    }

    public void onRequestPermissionsResult(int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            activateLocation();
        } else {
            Log.d("location", "permission denied");
            // permission denied, boo! Disable the
            // functionality that depends on this permission.
        }
    }


}

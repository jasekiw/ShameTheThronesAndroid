package com.jasekiw.shamethethrones.providers.places;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

public class AndroidPlacesApi {

    private GoogleApiClient mGoogleApiClient;

    public AndroidPlacesApi(GoogleApiClient apiClient) {
        mGoogleApiClient = apiClient;
    }
    public void getPlaceById(FetchPlaceByIdTask.OnPlaceFoundListener onPlaceFoundListener, String placeId) {
        FetchPlaceByIdTask task = new FetchPlaceByIdTask(mGoogleApiClient, onPlaceFoundListener);
        task.execute(placeId);
    }

    public static GoogleApiClient getGoogleApiClient(FragmentActivity activity) {
        return new GoogleApiClient.Builder(activity)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(activity, connectionResult -> {
                    Log.d("Google Play Services", "Connection failure: " + connectionResult.getErrorMessage());
                })
                .build();
    }

}

package com.jasekiw.shamethethrones.providers.places;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.Places;

public class AndroidPlacesApi {

    private GoogleApiClient mGoogleApiClient;

    public interface onPhotoReceivedListener {
        void onPhotoReceived(Bitmap photo);
    }

    public AndroidPlacesApi(GoogleApiClient apiClient) {
        apiClient.connect();
        mGoogleApiClient = apiClient;
    }
    public void getPlaceById(FetchPlaceByIdTask.OnPlaceFoundListener onPlaceFoundListener, String placeId) {
        FetchPlaceByIdTask task = new FetchPlaceByIdTask(mGoogleApiClient, onPlaceFoundListener);
        task.execute(placeId);
    }

    public void getPlacePhotos(String placeId, onPhotoReceivedListener listener) {
        Places.GeoDataApi.getPlacePhotos(mGoogleApiClient, placeId).setResultCallback(placePhotoMetadataResult -> {
            if(placePhotoMetadataResult.getStatus().isSuccess())
            {
                if(placePhotoMetadataResult.getPhotoMetadata().getCount() > 0)
                    placePhotoMetadataResult.getPhotoMetadata().get(0).getPhoto(mGoogleApiClient).setResultCallback( placePhotoResult -> {
                        listener.onPhotoReceived(placePhotoResult.getBitmap());
                    });
                else
                    Log.d("Places", "No image found");

            }
            placePhotoMetadataResult.getPhotoMetadata().release();
        });
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

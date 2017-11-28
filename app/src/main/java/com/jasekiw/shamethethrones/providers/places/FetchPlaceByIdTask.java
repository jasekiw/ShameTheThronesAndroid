package com.jasekiw.shamethethrones.providers.places;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;


public class FetchPlaceByIdTask extends AsyncTask<String,Void, Place> {

    public interface OnPlaceFoundListener {
        void onPlaceFound(Place place);
    }

    private OnPlaceFoundListener mOnPlaceFoundListener;
    private GoogleApiClient mGoogleApiClient;
    public FetchPlaceByIdTask(GoogleApiClient client, OnPlaceFoundListener onPlaceFoundListener) {
        mOnPlaceFoundListener = onPlaceFoundListener;
        mGoogleApiClient = client;
    }

    @Override
    protected Place doInBackground(String... placeIds) {
        Log.d("Place", placeIds[0]);

        PlaceBuffer result = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeIds[0]).await();
        if(result.getStatus().isSuccess()) {
            Place place = result.get(0).freeze();
            result.release(); // release the buffer
            return place;
        }
        else
        {
            result.release(); // release the buffer
            return null;
        }

    }

    @Override
    protected void onPostExecute(Place place) {
        super.onPostExecute(place);
        mOnPlaceFoundListener.onPlaceFound(place);
    }
}

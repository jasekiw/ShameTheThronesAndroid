package com.jasekiw.shamethethrones.providers.places;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;

import com.google.maps.errors.ApiException;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;

import java.io.IOException;

public class WebSearchTask extends AsyncTask<LatLng, Void,PlacesSearchResult[]> {

    private OnPlacesSearchResult mOnPlacesSearchResult;
    private GeoApiContext mGeoApiContext;

    public WebSearchTask(OnPlacesSearchResult onPlacesSearchResult, GeoApiContext geoApiContext) {
        mOnPlacesSearchResult = onPlacesSearchResult;
        mGeoApiContext = geoApiContext;
    }

    @Override
    protected PlacesSearchResult[] doInBackground(LatLng... latLngs) {
        com.google.maps.model.LatLng newLatLng = new com.google.maps.model.LatLng(latLngs[0].latitude, latLngs[0].longitude);
        try {
            PlacesSearchResponse response = PlacesApi.nearbySearchQuery(mGeoApiContext, newLatLng).radius(50).await();
            return response.results;
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
    protected void onPostExecute(PlacesSearchResult[] results) {
        mOnPlacesSearchResult.onResult(results);
    }


}

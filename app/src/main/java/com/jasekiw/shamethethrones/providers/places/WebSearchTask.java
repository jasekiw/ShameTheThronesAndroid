package com.jasekiw.shamethethrones.providers.places;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;

import com.google.maps.errors.ApiException;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;

import java.io.IOException;

public class WebSearchTask extends AsyncTask<PlacesWebApiSearch.SearchParams, Void,PlacesSearchResult[]> {

    private OnPlacesSearchResult mOnPlacesSearchResult;
    private GeoApiContext mGeoApiContext;

    public WebSearchTask(OnPlacesSearchResult onPlacesSearchResult, GeoApiContext geoApiContext) {
        mOnPlacesSearchResult = onPlacesSearchResult;
        mGeoApiContext = geoApiContext;
    }

    @Override
    protected PlacesSearchResult[] doInBackground(PlacesWebApiSearch.SearchParams... params) {
        PlacesWebApiSearch.SearchParams searchParams = params[0];
        com.google.maps.model.LatLng newLatLng = new com.google.maps.model.LatLng(searchParams.latLng.latitude, searchParams.latLng.longitude);
        try {
            PlacesSearchResponse response = PlacesApi.nearbySearchQuery(mGeoApiContext, newLatLng).radius(searchParams.radius).await();
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

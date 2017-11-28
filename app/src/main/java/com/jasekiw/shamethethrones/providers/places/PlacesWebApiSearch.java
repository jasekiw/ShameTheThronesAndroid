package com.jasekiw.shamethethrones.providers.places;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.android.volley.RequestQueue;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.GeoApiContext;
import com.google.maps.model.PlacesSearchResult;
import com.jasekiw.shamethethrones.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlacesWebApiSearch {

    RequestQueue mRequestQueue;
    Context mContext;

    GeoApiContext mGeoApiContext;
    public static class SearchParams {

        public LatLng latLng;
        public int radius;

        public SearchParams(LatLng latLng, int radius) {
            this.latLng = latLng;
            this.radius = radius;
        }

    }

    public PlacesWebApiSearch(Context context) {
        mContext = context;
        String key = mContext.getResources().getString(R.string.google_maps_key);
        mGeoApiContext = new GeoApiContext.Builder()
                .apiKey(key )
                .build();
    }

    public void getPlaceIdFromLatLng(Context context, SearchParams searchParams, OnChosenResult onResult) {
        Log.d("places", "request");
        Log.d("places", "Thread: " + android.os.Process.myTid());
        WebSearchTask task = new WebSearchTask(inputResults -> {
            PlacesSearchResult[] results = filterResults(inputResults);
            String[] resultNames = new String[results.length];
            Log.d("places", "Result Thread: " + android.os.Process.myTid());

            for(int i = 0; i < resultNames.length; i++)
                resultNames[i] = results[i].name;

            for (PlacesSearchResult placeResult : results) {
                Log.d("places", "result: name: " +  placeResult.name + ", address: " + Arrays.toString(placeResult.types));

            }
            buildAlertDialog(resultNames, context, (dialogInterface, i) -> {
                onResult.onSearchResult(results[i]);
            });
        }, mGeoApiContext);
        task.execute(searchParams);
    }

    protected PlacesSearchResult[] filterResults(PlacesSearchResult[] inputResults) {
        ArrayList<PlacesSearchResult> results = new ArrayList<>();
        for (PlacesSearchResult result : inputResults) {
            List<String> types = Arrays.asList(result.types);
            if(!types.contains("locality") && !types.contains("route") )
                results.add(result);
        }
        PlacesSearchResult[] outputArray = new PlacesSearchResult[results.size()];
        outputArray = results.toArray(outputArray);
        return outputArray;
    }

    protected void buildAlertDialog(String[] results, Context context,  DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context, R.style.DialogTheme);
        builderSingle.setTitle("Which Place do you want to add a restroom for?");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_item_text_view);
        for (String result : results)
            arrayAdapter.add(result);
        builderSingle.setNegativeButton("close", (dialog, which) -> dialog.dismiss());
        builderSingle.setAdapter(arrayAdapter, listener);
        AlertDialog dialog = builderSingle.create();
        dialog.setOnShowListener( dialogInterface -> dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE));
        dialog.show();
    }

}

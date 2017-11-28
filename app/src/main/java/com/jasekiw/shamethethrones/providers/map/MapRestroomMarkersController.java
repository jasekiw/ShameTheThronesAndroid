package com.jasekiw.shamethethrones.providers.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.jasekiw.shamethethrones.R;
import com.jasekiw.shamethethrones.providers.api.RestroomApi;
import com.jasekiw.shamethethrones.models.restroom.RestroomModel;
import com.jasekiw.shamethethrones.providers.util.DialogUtility;



public class MapRestroomMarkersController {

    public interface OnRestroomClickListener {
        void onRestroomClick(RestroomModel restroom);
    }
    private Map<Marker, RestroomModel> mMarkerRestroomModelMap = new HashMap<>();
    private List<Marker> mMarkers = new ArrayList<>();
    private RestroomApi mRestroomApi;
    private GoogleMap mMap;
    private Context mContext;

    /**
     * Initializes the controller with a google map and a context
     * @param map
     * @param context
     */
    public void intialize(GoogleMap map, Context context, OnRestroomClickListener clickListener) {
        mMap = map;
        mContext = context;
        mMap.setOnMarkerClickListener(marker -> {
            if(!mMarkers.contains(marker))
                return false;
            clickListener.onRestroomClick(mMarkerRestroomModelMap.get(marker));
            return true;
        });
    }

    public MapRestroomMarkersController(RestroomApi api) {
        mRestroomApi = api;
    }

    /**
     * Loads and replaces restrooms on the map
     */
    public void refreshRestrooms() {
        mRestroomApi.getRestrooms(mMap.getProjection().getVisibleRegion().latLngBounds, restrooms -> {
            removeRestroomMarkers();
            for (RestroomModel restroom : restrooms)
                addRestroomMarker(restroom);
        }, error -> DialogUtility.showErrorDialog(mContext, "Could not retrieve restrooms"));
    }

    private void addRestroomMarker(RestroomModel model) {
        MarkerOptions options = new MarkerOptions();
        options.position(model.getLatLng());
        Bitmap imageBitmap = BitmapFactory.decodeResource(mContext.getResources(), getResourceImageIdForRating(model.getRating()));
        options.icon(BitmapDescriptorFactory.fromBitmap(imageBitmap));
        options.anchor(0.5f, 1f);
        Marker marker = mMap.addMarker(options);

        mMarkers.add(marker);
        mMarkerRestroomModelMap.put(marker, model);
    }

    private void removeRestroomMarkers() {
        Log.d("MarkersController",  "removeRestroomMarkers");
        List<Marker> markers = mMarkers;
        mMarkers = new ArrayList<>();
        for (Marker marker : markers) {
            if(mMarkerRestroomModelMap.containsKey(marker))
                mMarkerRestroomModelMap.remove(marker);
            marker.remove();
        }
    }


    private int getResourceImageIdForRating(double rating) {
        if(rating == 0)
            return R.drawable.restroom_marker;
        int intRating = (int)Math.round(rating);
        if(intRating == 0)
            intRating = 1;
        if(intRating > 5)
            intRating = 5;

        switch (intRating) {
            case 1: return R.drawable.marker_1_star;
            case 2: return R.drawable.marker_2_star;
            case 3: return R.drawable.marker_3_star;
            case 4: return R.drawable.marker_4_star;
            case 5: return R.drawable.marker_5_star;
            default: return R.drawable.restroom_marker;
        }

    }

}

package com.jasekiw.shamethethrones.providers.map;

import com.google.android.gms.maps.model.LatLng;

public interface OnNewRestroomHandler {
    void handle(LatLng latLng);
}

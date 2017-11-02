package com.jasekiw.shamethethrones.map;

import com.google.android.gms.maps.model.Marker;

/**
 * A managed marker is needed to handle null references when passing a final parameter.
 * When a marker is removed from a map The marker should be set to null so the rest of the system
 * knows not to use it.
 */
public class ManagedMarker {

    private Marker mMarker;

    public Marker getMarker() {
        return mMarker;
    }

    public void setMarker(Marker mMarker) {
        this.mMarker = mMarker;
    }

    public boolean isMarkerNull() {
        return mMarker == null;
    }
    public boolean isMarkerNotNull() {
        return mMarker != null;
    }

}

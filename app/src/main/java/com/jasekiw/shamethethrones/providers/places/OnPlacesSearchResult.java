package com.jasekiw.shamethethrones.providers.places;

import com.google.maps.model.PlacesSearchResult;

public interface OnPlacesSearchResult {
    void onResult(PlacesSearchResult[] results);
}

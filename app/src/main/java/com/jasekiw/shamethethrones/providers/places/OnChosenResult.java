package com.jasekiw.shamethethrones.providers.places;

import com.google.maps.model.PlacesSearchResult;

public interface OnChosenResult {
    void onSearchResult(PlacesSearchResult result);
}

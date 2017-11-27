package com.jasekiw.shamethethrones.activites;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.GeoDataApi;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.jasekiw.shamethethrones.R;
import com.jasekiw.shamethethrones.providers.places.AndroidPlacesApi;
import com.jasekiw.shamethethrones.providers.restroom.OnAddRestroomCancelledListener;


public class AddRestroomFragment extends Fragment{

    private static final String ARG_PLACE_ID = "placeId";
    private static final String ARG_LAT_LNG = "latLng";


    private String placeId;
    private LatLng latLng;


    private OnAddRestroomCancelledListener mOnAddRestroomCancelled;

    private AndroidPlacesApi mAndroidPlacesApi;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
     * @return A new instance of fragment AddRestroomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddRestroomFragment newInstance(String placeId, LatLng latLng, AndroidPlacesApi androidPlacesApi) {
        AddRestroomFragment fragment = new AddRestroomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PLACE_ID, placeId);
        args.putParcelable(ARG_LAT_LNG, latLng);
        fragment.setAndroidPlacesApi(androidPlacesApi);
        fragment.setArguments(args);
        return fragment;
    }

    public void setAndroidPlacesApi(AndroidPlacesApi androidPlacesApi) {
        mAndroidPlacesApi = androidPlacesApi;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            placeId = getArguments().getString(ARG_PLACE_ID);
            latLng = getArguments().getParcelable(ARG_LAT_LNG);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_restroom, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(mAndroidPlacesApi != null)
            mAndroidPlacesApi.getPlaceById(place -> {
                if(place != null)
                    Log.d("AddRestroomFragment", "place: " + place.getName());
                else
                    Log.d("AddRestroomFragment", "place: null");
            }, placeId);
        getView().findViewById(R.id.add_restroom_btn_cancel).setOnClickListener( view1 -> {
            if(mOnAddRestroomCancelled != null)
                mOnAddRestroomCancelled.onAddRestroomCancelled();
        });
    }


    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnAddRestroomCancelledListener) {
            mOnAddRestroomCancelled = (OnAddRestroomCancelledListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAddRestroomCancelledListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnAddRestroomCancelled = null;
    }

}

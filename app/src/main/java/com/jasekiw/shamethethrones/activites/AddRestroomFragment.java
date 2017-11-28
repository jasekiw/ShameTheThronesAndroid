package com.jasekiw.shamethethrones.activites;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jasekiw.shamethethrones.R;
import com.jasekiw.shamethethrones.STTApp;
import com.jasekiw.shamethethrones.providers.places.AndroidPlacesApi;
import com.jasekiw.shamethethrones.providers.restroom.AddRestroomFragmentController;
import com.jasekiw.shamethethrones.models.restroom.AddRestroomModel;

import javax.inject.Inject;

public class AddRestroomFragment extends Fragment{

    private static final String ARG_MODEL = "MODEL";
    private static final String ARG_LAT_LNG = "latLng";


    public interface HasAndroidPlacesApi {
        AndroidPlacesApi getAndroidPlacesApi();
    }
    @Inject
    public AddRestroomFragmentController mAddRestroomController;

    /**
     * Creates a new instance of the fragment given the following parameters
     * @param model
     * @return
     */
    public static AddRestroomFragment newInstance(AddRestroomModel model) {
        AddRestroomFragment fragment = new AddRestroomFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_MODEL, model);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAddRestroomController.setModel(getArguments().getParcelable(ARG_MODEL));
        }
        if(savedInstanceState != null)
            mAddRestroomController.restore(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_restroom, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("Fragment", "onViewCreated");
        mAddRestroomController.bind(
                view.findViewById(R.id.add_restroom_title),
                view.findViewById(R.id.add_restroom_image),
                view.findViewById(R.id.add_restroom_rb_male),
                view.findViewById(R.id.add_restroom_rb_female),
                view.findViewById(R.id.add_restroom_rb_both),
                view.findViewById(R.id.add_restroom_gender_rb_group),
                view.findViewById(R.id.add_restroom_btn_cancel),
                view.findViewById(R.id.add_restroom_btn_add)
        );
        mAddRestroomController.updateView();
    }

    /**
     * Save current state
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mAddRestroomController.save(outState);
    }


    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        Log.d("Fragment", "onAttach");
        ((STTApp)context.getApplication()).getAppComponent().inject(this);
        if (context instanceof AddRestroomFragmentController.OnAddRestroomCancelledListener)
            mAddRestroomController.setOnAddRestroomCancelledListener((AddRestroomFragmentController.OnAddRestroomCancelledListener) context);
         else
            throw new RuntimeException(context.toString() + " must implement OnAddRestroomCancelledListener");

        if (context instanceof AddRestroomFragmentController.OnRestroomAddedListener)
            mAddRestroomController.setOnAddRestroomAddedListener((AddRestroomFragmentController.OnRestroomAddedListener) context);
        else
            throw new RuntimeException(context.toString() + " must implement OnAddRestroomCancelledListener");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mAddRestroomController.removeOnAddRestroomCancelledListener();
    }

}

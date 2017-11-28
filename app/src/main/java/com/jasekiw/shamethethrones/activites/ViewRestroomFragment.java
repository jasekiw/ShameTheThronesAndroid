package com.jasekiw.shamethethrones.activites;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jasekiw.shamethethrones.R;
import com.jasekiw.shamethethrones.STTApp;
import com.jasekiw.shamethethrones.models.restroom.RestroomModel;
import com.jasekiw.shamethethrones.providers.restroom.RestroomFragmentController;

import javax.inject.Inject;

public class ViewRestroomFragment extends Fragment{

    private static final String ARG_MODEL = "RESTROOM_MODEL";



    @Inject
    public RestroomFragmentController mRestroomFragmentController;

    /**
     * Creates a new instance of the fragment given the following parameters
     * @param model
     * @return
     */
    public static ViewRestroomFragment newInstance(RestroomModel model) {
        ViewRestroomFragment fragment = new ViewRestroomFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_MODEL, model);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRestroomFragmentController.setModel(getArguments().getParcelable(ARG_MODEL));
        }
        if(savedInstanceState != null)
            mRestroomFragmentController.restore(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_restroom, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("Fragment", "onViewCreated");
        mRestroomFragmentController.bind(
                view.findViewById(R.id.view_restroom_title),
                view.findViewById(R.id.view_restroom_rating_value),
                view.findViewById(R.id.view_restroom_gender_value),
                view.findViewById(R.id.view_restroom_image),
                view.findViewById(R.id.view_restroom_btn_back),
                view.findViewById(R.id.view_restroom_btn_navigate),
                view.findViewById(R.id.view_restroom_btn_add_rating),
                view.findViewById(R.id.view_restroom_ratings_container)
        );
        mRestroomFragmentController.updateView();
    }

    /**
     * Save current state
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mRestroomFragmentController.save(outState);
    }


    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        Log.d("Fragment", "onAttach");
        ((STTApp)context.getApplication()).getAppComponent().inject(this);
        if (context instanceof RestroomFragmentController.OnRestroomCancelledListener)
            mRestroomFragmentController.setOnRestroomCancelledListener((RestroomFragmentController.OnRestroomCancelledListener) context);
         else
            throw new RuntimeException(context.toString() + " must implement OnAddRestroomCancelledListener");


        if (context instanceof RestroomFragmentController.OnRestroomChangeListener)
            mRestroomFragmentController.setOnRestroomChangeListener((RestroomFragmentController.OnRestroomChangeListener) context);
        else
            throw new RuntimeException(context.toString() + " must implement OnAddRestroomCancelledListener");
        mRestroomFragmentController.setLayoutInflater(context.getLayoutInflater());

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mRestroomFragmentController.removeOnAddRestroomCancelledListener();
    }

}

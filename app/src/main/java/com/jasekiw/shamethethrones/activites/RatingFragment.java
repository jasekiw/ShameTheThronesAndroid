package com.jasekiw.shamethethrones.activites;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jasekiw.shamethethrones.R;
import com.jasekiw.shamethethrones.models.RatingModel;


public class RatingFragment extends Fragment {

    private static final String ARG_RATING_MODEL = "RATING_MODEL";

    private RatingModel mRatingModel;


    public RatingFragment() {
        // Required empty public constructor
    }


    public static RatingFragment newInstance(RatingModel ratingModel) {
        RatingFragment fragment = new RatingFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_RATING_MODEL, ratingModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRatingModel = getArguments().getParcelable(ARG_RATING_MODEL);
        }
        if(savedInstanceState != null)
            mRatingModel = savedInstanceState.getParcelable(ARG_RATING_MODEL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rating, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvRating = view.findViewById(R.id.fragment_rating_tv_rating);
        TextView tvComment = view.findViewById(R.id.fragment_rating_tv_comment);
        tvRating.setText(String.valueOf(mRatingModel.getValue()));
        tvComment.setText(String.valueOf(mRatingModel.getContent()));

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_RATING_MODEL, mRatingModel);
    }

}

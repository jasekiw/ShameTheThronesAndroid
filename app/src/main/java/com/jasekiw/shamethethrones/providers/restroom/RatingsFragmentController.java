package com.jasekiw.shamethethrones.providers.restroom;


import android.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jasekiw.shamethethrones.R;
import com.jasekiw.shamethethrones.activites.RatingFragment;
import com.jasekiw.shamethethrones.activites.ViewRestroomFragment;
import com.jasekiw.shamethethrones.models.RatingModel;
import com.jasekiw.shamethethrones.models.restroom.PlaceInformation;
import com.jasekiw.shamethethrones.providers.api.RatingApi;

public class RatingsFragmentController {
    private static String RATING_TAG = "RATING_FRAGMENT_";
    private int mTotalRatings = 0;
    private LinearLayout mContainer;
    private LayoutInflater mLayoutInflater;
    private RatingApi mRatingApi;
    private int mRestroomId;

    RatingsFragmentController(RatingApi ratingApi) {
        mRatingApi = ratingApi;
    }

    public void bind(LinearLayout container) {
        mContainer = container;
    }

    public void setRestroomId(int id) {
        mRestroomId = id;
    }


    public void setLayoutInflater(LayoutInflater layoutInflater) {
        mLayoutInflater = layoutInflater;
    }
    public void buildView() {
        addRatings();
    }

    private void addRatings() {
        mRatingApi.getRatings(mRestroomId, ratings -> {
            for (RatingModel rating : ratings) {
                addRating(rating);
            }
        }, error -> {
            Log.d("RatingsController", "Failed to get ratings");
        });
    }


    public void addRating(RatingModel rating) {
        View child =  mLayoutInflater.inflate(R.layout.fragment_rating, null);
        TextView tvRating = child.findViewById(R.id.fragment_rating_tv_rating);
        TextView tvComment = child.findViewById(R.id.fragment_rating_tv_comment);
        tvRating.setText(String.valueOf(rating.getValue()));
        tvComment.setText(String.valueOf(rating.getContent()));
        mContainer.addView(child);
    }
}

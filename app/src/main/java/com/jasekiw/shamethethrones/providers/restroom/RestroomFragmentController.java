package com.jasekiw.shamethethrones.providers.restroom;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jasekiw.shamethethrones.R;
import com.jasekiw.shamethethrones.models.NewRatingResponseModel;
import com.jasekiw.shamethethrones.models.RatingModel;
import com.jasekiw.shamethethrones.providers.api.RatingApi;
import com.jasekiw.shamethethrones.models.restroom.RestroomModel;
import com.jasekiw.shamethethrones.providers.util.DialogUtility;


public class RestroomFragmentController {

    public static interface OnRestroomCancelledListener {
        void onRestroomCancelled();
    }
    private RatingsFragmentController mRatingsFragmentController;
    private OnRestroomCancelledListener mOnAddRestroomCancelledListener;
    private TextView mTvTitle;
    private ImageView mIvPhoto;
    private RestroomModel mModel;
    private RatingApi mRatingApi;
    private TextView mTvRatingValue;
    private TextView mTvGenderValue;


    public RestroomFragmentController(RatingApi api, RatingsFragmentController ratingsFragmentController) {
        mRatingApi = api;
        mRatingsFragmentController = ratingsFragmentController;
    }

    public void setModel(RestroomModel model) {
        mRatingsFragmentController.setRestroomId(model.getId());
        mModel = model;
    }

    public void cancel() {
        if(mOnAddRestroomCancelledListener != null)
            mOnAddRestroomCancelledListener.onRestroomCancelled();
    }

    public void setOnRestroomCancelledListener(OnRestroomCancelledListener listener) {
        mOnAddRestroomCancelledListener = listener;
    }
    public void removeOnAddRestroomCancelledListener() {
        mOnAddRestroomCancelledListener = null;
    }

    public void updateView() {
        mTvTitle.setText(mModel.getPlaceInformation().getPlaceName());
        mIvPhoto.setImageBitmap(mModel.getPlaceInformation().getPhoto());
        mTvGenderValue.setText(mModel.getGender().name());
        mTvRatingValue.setText(String.valueOf(mModel.getRating()));
    }

    public void setLayoutInflater(LayoutInflater layoutInflater) {
        mRatingsFragmentController.setLayoutInflater(layoutInflater);
    }


    public void bind(
            TextView tvTitle,
            TextView tvRatingValue,
            TextView tvGenderValue,
            ImageView ivPhoto,
            Button bvCancel,
            Button bvNavigate,
            Button bvAddRating,
            LinearLayout ratingsContainer
           ) {
        mRatingsFragmentController.bind(ratingsContainer);
        mTvTitle = tvTitle;
        mIvPhoto = ivPhoto;
        mTvRatingValue = tvRatingValue;
        mTvGenderValue = tvGenderValue;
        bvCancel.setOnClickListener( v -> cancel());
        bvNavigate.setOnClickListener(v -> {
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + mModel.getLatLng().latitude + "," + mModel.getLatLng().longitude);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            v.getContext().startActivity(mapIntent);
        });
        bvAddRating.setOnClickListener(v -> showRatingDialog(v.getContext()));
        mRatingsFragmentController.buildView();
    }

    public void save(Bundle outState) {
        outState.putParcelable("MODEL", mModel);
    }


    public void restore(Bundle saveInstanceBundle) {
        mModel = saveInstanceBundle.getParcelable("MODEL");
    }

    protected void showRatingDialog(Context context) {
        final Dialog dialog = new Dialog(context,R.style.DialogTheme);
        dialog.setContentView(R.layout.dialog_add_rating);
        dialog.setTitle("Add a rating");

        // set the custom dialog components - text, image and button
        EditText etComment = dialog.findViewById(R.id.dialog_add_rating_et_comment);
        SeekBar ratingSeekBar = dialog.findViewById(R.id.dialog_add_rating_sb_rating);


        Button dialogButton = dialog.findViewById(R.id.dialog_add_rating_bv_add_rating);
        Button cancelButton = dialog.findViewById(R.id.dialog_add_rating_bv_cancel);
        // if button is clicked, close the custom dialog
        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialogButton.setOnClickListener(v -> {
            RatingModel model = new RatingModel();
            model.setContent(etComment.getText().toString());
            model.setValue(ratingSeekBar.getProgress());
            model.setRestroomId(mModel.getId());
            mRatingApi.addRating(model,
                    newRatingResponseModel -> {
                        dialog.dismiss();
                        onNewRating(newRatingResponseModel);
                    },
                    error -> DialogUtility.showErrorDialog(context, "Please fill in all the values!")
            );
        });
        dialog.show();
    }

    public void onNewRating(NewRatingResponseModel newRatingResponseModel) {
        mModel.setRating(newRatingResponseModel.getNewAverage());
        mRatingsFragmentController.addRating(newRatingResponseModel.getRating());
        updateView();
    }


}

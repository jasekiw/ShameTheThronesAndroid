package com.jasekiw.shamethethrones.providers.restroom;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jasekiw.shamethethrones.models.restroom.AddRestroomModel;
import com.jasekiw.shamethethrones.models.restroom.RestroomModel;
import com.jasekiw.shamethethrones.providers.api.RestroomApi;


public class AddRestroomFragmentController {

    public static interface OnAddRestroomCancelledListener {
        void onAddRestroomCancelled();
    }

    public static interface OnRestroomAddedListener {
        void onRestroomAdded(RestroomModel restroomModel);
    }

    private OnAddRestroomCancelledListener mOnAddRestroomCancelledListener;
    private OnRestroomAddedListener mOnRestroomAddedListener;
    private TextView mTvTitle;
    private ImageView mIvPhoto;
    private AddRestroomModel mModel;
    private RadioButton mRbMale;
    private RadioButton mRbFemale;
    private RadioButton mRbBoth;
    private RadioGroup mRbgGender;
    private RestroomApi mRestroomApi;

    public AddRestroomFragmentController(RestroomApi api) {
        mRestroomApi = api;
    }
    public void setModel(AddRestroomModel mModel) {
        this.mModel = mModel;
    }

    public void cancel() {
        if(mOnAddRestroomCancelledListener != null)
            mOnAddRestroomCancelledListener.onAddRestroomCancelled();
    }

    public void setOnAddRestroomCancelledListener(OnAddRestroomCancelledListener listener) {
        mOnAddRestroomCancelledListener = listener;
    }

    public void setOnAddRestroomAddedListener(OnRestroomAddedListener listener) {
        mOnRestroomAddedListener = listener;
    }
    public void removeOnAddRestroomCancelledListener() {
        mOnAddRestroomCancelledListener = null;
    }

    public void updateView() {
        mTvTitle.setText(mModel.getPlaceName());
        mIvPhoto.setImageBitmap(mModel.getPhoto());
        mRbgGender.clearCheck();
        switch (mModel.getGender())
        {
            case FEMALE : mRbFemale.setChecked(true);
            case MALE : mRbMale.setChecked(true);
            case BOTH : mRbBoth.setChecked(true);
        }
    }



    public void bind(
            TextView tvTitle,
            ImageView ivPhoto,
            RadioButton rbMale,
            RadioButton rbFemale,
            RadioButton rbBoth,
            RadioGroup rbgGender,
            Button bvCancel,
            Button bvAddRestroom) {

        mTvTitle = tvTitle;
        mIvPhoto = ivPhoto;

        mRbMale = rbMale;
        mRbFemale = rbFemale;
        mRbBoth = rbBoth;
        mRbgGender = rbgGender;
        mRbMale.setOnClickListener(v -> mModel.setGender(RestroomGender.MALE));
        mRbFemale.setOnClickListener(v -> mModel.setGender(RestroomGender.FEMALE));
        mRbBoth.setOnClickListener(v -> mModel.setGender(RestroomGender.BOTH));
        bvCancel.setOnClickListener( v -> cancel());
        bvAddRestroom.setOnClickListener(this::addRestroom);
    }

    public void save(Bundle outState) {
        outState.putParcelable("MODEL", mModel);
    }


    public void restore(Bundle saveInstanceBundle) {
        mModel = saveInstanceBundle.getParcelable("MODEL");
    }

    public void addRestroom(View v) {
       mRestroomApi.addRestroom(mModel, model -> {
        if(mOnRestroomAddedListener != null)
            mOnRestroomAddedListener.onRestroomAdded(model);
       }, error -> showErrorDialog(v.getContext(), error));
    }



    protected void showErrorDialog(Context context, String message) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Please fill out all the fields before adding the restroom. Error received: " + message);
        builderSingle.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
        builderSingle.show();
    }


}

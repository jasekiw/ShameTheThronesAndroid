package com.jasekiw.shamethethrones.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class NewRatingResponseModel implements Parcelable {

    private RatingModel mRating;
    private double mNewAverage;

    public NewRatingResponseModel() {}
    protected NewRatingResponseModel(Parcel in) {
        mRating = in.readParcelable(RatingModel.class.getClassLoader());
        mNewAverage = in.readDouble();
    }

    public static final Creator<NewRatingResponseModel> CREATOR = new Creator<NewRatingResponseModel>() {
        @Override
        public NewRatingResponseModel createFromParcel(Parcel in) {
            return new NewRatingResponseModel(in);
        }

        @Override
        public NewRatingResponseModel[] newArray(int size) {
            return new NewRatingResponseModel[size];
        }
    };

    public RatingModel getRating() {
        return mRating;
    }

    public double getNewAverage() {
        return mNewAverage;
    }


    public static NewRatingResponseModel fromJson(JSONObject obj) {

        try {
            NewRatingResponseModel model = new NewRatingResponseModel();
            model.mRating = RatingModel.fromJson(obj.getJSONObject("rating"));
            model.mNewAverage = obj.getDouble("newAverageRating");
            return model;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(mRating, i);
        parcel.writeDouble(mNewAverage);
    }
}

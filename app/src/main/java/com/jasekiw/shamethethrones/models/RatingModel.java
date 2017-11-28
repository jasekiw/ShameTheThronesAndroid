package com.jasekiw.shamethethrones.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class RatingModel implements Parcelable {



    private int mRestroomId;
    private double mValue;
    private String mContent;

    public RatingModel() { }

    protected RatingModel(Parcel in) {
        mValue = in.readDouble();
        mContent = in.readString();
        mRestroomId = in.readInt();
    }

    public static final Creator<RatingModel> CREATOR = new Creator<RatingModel>() {
        @Override
        public RatingModel createFromParcel(Parcel in) {
            return new RatingModel(in);
        }

        @Override
        public RatingModel[] newArray(int size) {
            return new RatingModel[size];
        }
    };

    public static RatingModel fromJson(JSONObject obj) {

        try {
            RatingModel model = new RatingModel();
            model.mContent = obj.getString("content");
            model.mValue = obj.getDouble("value");
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
        parcel.writeDouble(mValue);
        parcel.writeString(mContent);
        parcel.writeInt(mRestroomId);
    }

    public double getValue() {
        return mValue;
    }

    public void setValue(double mValue) {
        this.mValue = mValue;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String mContent) {
        this.mContent = mContent;
    }

    public int getRestroomId() {
        return mRestroomId;
    }

    public void setRestroomId(int mRestroomId) {
        this.mRestroomId = mRestroomId;
    }

    public JSONObject toJson() {

        try {
            JSONObject obj = new JSONObject();
            obj.put("value", mValue);
            obj.put("content", mContent);
            obj.put("restroomId", mRestroomId);
            return obj;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

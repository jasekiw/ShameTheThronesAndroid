package com.jasekiw.shamethethrones.models.restroom;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.maps.model.LatLng;
import org.json.JSONException;
import org.json.JSONObject;

import com.jasekiw.shamethethrones.providers.restroom.RestroomGender;

public class RestroomModel implements Parcelable {

    private int mId;
    private LatLng mLatLng;
    private double mRating;
    private String mPlaceId;
    private RestroomGender mGender = RestroomGender.BOTH;
    // this information must be loaded in. It is not provided by the backend server
    private PlaceInformation mPlaceInformation;

    public RestroomModel() { }

    protected RestroomModel(Parcel in) {
        mLatLng = in.readParcelable(LatLng.class.getClassLoader());
        mRating = in.readDouble();
        mPlaceId = in.readString();
        mPlaceInformation = in.readParcelable(PlaceInformation.class.getClassLoader());
        mId = in.readInt();
    }

    public static final Creator<RestroomModel> CREATOR = new Creator<RestroomModel>() {
        @Override
        public RestroomModel createFromParcel(Parcel in) {
            return new RestroomModel(in);
        }

        @Override
        public RestroomModel[] newArray(int size) {
            return new RestroomModel[size];
        }
    };

    public static RestroomModel fromJson(JSONObject obj) {
        RestroomModel model = new RestroomModel();
        try {
            double lat = obj.getDouble("lat");
            double lng = obj.getDouble("lng");
            model.mLatLng = new LatLng(lat, lng);
            model.mRating = obj.getDouble("rating");
            model.mPlaceId = obj.getString("placeId");
            model.mGender = RestroomGender.values()[obj.getInt("gender")];
            model.mId = obj.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return model;
    }

    public LatLng getLatLng() {
        return mLatLng;
    }

    public double getRating() {
        return mRating;
    }

    public String getPlaceId() {
        return mPlaceId;
    }

    public RestroomGender getGender() {
        return mGender;
    }

    public PlaceInformation getPlaceInformation() {
        return mPlaceInformation;
    }

    public int getId() {
        return mId;
    }
    public void setRating(double mRating) {
        this.mRating = mRating;
    }

    public void setPlaceInformation(PlaceInformation mPlaceInformation) {
        this.mPlaceInformation = mPlaceInformation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(mLatLng, i);
        parcel.writeDouble(mRating);
        parcel.writeString(mPlaceId);
        parcel.writeParcelable(mPlaceInformation, i);
        parcel.writeInt(mId);
    }
}

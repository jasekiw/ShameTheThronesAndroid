package com.jasekiw.shamethethrones.models.restroom;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.maps.model.LatLng;
import com.jasekiw.shamethethrones.providers.restroom.RestroomGender;

import org.json.JSONException;
import org.json.JSONObject;


public class AddRestroomModel implements Parcelable {

    private String mPlaceName;
    private Bitmap mPhoto;
    private String mPlaceId;
    private LatLng mLatLng;
    private RestroomGender mGender = RestroomGender.BOTH;


    public AddRestroomModel(String placeName, Bitmap photo, String placeId,  LatLng latLng) {
        mPlaceName = placeName;
        mPhoto = photo;
        mPlaceId = placeId;
        mLatLng = latLng;
    }

    public AddRestroomModel() {}

    public static final Creator<AddRestroomModel> CREATOR = new Creator<AddRestroomModel>() {
        @Override
        public AddRestroomModel createFromParcel(Parcel in) {
            return new AddRestroomModel(in);
        }

        @Override
        public AddRestroomModel[] newArray(int size) {
            return new AddRestroomModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    protected AddRestroomModel(Parcel in) {
        mPlaceName = in.readString();
        mPlaceId = in.readString();
        mPhoto = in.readParcelable(Bitmap.class.getClassLoader());
        mLatLng = in.readParcelable(LatLng.class.getClassLoader());
        mGender = RestroomGender.valueOf(in.readString());
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mPlaceName);
        parcel.writeString(mPlaceId);
        parcel.writeParcelable(mPhoto, i);
        parcel.writeParcelable(mLatLng, i);
        parcel.writeString(mGender.name());
    }

    public String getPlaceName() {
        return mPlaceName;
    }

    public void setPlaceName(String mPlaceName) {
        this.mPlaceName = mPlaceName;
    }

    public Bitmap getPhoto() {
        return mPhoto;
    }

    public void setPhoto(Bitmap mPhoto) {
        this.mPhoto = mPhoto;
    }

    public String getPlaceId() {
        return mPlaceId;
    }

    public void setPlaceId(String mPlaceId) {
        this.mPlaceId = mPlaceId;
    }

    public LatLng getLatLng() {
        return mLatLng;
    }

    public void setLatLng(LatLng mLatLng) {
        this.mLatLng = mLatLng;
    }

    public RestroomGender getGender() {
        return mGender;
    }

    public void setGender(RestroomGender mGender) {
        this.mGender = mGender;
    }

   public JSONObject toJson() {
        JSONObject obj = new JSONObject();
       try {
           obj.put("lat", mLatLng.latitude);
           obj.put("lng", mLatLng.longitude);
           obj.put("gender", mGender.getValue());
           obj.put("placeId", mPlaceId);
           return obj;
       } catch (JSONException e) {
           e.printStackTrace();
       }
       return null;
   }
}

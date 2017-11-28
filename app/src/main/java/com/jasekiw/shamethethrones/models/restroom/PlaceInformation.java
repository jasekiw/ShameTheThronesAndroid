package com.jasekiw.shamethethrones.models.restroom;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class PlaceInformation  implements Parcelable {
    private String mPlaceName;
    private Bitmap mPhoto;

    public PlaceInformation(String placeName, Bitmap photo) {
        mPlaceName = placeName;
        mPhoto = photo;
    }

    protected PlaceInformation(Parcel in) {
        mPlaceName = in.readString();
        mPhoto = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<PlaceInformation> CREATOR = new Creator<PlaceInformation>() {
        @Override
        public PlaceInformation createFromParcel(Parcel in) {
            return new PlaceInformation(in);
        }

        @Override
        public PlaceInformation[] newArray(int size) {
            return new PlaceInformation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mPlaceName);
        parcel.writeParcelable(mPhoto, i);
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
}

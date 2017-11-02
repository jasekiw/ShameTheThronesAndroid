package com.jasekiw.shamethethrones.providers.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapUtilities {

    private Context mContext;

    public BitmapUtilities(Context context) {
        mContext = context;
    }

    public Bitmap resize(int resourceID, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(mContext.getResources(),resourceID);
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
    }
}

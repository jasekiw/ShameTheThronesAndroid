package com.jasekiw.shamethethrones.providers.map.util;


import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import com.jasekiw.shamethethrones.R;
import com.jasekiw.shamethethrones.providers.map.ManagedMarker;
import com.jasekiw.shamethethrones.providers.util.BitmapUtilities;

public class MarkerAnimation {

    private BitmapUtilities mBmu;

    public MarkerAnimation(BitmapUtilities bmu) {
        mBmu = bmu;
    }

    public void applyScaleEffect(final ManagedMarker marker, final int targetSize) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 200;

        final Interpolator interpolator = new DecelerateInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {

                if(marker.getMarker() == null)
                    return;

                long elapsed = SystemClock.uptimeMillis() - start;
                float t = Math.max(
                        1 - interpolator.getInterpolation((float) elapsed
                                / duration), 0);

                int width = (int) (targetSize - (50.0 * t));
                int height = (int) (targetSize - (50.0 * t));
                marker.getMarker().setIcon(BitmapDescriptorFactory.fromBitmap(mBmu.resize(R.drawable.marker, width, height)));
                if (t > .1) {
                    // Post again 15ms later.
                    handler.postDelayed(this, 30);
                } else {
                    marker.getMarker().setIcon(BitmapDescriptorFactory.fromBitmap(mBmu.resize(R.drawable.marker, targetSize, targetSize)));
//                    marker.getMarker().showInfoWindow();

                }
            }
        });
    }
}

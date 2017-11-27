package com.jasekiw.shamethethrones.providers.animations;

import android.view.animation.Interpolator;

public class MVAccelerateDecelerateInterpolator implements Interpolator {
    @Override
    public float getInterpolation(float t) {
        float x;
        if (t<0.5f)
        {
            x = t*2.0f;
            return 0.5f*x*x*x*x*x;
        }
        x = (t-0.5f)*2-1;
        return 0.5f*x*x*x*x*x+1;
    }
}

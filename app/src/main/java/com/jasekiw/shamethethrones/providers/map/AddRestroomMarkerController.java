package com.jasekiw.shamethethrones.providers.map;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;

public class AddRestroomMarkerController {

    private int lastX = 0;
    private int lastY = 0;

    private LottieAnimationView mView;
    private LinearLayout mLayout;


    public void initialize(LottieAnimationView markerView, LinearLayout layout) {
        mView = markerView;
        mLayout = layout;
        layout.setOnTouchListener((v, event) -> trackTouchCoordinates(event));
        markerView.setOnTouchListener((v, motionEvent) -> true);
    }

    /**
     * Get the coordinates from a click event
     * @param motionEvent
     * @return
     */
    private boolean trackTouchCoordinates(MotionEvent motionEvent) {
        Log.d("main", "touch");
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            lastX = (int) motionEvent.getX();
            lastY = (int) motionEvent.getY();
        }
        return false;
    }

    public void showMarker() {
        mView.setAnimation("data.json");
        mView.loop(false);
        mView.setX(lastX - mView.getWidth() / 2);
        mView.setY(lastY - mView.getHeight());
        mView.setVisibility(View.VISIBLE);
        mView.setSpeed(2);
        mView.playAnimation();
    }


    public void hideMarker() {
        if(mView.getVisibility() == View.GONE)
            return;
        mView.setSpeed(-2);
        mView.playAnimation();
        mView.setVisibility(View.GONE);
    }


    public void toggle() {
        if(mView.getVisibility() == View.VISIBLE)
            hideMarker();
        else
            showMarker();
    }


}

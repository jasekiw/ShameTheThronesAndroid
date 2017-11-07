package com.jasekiw.shamethethrones.providers.map;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;

public class AddRestroomMarkerController {

    private int lastX = 0;
    private int lastY = 0;

    private LottieAnimationView mView;


    /**
     *
     * @param markerView
     * @param layout
     */
    public void initialize(LottieAnimationView markerView, LinearLayout layout) {
        mView = markerView;
        layout.setOnTouchListener((view, event) -> trackTouchCoordinates(event));
        markerView.setOnTouchListener((view, motionEvent) -> true);
    }

    /**
     * Get the coordinates from a click event
     * @param motionEvent
     * @return
     */
    private boolean trackTouchCoordinates(MotionEvent motionEvent) {

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            lastX = (int) motionEvent.getX();
            lastY = (int) motionEvent.getY();
            Log.d("main", "touch: " + lastX + ", " + lastY);
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

    /**
     * @bug The first touch is shown with the wrong x and y coords.
     */
    public void toggle() {
        Log.d("main", "toggle: " + lastX + ", " + lastY);
        if(mView.getVisibility() == View.VISIBLE)
            hideMarker();
        else
            showMarker();
    }


}

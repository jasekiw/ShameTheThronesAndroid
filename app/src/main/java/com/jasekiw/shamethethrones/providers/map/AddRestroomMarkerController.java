package com.jasekiw.shamethethrones.providers.map;

import android.animation.Animator;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.maps.model.LatLng;


public class AddRestroomMarkerController {
    // track the last x and y coordinates that the user touched the screen
    private int lastX = 0;
    private int lastY = 0;
    // if new image is uploaded of the animation these values need to be updated as well
    private int width = 210;
    private int height = 210;
    // used to prevent additional animation from running concurrently
    private boolean animating = false;
    private LottieAnimationView mView;

    private LatLng lastLatLng;

    /**
     *
     * @param markerView
     * @param layout
     */
    public void initialize(LottieAnimationView markerView, LinearLayout layout, OnNewRestroomHandler onTouchListener) {
        mView = markerView;
        mView.setAnimation("data.json");
        mView.loop(false);
        layout.setOnTouchListener((view, event) -> trackTouchCoordinates(event));
        markerView.setOnClickListener((View v) -> {
            Log.d("AddRestroomMarker", "click");
            onTouchListener.handle(lastLatLng);
        });
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
        if(animating) // do not continue if already animating
            return;
        animating = true;
        mView.setX(lastX - width / 2);
        mView.setY(lastY - height);
        mView.setVisibility(View.VISIBLE);
        mView.setSpeed(4);
        mView.playAnimation();
        // animation has now ended and other animations can be started
        new Handler().postDelayed(() -> animating = false, 500);

    }

    public void showMarker(LatLng latLng) {
        if(latLng != null)
            lastLatLng = latLng;
        showMarker();
    }


    public void hideMarker() {
        if(animating || mView.getVisibility() == View.GONE) // do not continue if already animating
            return;
        animating = true; // begin animation
        mView.setSpeed(-8);
        mView.playAnimation();
        new Handler().postDelayed(() -> {
            mView.setVisibility(View.GONE);
            animating = false; // animation has now ended and other animations can be started
        }, 200);
    }

    /**
     * Hide or show the marker
     */
    public void toggle() {
        toggle(null);
    }


    /**
     * Hide or show the marker
     */
    public void toggle(LatLng latLng) {
        if(mView.getVisibility() == View.VISIBLE)
            hideMarker();
        else {
            showMarker(latLng);
        }
    }


}

package com.jasekiw.shamethethrones.providers.api;


import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.LatLngBounds;
import com.jasekiw.shamethethrones.R;
import com.jasekiw.shamethethrones.models.restroom.AddRestroomModel;
import com.jasekiw.shamethethrones.models.restroom.RestroomModel;
import com.jasekiw.shamethethrones.providers.util.DialogUtility;
import com.jasekiw.shamethethrones.providers.util.OnErrorListener;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

public class RestroomApi {

    private RequestQueue mRequestQueue;
    private String mBaseUrl = "restrooms/";
    private Context mContext;

    public RestroomApi(RequestQueue rq, Context context) {
        mRequestQueue = rq;
        mContext = context;
        mBaseUrl = context.getResources().getString(R.string.api_base_url) + mBaseUrl;
    }

    public interface OnCreatedListener {
        void onCreated(RestroomModel model);
    }

    public interface OnGetRestroomResultListener {
        void onResult(RestroomModel[] restrooms);
    }

    public void addRestroom(AddRestroomModel model, OnCreatedListener createdListener, OnErrorListener errorListener) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, mBaseUrl, model.toJson(),
                response -> createdListener.onCreated(RestroomModel.fromJson(response)),
                error ->  errorListener.onError(error.getMessage())
        );
        // prevent adding multiple restrooms if the request was serviced but failed to respond in time
        request.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(request);
    }

    public void getRestrooms( LatLngBounds bounds, OnGetRestroomResultListener resultListener, OnErrorListener errorListener) {
        Log.d("getRestrooms", "Thread#: " + android.os.Process.myTid());
        String uri = "";
        try {
            uri = mBaseUrl +
                    "?northEastLat=" + URLEncoder.encode( String.valueOf(bounds.northeast.latitude), "UTF-8")  +
                    "&northEastLng=" + URLEncoder.encode( String.valueOf(bounds.northeast.longitude), "UTF-8") +
                    "&southWestLat=" + URLEncoder.encode( String.valueOf(bounds.southwest.latitude), "UTF-8") +
                    "&southWestLng=" + URLEncoder.encode( String.valueOf(bounds.southwest.longitude), "UTF-8");
        }
        catch(UnsupportedEncodingException e) {}

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, uri, null,
                response -> {
                    Log.d("getRestrooms", "Response Thread#: " + android.os.Process.myTid());
                    RestroomModel[] models = new RestroomModel[response.length()];
                    try {
                        for(int i = 0; i < response.length(); i++)
                            models[i] = RestroomModel.fromJson(response.getJSONObject(i));
                        resultListener.onResult(models);
                    } catch (JSONException e) {
                        DialogUtility.showErrorDialog(mContext, "Something went wrong when parsing restrooms!");
                    }
                },
                error ->  errorListener.onError(error.getMessage())
        );
        mRequestQueue.add(request);
    }
}

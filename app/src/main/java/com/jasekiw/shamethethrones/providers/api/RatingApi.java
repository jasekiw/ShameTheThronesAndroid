package com.jasekiw.shamethethrones.providers.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jasekiw.shamethethrones.R;
import com.jasekiw.shamethethrones.models.NewRatingResponseModel;
import com.jasekiw.shamethethrones.models.RatingModel;
import com.jasekiw.shamethethrones.providers.util.DialogUtility;
import com.jasekiw.shamethethrones.providers.util.OnErrorListener;

import org.json.JSONException;

import java.util.concurrent.TimeUnit;

public class RatingApi {

    public interface OnCreatedListener {
        void onCreated(NewRatingResponseModel model);
    }

    public interface OnGetRatingsResultListener {
        void onResult(RatingModel[] ratings);
    }

    private RequestQueue mRequestQueue;
    private String mBaseUrl = "ratings/";
    private Context mContext;

    public RatingApi(RequestQueue rq, Context context) {
        mRequestQueue = rq;
        mContext = context;
        mBaseUrl = context.getResources().getString(R.string.api_base_url) + mBaseUrl;
    }

    public void addRating(RatingModel model, RatingApi.OnCreatedListener createdListener, OnErrorListener errorListener) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, mBaseUrl, model.toJson(),
                response -> createdListener.onCreated(NewRatingResponseModel.fromJson(response)),
                error ->  errorListener.onError(error.getMessage())
        );
        // prevent adding multiple ratings if the request was serviced but failed to respond in time
        request.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(request);
    }

    public void getRatings(int restroomId, OnGetRatingsResultListener resultListener, OnErrorListener errorListener) {
        String uri = mBaseUrl + "?restroomId=" + restroomId;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, uri, null,
                response -> {
                    Log.d("getRestrooms", "Response Thread#: " + android.os.Process.myTid());
                    RatingModel[] models = new RatingModel[response.length()];
                    try {
                        for(int i = 0; i < response.length(); i++)
                            models[i] = RatingModel.fromJson(response.getJSONObject(i));
                        resultListener.onResult(models);
                    } catch (JSONException e) {
                        DialogUtility.showErrorDialog(mContext, "Something went wrong when parsing ratings!");
                    }
                },
                error ->  errorListener.onError(error.getMessage())
        );
        mRequestQueue.add(request);
    }
}

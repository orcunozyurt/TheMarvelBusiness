package com.nerdzlab.themarvelbusiness.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.karumi.marvelapiclient.model.ComicDto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by orcun on 08.02.2017.
 */

public class MyApplication extends Application {

    public static final String TAG = MyApplication.class
            .getSimpleName();
    private static MyApplication mInstance;
    private RequestQueue mRequestQueue;

    private String hash;


    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

    }


    public List<ComicDto> retrieveDataSharedPrefs() {
        SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences("DATA", Context.MODE_PRIVATE);
        String act = sharedpreferences.getString("comiclist", null);

        Gson gson = new Gson();
        List<ComicDto> comiclist = new ArrayList<>();

        try {
            JSONObject jsonObj = new JSONObject(act);
            JSONArray jsonArr = jsonObj.getJSONArray("result");
            for(int i = 0; i < jsonArr.length(); i++)
            {
                ComicDto comic =
                        gson.fromJson(String.valueOf(jsonArr),
                                ComicDto.class);

                comiclist.add(comic);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return comiclist;
    }
    public void setDataOffline(String data) {

        SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences("DATA", Context.MODE_PRIVATE);
        sharedpreferences.edit().putString("comiclist", data).commit();
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }


    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }


}

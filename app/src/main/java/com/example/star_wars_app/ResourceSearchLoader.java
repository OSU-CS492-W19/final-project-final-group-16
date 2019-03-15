package com.example.star_wars_app;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.star_wars_app.utils.NetworkUtils;

import java.io.IOException;

public class ResourceSearchLoader extends AsyncTaskLoader<String> {
    private static final String TAG = ResourceSearchLoader.class.getSimpleName();

    private String mWeatherSearchJSON;
    private String mURL;
    private String mType;

    ResourceSearchLoader(Context context, String url, String type) {
        super(context);
        mURL = url;
        mType = type;
    }

    public String getType(){
        return mType;
    }
    @Override
    protected void onStartLoading() {
        if (mURL != null) {
            if (mWeatherSearchJSON != null) {
                Log.d(TAG, "Delivering cached results");
                deliverResult(mWeatherSearchJSON);
            } else {
                forceLoad();
            }
        }
    }

    @Nullable
    @Override
    public String loadInBackground() {
        if (mURL != null) {
            String results = null;
            try {
                Log.d(TAG, "loading results from Weather API with URL: " + mURL);
                results = NetworkUtils.doHTTPGet(mURL);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return results;
        } else {
            return null;
        }
    }

    @Override
    public void deliverResult(@Nullable String data) {
        mWeatherSearchJSON = data;
        super.deliverResult(data);
    }
}
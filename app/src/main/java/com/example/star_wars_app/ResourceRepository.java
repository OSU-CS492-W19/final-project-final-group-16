package com.example.star_wars_app;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.example.star_wars_app.utils.NetworkUtils;
import com.example.star_wars_app.utils.SWAPIUtils;

import java.io.IOException;


public class ResourceRepository {
    public enum Status{
        LOADING,
        ERROR,
        SUCCESS
    }

    private static final String TAG = ResourceRepository.class.getSimpleName();

    private MutableLiveData<SWAPIUtils.PersonResource[]> mSearchData;
    private MutableLiveData<SWAPIUtils.PlanetResource[]> mPlanetData;
    private MutableLiveData<SWAPIUtils.FilmResource[]> mFilmData;

    private MutableLiveData<Status> mStatus;
    private String currSearch;
    private String sKey;

    public ResourceRepository(){
        mSearchData = new MutableLiveData<>();
        mSearchData.setValue(null);
        mPlanetData = new MutableLiveData<>();
        mPlanetData.setValue(null);
        mFilmData = new MutableLiveData<>();
        mFilmData.setValue(null);
        mStatus = new MutableLiveData<>();
        mStatus.setValue(Status.SUCCESS);
        currSearch = "";
        sKey = "";
    }

    public LiveData<SWAPIUtils.PersonResource[]> getSearchData(){
        return mSearchData;
    }

    public LiveData<SWAPIUtils.PlanetResource[]> getPlanetData(){
        return mPlanetData;
    }

    public LiveData<SWAPIUtils.FilmResource[]> getFilmData(){
        return mFilmData;
    }

    public LiveData<Status> getStatus(){
        return mStatus;
    }

    public void loadSWAPICall(String url, String searchKey){
        Log.d("comp:", url + " curr: " + currSearch);
        if (!url.equals(currSearch)){
            currSearch = url;
            sKey = searchKey;
            mStatus.setValue(Status.LOADING);
            new SWAPIAsyncTask(currSearch, sKey).execute();
        }else{
            Log.d(TAG, "using cached resuslts");
            mStatus.setValue(Status.SUCCESS);

        }
    }



    class SWAPIAsyncTask extends AsyncTask<Void, Void, String>{

        private String mUrl;
        private String searchKey;

        public SWAPIAsyncTask(String url, String key){
            mUrl = url;
            searchKey = key;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String retVal = null;
            try {
                Log.d(TAG, "loading results from SW API with URL: " + mUrl);
                retVal = NetworkUtils.doHTTPGet(mUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return retVal;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                mStatus.setValue(ResourceRepository.Status.SUCCESS);
                mSearchData.setValue(SWAPIUtils.parsePersonJSON(s));
                if (searchKey.equals("planets")) {
                    mPlanetData.setValue(SWAPIUtils.parsePlanetJSON(s));
                } else if (searchKey.equals("films")) {
                    mFilmData.setValue(SWAPIUtils.parseFilmJSON(s));
                }
            }else{
                mStatus.setValue(ResourceRepository.Status.ERROR);
            }
        }
    }

}

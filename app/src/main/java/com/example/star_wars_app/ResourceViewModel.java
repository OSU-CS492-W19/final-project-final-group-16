package com.example.star_wars_app;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.star_wars_app.utils.SWAPIUtils;

public class ResourceViewModel extends ViewModel {

    private LiveData<SWAPIUtils.PersonResource[]> mSearchData;
    private LiveData<ResourceRepository.Status> mStatus;
    private LiveData<SWAPIUtils.PlanetResource[]> mPlanetData;
    private LiveData<SWAPIUtils.FilmResource[]> mFilmData;

    private ResourceRepository mRepo;

    public ResourceViewModel(){
        mRepo = new ResourceRepository();
        mSearchData = mRepo.getSearchData();
        mPlanetData = mRepo.getPlanetData();
        mFilmData = mRepo.getFilmData();
        mStatus = mRepo.getStatus();
        //get mSearchData and mStatus from the repo
    }

    public void loadResources(String url, String searchKey){
        Log.d("invm", "in Loadresources");
        mRepo.loadSWAPICall(url, searchKey);
    }

    public LiveData<SWAPIUtils.PersonResource[]> getSearchData(){return mSearchData;}

    public LiveData<SWAPIUtils.PlanetResource[]> getPlanetData() {
        return mPlanetData;
    }

    public LiveData<SWAPIUtils.FilmResource[]> getFilmData() {
        return mFilmData;
    }

    public LiveData<ResourceRepository.Status> getStatus() {
        return mStatus;
    }
}


package com.example.star_wars_app;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.star_wars_app.utils.NetworkUtils;
import com.example.star_wars_app.utils.SWAPIUtils;


public class MainActivity extends AppCompatActivity
        implements PersonAdapter.OnForecastItemClickListener {

    private SharedPreferences settings;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String SEARCH_URL_KEY = "resourceSearchURL";
    private static final String SEARCH_TYPE_KEY = "resourceSearchType";
    private static final int RESOURCE_SEARCH_LOADER_ID = 0;

    private SWAPIUtils.PersonResource[] mPeople;
    private SWAPIUtils.PlanetResource[] mPlanets;
    private SWAPIUtils.FilmResource[] mFilms;

    private EditText mSearchBoxET;
    private PersonAdapter mPersonAdapter;
    private RecyclerView mSearchRV;
    private ProgressBar mLoadingPB;

    private ResourceViewModel mResourceVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBoxET = findViewById(R.id.et_search_box);
        mPersonAdapter = new PersonAdapter(this);
        mSearchRV = findViewById(R.id.rv_search_results);
        mSearchRV.setAdapter(mPersonAdapter);

        mLoadingPB = findViewById(R.id.pb_loading);

        mSearchRV.setLayoutManager(new LinearLayoutManager(this));
        mSearchRV.setHasFixedSize(true);

        settings = PreferenceManager.getDefaultSharedPreferences(this);
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                String searchkey = sharedPreferences.getString(getString(R.string.pref_sort_key),
                        getString(R.string.pref_sort_default));
                mSearchBoxET.setHint("Search for " + searchkey);
            }
        };

        String searchkey = sharedPreferences.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_default));
        mSearchBoxET.setHint("Search for " + searchkey);
        settings.registerOnSharedPreferenceChangeListener(listener);


        Button searchButton = findViewById(R.id.btn_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = mSearchBoxET.getText().toString();
                if (!TextUtils.isEmpty(searchQuery)) {
                    doSearch(searchQuery);
                }
            }
        });

        mResourceVM = ViewModelProviders.of(this).get(ResourceViewModel.class);
        mResourceVM.getSearchData().observe(this, new Observer<SWAPIUtils.PersonResource[]>() {
            @Override
            public void onChanged(@Nullable SWAPIUtils.PersonResource[] personResources) {
                mPersonAdapter.updatePeople(personResources);
                mPeople = personResources;
            }
        });
        mResourceVM.getFilmData().observe(this, new Observer<SWAPIUtils.FilmResource[]>() {
            @Override
            public void onChanged(@Nullable SWAPIUtils.FilmResource[] filmResources) {
                mFilms = filmResources;
            }
        });
        mResourceVM.getPlanetData().observe(this, new Observer<SWAPIUtils.PlanetResource[]>() {
            @Override
            public void onChanged(@Nullable SWAPIUtils.PlanetResource[] planetResources) {
                mPlanets = planetResources;
            }
        });

        mResourceVM.getStatus().observe(this, new Observer<ResourceRepository.Status>() {
            @Override
            public void onChanged(@Nullable ResourceRepository.Status status) {
                if (status == ResourceRepository.Status.LOADING){
                    mLoadingPB.setVisibility(View.VISIBLE);
                }else if (status == ResourceRepository.Status.SUCCESS){
                    mLoadingPB.setVisibility(View.INVISIBLE);
                    mSearchRV.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_favorite:
                Intent favIntent = new Intent(this, FavoritesActivity.class);
                startActivity(favIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void doSearch(String query) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String searchType = sharedPreferences.getString(
                getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_default)
        );
        String language = sharedPreferences.getString(
                getString(R.string.pref_language_key),
                getString(R.string.pref_language_default)
        );

        String url = SWAPIUtils.buildSearch(query, searchType);
        Log.d(TAG, "querying search URL: " + url);
        mResourceVM.loadResources(url, searchType);

    }


    @Override
    public void onForecastItemClick(int pos) {
        Intent intent = new Intent(this, ResourceDetailActivity.class);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String searchType = sharedPreferences.getString(
                getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_default)
        );
        if(searchType.equals("people")){
            intent.putExtra(SWAPIUtils.EXTRA_RESOURCE, mPeople[pos]);
            intent.putExtra(SWAPIUtils.EXTRA_TYPE, searchType);
        } else if(searchType.equals("planets")){
            intent.putExtra(SWAPIUtils.EXTRA_RESOURCE, mPlanets[pos]);
            intent.putExtra(SWAPIUtils.EXTRA_TYPE, searchType);
        } else {
            intent.putExtra(SWAPIUtils.EXTRA_RESOURCE, mFilms[pos]);
            intent.putExtra(SWAPIUtils.EXTRA_TYPE, searchType);
        }
        startActivity(intent);
    }
}
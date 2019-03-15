package com.example.star_wars_app;

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
        implements PersonAdapter.OnForecastItemClickListener, LoaderManager.LoaderCallbacks<String> {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBoxET = findViewById(R.id.et_search_box);
        mPersonAdapter = new PersonAdapter(this);
        mSearchRV = findViewById(R.id.rv_search_results);
        mSearchRV.setAdapter(mPersonAdapter);

        mLoadingPB = findViewById(R.id.pb_loading);

        mSearchRV.setLayoutManager(new LinearLayoutManager(this));
        mSearchRV.setHasFixedSize(true);

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
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {
        String url = null;
        String type = null;
        if (bundle != null) {
            url = bundle.getString(SEARCH_URL_KEY);
            type = bundle.getString(SEARCH_TYPE_KEY);
        }
        return new ResourceSearchLoader(this, url, type);
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

        Bundle args = new Bundle();
        args.putString(SEARCH_URL_KEY, url);
        args.putString(SEARCH_TYPE_KEY, searchType);
        mLoadingPB.setVisibility(View.VISIBLE);
        mSearchRV.setVisibility(View.INVISIBLE);
        getSupportLoaderManager().restartLoader(RESOURCE_SEARCH_LOADER_ID, args, this);

    }



    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String s) {
        Log.d(TAG, "Got results from the loader");
        if (s != null) {
            mSearchRV.setVisibility(View.VISIBLE);
            mPeople = SWAPIUtils.parsePersonJSON(s);
            mPersonAdapter.updatePeople(mPeople);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

            String searchType = sharedPreferences.getString(
                    getString(R.string.pref_sort_key),
                    getString(R.string.pref_sort_default)
            );
            if(searchType.equals("planets")){
                mPlanets = SWAPIUtils.parsePlanetJSON(s);
            } else if(searchType.equals("films")){
                mFilms = SWAPIUtils.parseFilmJSON(s);
            }


        }
        mLoadingPB.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        // Nothing to do here...
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
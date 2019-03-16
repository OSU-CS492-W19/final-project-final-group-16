package com.example.star_wars_app;

import android.content.Intent;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.star_wars_app.utils.SWAPIUtils;

import java.text.DateFormat;

public class ResourceDetailActivity extends AppCompatActivity {

    private String mType;

    private TextView mNameTv;
    private TextView mYearTv;
    private TextView mHeightTv;
    private TextView mSexTv;

    private TextView mClimateTv;
    private TextView mGravityTv;
    private TextView mDiameterTv;
    private TextView mPeriodTv;
    private TextView mPopulationTv;

    private TextView mDirectorTv;
    private TextView mReleaseTv;
    private TextView mEpisodeTv;
    private TextView mProducerTv;


    private SWAPIUtils.PersonResource mPerson;
    private SWAPIUtils.PlanetResource mPlanet;
    private SWAPIUtils.FilmResource mFilm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(SWAPIUtils.EXTRA_TYPE) && intent.hasExtra(SWAPIUtils.EXTRA_RESOURCE)) {
            mType = intent.getStringExtra( SWAPIUtils.EXTRA_TYPE);
            if(mType.equals("films")) {
                mFilm = (SWAPIUtils.FilmResource) intent.getSerializableExtra(
                        SWAPIUtils.EXTRA_RESOURCE
                );
                setContentView(R.layout.activity_film_item_detail);
                mNameTv = findViewById(R.id.tv_film_name);
                mDirectorTv = findViewById(R.id.tv_director);
                mReleaseTv = findViewById(R.id.tv_release);
                mEpisodeTv = findViewById(R.id.tv_episode);
                mProducerTv = findViewById(R.id.tv_producer);
                fillInLayout(mFilm);
            } else if(mType.equals("planets")){
                mPlanet = (SWAPIUtils.PlanetResource) intent.getSerializableExtra(
                        SWAPIUtils.EXTRA_RESOURCE
                );

                setContentView(R.layout.activity_planets_item_detail);
                mNameTv = findViewById(R.id.tv_planet_name);
                mPopulationTv = findViewById(R.id.tv_planet_population);
                mClimateTv = findViewById(R.id.tv_planet_climate);
                mGravityTv = findViewById(R.id.tv_planet_gravity);
                mDiameterTv = findViewById(R.id.tv_planet_diameter);
                mPeriodTv = findViewById(R.id.tv_planet_period);
                fillInLayout(mPlanet);
            } else {

                mPerson = (SWAPIUtils.PersonResource) intent.getSerializableExtra(
                        SWAPIUtils.EXTRA_RESOURCE
                );
                setContentView(R.layout.activity_person_item_detail);

                mNameTv = findViewById(R.id.tv_name);
                mYearTv = findViewById(R.id.tv_year);
                mHeightTv = findViewById(R.id.tv_height);
                mSexTv = findViewById(R.id.tv_sex);
                fillInLayout(mPerson);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.resource_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                shareForecast();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void shareForecast() {
        if (!mType.isEmpty()) {
            String shareText = createShareString(mType);
            ShareCompat.IntentBuilder.from(this)
                    .setType("text/plain")
                    .setText(shareText)
                    .setChooserTitle(R.string.share_chooser_title)
                    .startChooser();
        }
    }

    public String createShareString(String type){
        String sharetxt = "";
        if (type.equals("films")){
            sharetxt = String.format("Checkout my favorite Star Wars movie: %s!" +
                    " Did you know it was directed by: %s?", mFilm.title, mFilm.director);
        }else if (type.equals("planets")){
            sharetxt = String.format("Did you know that the planet %s from Star Wars has "+
             "an orbital period of %s days?", mPlanet.name, mPlanet.orbital_period);
        }else{
            sharetxt = String.format("My favorite star wars character is %s cm tall! Can you guess who it is?",
                    mPerson.height);
        }
        return sharetxt;
    }

    private void fillInLayout(SWAPIUtils.PersonResource person) {

        String nameString = getString(R.string.name_format, person.name);
        String yearString = getString(R.string.year_format, person.birth_year);
        String heightString = getString(R.string.height_format, person.height);
        String sexString = getString(R.string.sex_format, person.gender);

        mNameTv.setText(nameString);
        mYearTv.setText(yearString);
        mHeightTv.setText(heightString);
        mSexTv.setText(sexString);
    }
    private void fillInLayout(SWAPIUtils.PlanetResource planet) {

        String nameString = getString(R.string.name_format, planet.name);
        String gravityString = getString(R.string.planet_gravity_format, planet.gravity);
        String periodString = getString(R.string.planet_period_format, planet.orbital_period);
        String populationString = getString(R.string.planet_population_format, planet.population);
        String diameterString = getString(R.string.planet_diameter_format, planet.diameter);
        String climateString = getString(R.string.planet_climate_format, planet.climate);

        mNameTv.setText(nameString);
        mGravityTv.setText(gravityString);
        mPeriodTv.setText(periodString);
        mPopulationTv.setText(populationString);
        mDiameterTv.setText(diameterString);
        mClimateTv.setText(climateString);
    }

    private void fillInLayout(SWAPIUtils.FilmResource film){
        String nameString = getString(R.string.film_name_format, film.title);
        String directorString = getString(R.string.film_director_format, film.director);
        String producerString = getString(R.string.film_producer_format, film.producer);
        String releaseString = getString(R.string.film_release_format, film.release_date);
        String episodeString = getString(R.string.film_episode_format, film.episode_id);

        mNameTv.setText(nameString);
        mDirectorTv.setText(directorString);
        mProducerTv.setText(producerString);
        mReleaseTv.setText(releaseString);
        mEpisodeTv.setText(episodeString);
    }
}

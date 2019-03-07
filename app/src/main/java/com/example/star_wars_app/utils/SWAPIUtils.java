package com.example.star_wars_app.utils;

import android.app.Person;
import android.content.SharedPreferences;
import android.net.Uri;
import com.google.gson.Gson;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class SWAPIUtils {

    public static final String EXTRA_RESOURCE = "com.example.android.lifecycleweather.utils.ForecastItem";

    private final static String SWAPI_RESOURCE_BASE_URL = "https://swapi.co/api/";
    private final static String SWAPI_RESOURCE_SEARCH_PARAM = "search";
    private final static String SWAPI_RESOURCE_FILM = "films";
    private final static String SWAPI_RESOURCE_PEOPLE = "people";
    private final static String SWAPI_RESOURCE_PLANETS = "planets";

    //private static String type = SWAPI_RESOURCE_PEOPLE;

    public static class PeopleSearchResult implements Serializable {
        public int count;
        public String next;
        public String previous;
        public PersonResource[] results;
    }

    public static class PlanetSearchResult implements Serializable {
        public int count;
        public String next;
        public String previous;
        public PlanetResource[] results;
    }

    public static class FilmSearchResult implements Serializable {
        public int count;
        public String next;
        public String previous;
        public FilmResource[] results;
    }

    /*
     * This class is used as a final representation of a single forecast item.  It condenses the
     * classes below that are used for parsing the OWN JSON response with Gson.
     */
    public static class PersonResource implements Serializable {
        public String birthYear;
        public String eyeColor;
        public String[] films;
        public String gender;
        public String hair_color;
        public String height;
        public String homeWorld;
        public String mass;
        public String name;
        public String skinColor;
        public String[] species;
        public String url;

    }

    public static class PlanetResource implements Serializable {
        public String climate;
        public String created;
        public String diameter;
        public String edited;
        public String[] films;
        public String gravity;
        public String name;
        public String orbital_period;
        public String population;
        public String[] residents;
        public String rotation_period;
        public String surface_water;
        public String terrain;
        public String url;
    }

    public static class FilmResource implements Serializable {
        public String climate;
        public String created;
        public String diameter;
        public String edited;
        public String[] films;
        public String gravity;
        public String name;
        public String orbital_period;
        public String population;
        public String[] residents;
        public String rotation_period;
        public String surface_water;
        public String terrain;
        public String url;
    }

    public  static String buildSearch(String query, String type){
        String searchResource = "";
        if (type.equals("People"))
            searchResource = SWAPI_RESOURCE_PEOPLE;
        if (type.equals("Planents"))
            searchResource = SWAPI_RESOURCE_PLANETS;
        if (type.equals("Films"))
            searchResource = SWAPI_RESOURCE_FILM;
        return Uri.parse(SWAPI_RESOURCE_BASE_URL).buildUpon()
                .appendPath(searchResource)
                .appendQueryParameter(SWAPI_RESOURCE_SEARCH_PARAM, query)
                .build()
                .toString();
    }

    public static PersonResource[] parsePersonJSON(String json) {
        Gson gson = new Gson();
        PeopleSearchResult results = gson.fromJson(json, PeopleSearchResult.class);
        if (results != null && results.results != null) {
            return results.results;
        } else {
            return null;
        }
    }
}
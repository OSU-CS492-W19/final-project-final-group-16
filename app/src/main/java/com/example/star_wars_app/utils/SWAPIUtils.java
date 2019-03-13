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

    public static final String EXTRA_RESOURCE = "com.example.android.star_wars_app.utils.GenericResource";

    private final static String SWAPI_RESOURCE_BASE_URL = "https://swapi.co/api/";
    private final static String SWAPI_RESOURCE_SEARCH_PARAM = "search";
    private final static String SWAPI_RESOURCE_FILM = "films";
    private final static String SWAPI_RESOURCE_PEOPLE = "people";
    private final static String SWAPI_RESOURCE_PLANETS = "planets";

    public static class GenericSearchResult {
        int count;
        String next;
        String previous;
        //GenericResource[] results;
    }


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

    public static class PersonResource extends GenericResource implements Serializable {
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
        public String getInfoString() {
            return this.name;
        }
    }

    public static abstract class GenericResource implements Serializable {
        public abstract String getInfoString();
    }

    public static class PlanetResource extends GenericResource implements Serializable {
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
        public String getInfoString() {
            return this.name;
        }
    }

    public static class FilmResource extends GenericResource implements Serializable {
        public String title;
        public String episode_id;
        public String opening_crawl;
        public String director;
        public String producer;
        public String release_date;
        public String[] character;
        public String[] planets;
        public String url;
        public String getInfoString() {
            return this.title;
        }
    }

    public static String buildSearch(String query, String type){
        String searchResource = "";
        if (type.equals("People"))
            searchResource = SWAPI_RESOURCE_PEOPLE;
        if (type.equals("Planets"))
            searchResource = SWAPI_RESOURCE_PLANETS;
        if (type.equals("Films"))
            searchResource = SWAPI_RESOURCE_FILM;
        return Uri.parse(SWAPI_RESOURCE_BASE_URL).buildUpon()
                .appendPath(searchResource)
                .appendQueryParameter(SWAPI_RESOURCE_SEARCH_PARAM, query)
                .build()
                .toString();
    }

    public static GenericResource[] parseJSON(String json, String type) {
        Gson gson = new Gson();
        GenericResource[] resources = null;
        if(type.equals("People")){
            PeopleSearchResult p = gson.fromJson(json, PeopleSearchResult.class);
            if (p != null && p.results != null)
                resources = p.results;
        } else if(type.equals("Planets")){
            PlanetSearchResult p = gson.fromJson(json, PlanetSearchResult.class);
            if (p != null && p.results != null)
                resources = p.results;
        } else if(type.equals("Films")){
            FilmSearchResult f = gson.fromJson(json, FilmSearchResult.class);
            if (f != null && f.results != null)
                resources = f.results;
        }

        if (resources != null) {
            return resources;
        } else {
            return null;
        }
    }
}
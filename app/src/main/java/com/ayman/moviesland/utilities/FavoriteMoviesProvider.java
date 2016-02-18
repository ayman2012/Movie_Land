package com.ayman.moviesland.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ayman.moviesland.BuildConfig;
import com.ayman.moviesland.models.Movieapp;
import com.google.gson.Gson;
import java.util.ArrayList;



public class FavoriteMoviesProvider {

    private ArrayList<Movieapp> result;

    public FavoriteMoviesProvider() {
    }

    public ArrayList<Movieapp> loadFavorites(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String favoritesString = sharedPreferences.getString(BuildConfig.FAVORITES_PREFERENCE_KEY, null);

        if (favoritesString == null)
            return null;


        result = new ArrayList<Movieapp>();

        String[] splitter = favoritesString.split("\\|");

        for (String element : splitter) {
            if (!element.equals("")) {
                try {
                    Gson gson = new Gson();
                    Movieapp movieapp = gson.fromJson(element, Movieapp.class);
                    result.add(movieapp);
                } catch (Exception ex) {
                }
            }
        }
        return result;
    }

    public void addToFavorite(Context context, Movieapp movieapp) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String favoritesString = sharedPreferences.getString(BuildConfig.FAVORITES_PREFERENCE_KEY, null);

        Gson gson = new Gson();
        String gsonStr = gson.toJson(movieapp);

        if (favoritesString == null) {
            favoritesString = gsonStr + "|";
        } else {
            favoritesString += gsonStr + "|";
        }

        saveSharedPreferences(context, favoritesString);
    }

    public void removeFromFavorite(Context context, Movieapp movieapp) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String favoritesString = sharedPreferences.getString(BuildConfig.FAVORITES_PREFERENCE_KEY, null);

        Gson gson = new Gson();
        String gsonStr = gson.toJson(movieapp);

        if (favoritesString == null) {
            return;
        } else {
            favoritesString = favoritesString.replace(gsonStr, "");
        }
        saveSharedPreferences(context, favoritesString);
    }

    public boolean isFavorite(Context context, Movieapp movieapp) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String favoritesString = sharedPreferences.getString(BuildConfig.FAVORITES_PREFERENCE_KEY, null);

        Gson gson = new Gson();
        String gsonStr = gson.toJson(movieapp);

        if (favoritesString == null) return false;
        return (favoritesString.indexOf(gsonStr) != -1) ? true : false;
    }

    private void saveSharedPreferences(Context context, String favorites) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(BuildConfig.FAVORITES_PREFERENCE_KEY, favorites);
        editor.commit();
    }
}

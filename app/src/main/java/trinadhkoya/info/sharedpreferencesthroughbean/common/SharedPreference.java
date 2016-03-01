package trinadhkoya.info.sharedpreferencesthroughbean.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import trinadhkoya.info.sharedpreferencesthroughbean.model.Doctor;


public class SharedPreference {

    public static final String PREFS_NAME = "PRODUCT_APP";
    public static final String FAVORITES = "Product_Favorite";

    public SharedPreference() {
        super();
    }

    // This four methods are used for maintaining favorites.
    public void saveFavorites(Context context, List<Doctor> favorites) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FAVORITES, jsonFavorites);

        editor.commit();
    }

    public void addFavorite(Context context, Doctor doctor) {
        List<Doctor> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<Doctor>();
        favorites.add(doctor);
        saveFavorites(context, favorites);
    }

    public void removeFavorite(Context context, Doctor doctor) {
        ArrayList<Doctor> favorites = getFavorites(context);
        if (favorites != null) {
            favorites.remove(doctor);
            saveFavorites(context, favorites);
        }
    }

    public ArrayList<Doctor> getFavorites(Context context) {
        SharedPreferences settings;
        List<Doctor> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            Doctor[] favoriteItems = gson.fromJson(jsonFavorites,
                    Doctor[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<Doctor>(favorites);
        } else
            return null;

        return (ArrayList<Doctor>) favorites;
    }
}

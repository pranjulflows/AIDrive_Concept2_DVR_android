package com.softradix.core;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class AppPreferences {
    private static AppPreferences appPreference;
    private SharedPreferences sharedPreferences;

    private AppPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.APP_PREFS, Context.MODE_PRIVATE);
    }


    public static AppPreferences init(Context context) {
        if (appPreference == null) {
            appPreference = new AppPreferences(context);
        }
        return appPreference;
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void clearEditor(Activity activity) {

        sharedPreferences.edit().clear().apply();
    }

    public void saveDetails(String key, Object object) {
        Gson gson = new Gson();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, gson.toJson(object));
        editor.apply();
    }

    public <T> T getDetails(String key, Class<T> type) {
        Gson gson = new Gson();
        return gson.fromJson(sharedPreferences.getString(key, ""), type);
    }
}

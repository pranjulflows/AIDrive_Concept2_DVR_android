package com.aidrive.aidriveconcept.app;

import android.app.Application;
import android.content.Context;

import com.softradix.core.AppPreferences;

public class AiDriveApp extends Application {
    private static AiDriveApp instance;
    private static AppPreferences appPreferences;

    private Context context;

    public static AiDriveApp getContext() {
        return instance;
    }

    /**
     * return context of app here
     */
    public static Context context() {
        if (instance == null) {
            instance = new AiDriveApp();
        }
        return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        context = this;
        appPreferences = AppPreferences.init(context);
    }

    public static AppPreferences getAppPreferences() {
        return appPreferences;
    }
}

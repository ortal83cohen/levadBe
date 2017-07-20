package com.query.social.app;

import android.app.Application;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.query.social.app.service.SharedPreferencesService;


public class LevadBeApplication extends Application {

    private static SharedPreferencesService sharedPreferencesService;
    private static boolean isManager;

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp fb = FirebaseApp.initializeApp(this);

        if (fb == null) {
            Log.e("", "NULL");
        }
        sharedPreferencesService = new SharedPreferencesService(getBaseContext());
        isManager = sharedPreferencesService.isManager();
    }

    public static boolean isManager() {
        return isManager;
    }

    public static void setIsManager(boolean inIsManager) {
        isManager = inIsManager;
        sharedPreferencesService.saveIsManager(isManager);
    }

}

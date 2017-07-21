package com.levadbe.berlin.app;

import android.app.Application;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.levadbe.berlin.app.service.SharedPreferencesService;


public class LevadBeApplication extends Application {

    private static SharedPreferencesService sharedPreferencesService;
    private static boolean isManager;

    public static boolean isManager() {
        return isManager;
    }

    public static void setIsManager(boolean inIsManager) {
        isManager = inIsManager;
        sharedPreferencesService.saveIsManager(isManager);
    }

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

}

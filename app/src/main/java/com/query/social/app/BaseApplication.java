package com.query.social.app;

import android.app.Application;
import android.util.Log;

import com.google.firebase.FirebaseApp;


public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp fb = FirebaseApp.initializeApp(this);

        if (fb == null) {
            Log.e("", "NULL");
        }
    }

}

package com.levadbe.berlin.app.service;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ortal Cohen on 20/7/2017.
 */

public class SharedPreferencesService {

    private static final String PREFERENCES = "levadBe";
    SharedPreferences sharedpreferences;

    public SharedPreferencesService(Context context) {

        sharedpreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

    }

    public void saveIsManager(boolean isManager) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("isManager", isManager);
        editor.commit();

    }

    public boolean isManager() {
        return sharedpreferences.getBoolean("isManager", false);
    }

    public void saveGroup(String group) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("group", group);
        editor.commit();

    }

    public String getGroup() {
        return sharedpreferences.getString("group", "");
    }
}

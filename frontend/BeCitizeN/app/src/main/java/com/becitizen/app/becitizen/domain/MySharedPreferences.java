package com.becitizen.app.becitizen.domain;

import android.content.Context;
import android.content.SharedPreferences;

import com.becitizen.app.becitizen.exceptions.SharedPreferencesException;

import static android.content.Context.MODE_PRIVATE;

public class MySharedPreferences {

    private static MySharedPreferences instance;
    private Context context;

    public static void init(Context context) {
        if(instance == null) {
            instance = new MySharedPreferences();
            instance.context = context;
        }
    }

    public static MySharedPreferences getInstance() throws SharedPreferencesException {
        if(instance == null) {
            throw new SharedPreferencesException("MySharedPreferences has not been initialized");
        }
        return instance;
    }

    public void saveValue(String name, String key, String value) {

        SharedPreferences settings = context.getSharedPreferences(name, MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getValue(String name, String key) {

        SharedPreferences settings = context.getSharedPreferences(name, MODE_PRIVATE);
        return settings.getString(key, "");
    }

}

package com.pic.libphotocollage.core.pref;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by Adm on 12/9/2015.
 */
public class LibPref {

    public static final String KEY_ROUND_SIZE = "KEY_ROUND_SIZE";
    public static final String KEY_MARGIN_SIZE = "KEY_MARGIN_SIZE";
    public static final String KEY_OPACITY_SIZE = "KEY_OPACITY_SIZE";
    private static LibPref instance = null;
    private SharedPreferences preferences = null;
    private boolean isInit = false;

    public LibPref() {

    }

    public static LibPref getInstance() {
        if (instance == null) {
            instance = new LibPref();
        }
        return instance;
    }

    public static void init(Context ctx) {
        LibPref instance = getInstance();

        if (instance.isInit()) {
            return;
        }

        instance.setIsInit(true);
        SharedPreferences prefs = ctx.getSharedPreferences(ctx.getPackageName(), Context.MODE_PRIVATE);
        instance.setPreferences(prefs);

        releaseUnused();
    }

    private static void releaseUnused() {
        instance.remove(LibPref.KEY_ROUND_SIZE);
        instance.remove(LibPref.KEY_MARGIN_SIZE);
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public void setPreferences(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public boolean isInit() {
        return isInit;
    }

    public void setIsInit(boolean isInit) {
        this.isInit = isInit;
    }

    public void putString(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    public void putBoolean(String key, boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }

    public void putInt(String key, int value) {
        preferences.edit().putInt(key, value).apply();
    }

    public void putLong(String key, long value) {
        preferences.edit().putLong(key, value).apply();
    }

    public void putFloat(String key, float value) {
        preferences.edit().putFloat(key, value).apply();
    }

    public String getString(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }

    public long getLong(String key, long defValue) {
        return preferences.getLong(key, defValue);
    }


    public float getFloat(String key, float defValue) {
        return preferences.getFloat(key, defValue);
    }


    public void remove(String key) {
        preferences.edit().remove(key).apply();
    }

}

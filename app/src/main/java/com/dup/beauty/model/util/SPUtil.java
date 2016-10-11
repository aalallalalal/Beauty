package com.dup.beauty.model.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.dup.beauty.app.MyApplication;


/**
 * SharedPreference工具类
 */
public class SPUtil {

    public final static String SP_NAME = "SP_INFO";
    private static SharedPreferences preferences = MyApplication.getInstance()
            .getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);

    public static String getInfoFromShared(String key) {
        return preferences.getString(key, null);
    }

    public static String getInfoFromShared(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    public static boolean getInfoFromShared(String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }

    public static int getInfoFromShared(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    public static boolean setInfoToShared(String key, String value) {
        Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
        return true;
    }

    public static boolean setInfoToShared(String key, boolean value) {
        Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
        return true;
    }

    public static boolean setInfoToShared(String key, int value) {
        Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
        return true;
    }
}

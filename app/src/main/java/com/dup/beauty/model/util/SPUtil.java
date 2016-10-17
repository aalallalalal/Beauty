package com.dup.beauty.model.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.dup.beauty.app.MyApplication;


/**
 * SharedPreference工具类
 */
public class SPUtil {
    /*********SharePreference KEY************/
    /**
     * 加载模式（是否仅Wifi联网）
     */
    public static final String KEY_NET_MODE = "net_mode";
    /**
     * 是否第一次使用app
     */
    public static final String KEY_FIRST_TIME_USE = "fist_time_use";

    /*********
     * SharePreference KEY
     ************/
    public final static String SP_NAME = "SP_INFO";
    private static SharedPreferences preferences = MyApplication.getInstance()
            .getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);

    public static String getString(String key) {
        return preferences.getString(key, null);
    }

    public static String getString(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }

    public static int getInt(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    public static boolean setInfo(String key, String value) {
        Editor editor = preferences.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static boolean setInfo(String key, boolean value) {
        Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public static boolean setInfo(String key, int value) {
        Editor editor = preferences.edit();
        editor.putInt(key, value);
        return editor.commit();
    }
}

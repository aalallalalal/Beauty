package com.dup.beauty.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.File;
import java.security.Permission;

/**
 * Created by DP on 2016/10/14.
 */
public class FileUtil {

    public static File getNetCacheFile(Context context) {
        File cache = null;
        String state;
        try {
            state = Environment.getExternalStorageState();
        } catch (NullPointerException e) {
            state = "";
        } catch (IncompatibleClassChangeError e) {
            state = "";
        }
        if(Environment.MEDIA_MOUNTED.equals(state)&&hasPermission(context)){
            cache = context.getExternalCacheDir();
        }
        if (cache == null) {
            cache = context.getCacheDir();
        }
        if (cache == null) {
            L.w("无法创建网络缓存文件！缓存功能失效");
        }
        return cache;
    }


    private static boolean hasPermission(Context context) {
        int perm = context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
        return perm == PackageManager.PERMISSION_GRANTED;
    }
}

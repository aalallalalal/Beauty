package com.dup.beauty.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.File;

/**
 * Created by DP on 2016/10/14.
 */
public class FileUtil {

    /**
     * 获取下载缓存图片文件
     *
     * @param context
     * @param picPath
     * @param fileName
     * @return
     */
    public static File getDownLoadTempFile(Context context, String picPath, String fileName) {
        File downloadTemp = null;
        String state;
        try {
            state = Environment.getExternalStorageState();
        } catch (NullPointerException e) {
            state = "";
        } catch (IncompatibleClassChangeError e) {
            state = "";
        }
        if (Environment.MEDIA_MOUNTED.equals(state) && hasPermission(context)) {
            File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath(), picPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            downloadTemp = new File(folder, fileName + ".tmp");
        }
        if (downloadTemp == null) {
            L.w("无法实例图片下载缓存文件！下载功能失效");
        }
        return downloadTemp;
    }

    /**
     * 获取图片下载文件
     *
     * @param context
     * @param path    图片文件夹
     * @return 图片文件
     */
    public static File getDownLoadImageFile(Context context, String path, String fileName) {
        File download = null;
        String state;
        try {
            state = Environment.getExternalStorageState();
        } catch (NullPointerException e) {
            state = "";
        } catch (IncompatibleClassChangeError e) {
            state = "";
        }
        if (Environment.MEDIA_MOUNTED.equals(state) && hasPermission(context)) {
            File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath(), path);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            download = new File(folder, fileName);
        }
        if (download == null) {
            L.w("无法创建图片下载文件！下载功能失效");
        }
        return download;
    }

    /**
     * 获取下载的图片文件夹
     *
     * @param context
     * @param path    文件夹路径
     * @return
     */
    public static File getDownLoadFolder(Context context, String path) {
        File download = null;
        String state;
        try {
            state = Environment.getExternalStorageState();
        } catch (NullPointerException e) {
            state = "";
        } catch (IncompatibleClassChangeError e) {
            state = "";
        }
        if (Environment.MEDIA_MOUNTED.equals(state) && hasPermission(context)) {
            download = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath(), path);
        }
        if (download == null) {
            L.w("无法获取图片下载文件！");
        }
        return download;
    }

    /**
     * 获取网络请求缓存文件
     *
     * @param context
     * @return
     */
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
        if (Environment.MEDIA_MOUNTED.equals(state) && hasPermission(context)) {
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

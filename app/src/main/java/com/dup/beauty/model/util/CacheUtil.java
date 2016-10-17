package com.dup.beauty.model.util;

import android.content.Context;

import com.bumptech.glide.Glide;

import java.io.IOException;

import okhttp3.Cache;

/**
 * app缓存数据管理
 * Created by DP on 2016/10/14.
 */
public class CacheUtil {

    /**
     * 清空图片缓存
     *
     * @param context
     */
    public static void clearImageCache(final Context context) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                Glide.get(context).clearDiskCache();
            }
        }.start();
    }

    /**
     * 清空网络请求缓存
     *
     * @param context
     */
    public static void clearNetCache(Context context) {
        Cache cache = HttpUtil.getClient(context).cache();
        if (cache != null) {
            try {
                cache.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 清空所有缓存数据
     *
     * @param context
     */
    public static void clearAllCache(Context context) {
        clearNetCache(context);
        clearImageCache(context);
    }

}

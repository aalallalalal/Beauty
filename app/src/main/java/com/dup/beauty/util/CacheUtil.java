package com.dup.beauty.util;

import android.content.Context;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

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
        Glide.get(context).clearMemory();
        new Thread() {
            @Override
            public void run() {
                super.run();
                Glide.get(context).clearDiskCache();
                EventBus.getDefault().post("IMAGE_CACHE_CLEAR");
            }
        }.start();
    }

}

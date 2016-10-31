package com.dup.beauty.util;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.dup.beauty.model.util.HttpUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
                EventBus.getDefault().post("IMAGE_CACHE_CLEAR");
            }
        }.start();
    }

}

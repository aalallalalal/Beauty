package com.dup.beauty.app;

import android.app.Application;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.dup.beauty.model.util.DBUtil;
import com.dup.beauty.model.util.HttpUtil;
import com.dup.beauty.util.L;
import com.dup.changeskin.SkinManager;

/**
 * Created by DP on 2016/9/18.
 */
public class MyApplication extends Application {
    private static MyApplication INSTANCE;

    public static MyApplication getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;

        L.setDefTag(Constant.TAG);

        DBUtil.getInstance().init(getApplicationContext());

        Glide.get(getApplicationContext()).setMemoryCategory(MemoryCategory.HIGH);

        HttpUtil.addNetModeWhiteList("/tnfs/api/classify");//设置网络模式 白名单

        SkinManager.getInstance().init(this);
    }

}

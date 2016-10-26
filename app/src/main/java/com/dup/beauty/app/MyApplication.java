package com.dup.beauty.app;

import android.app.Application;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.dup.beauty.model.util.DBUtil;
import com.dup.beauty.model.util.HttpUtil;
import com.dup.beauty.util.L;
import com.dup.beauty.util.NetUtil;
import com.dup.changeskin.SkinManager;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

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
        UMShareAPI.get(this);

        INSTANCE = this;

        L.setDefTag(Constant.TAG);

        DBUtil.getInstance().init(getApplicationContext());

        Glide.get(getApplicationContext()).setMemoryCategory(MemoryCategory.HIGH);

        HttpUtil.addNetModeWhiteList("/tnfs/api/classify");//设置网络模式拦截 白名单

        SkinManager.getInstance().init(this);

        NetUtil.initContext(getApplicationContext());

        initUmeng();
    }

    private void initUmeng() {
        //        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        PlatformConfig.setSinaWeibo("3452036421", "1839045677f9f25ff7438ad520bb473b");
        PlatformConfig.setQQZone("1105694481", "scQnufgdjQ39ZDJv");
    }

}

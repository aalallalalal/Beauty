package com.dup.beauty.app;

import android.app.Application;

import com.dup.beauty.model.util.DBUtil;
import com.dup.beauty.model.util.GlideUtil;
import com.dup.beauty.model.util.HttpUtil;
import com.dup.beauty.util.CrashHandler;
import com.dup.beauty.util.L;
import com.dup.beauty.util.NetUtil;
import com.dup.changeskin.SkinManager;
import com.tencent.bugly.crashreport.CrashReport;
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

        GlideUtil.init(getApplicationContext());

        HttpUtil.addNetModeWhiteList("/tnfs/api/classify");//设置网络模式拦截 白名单

        SkinManager.getInstance().init(this);

        NetUtil.initContext(getApplicationContext());

        initUmeng();//分享

        CrashHandler.getInstance().init(getApplicationContext());

        CrashReport.initCrashReport(getApplicationContext(), "900058060", true);//Bugly崩溃
    }

    private void initUmeng() {
        PlatformConfig.setWeixin("wx5d6354a5c9abf5b8", "cb78277c5982700758a7de000314c41f");
//        PlatformConfig.setSinaWeibo("3452036421", "1839045677f9f25ff7438ad520bb473b");
        PlatformConfig.setQQZone("1105694481", "scQnufgdjQ39ZDJv");
    }

}

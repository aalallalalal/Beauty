package com.dup.beauty.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;
import android.widget.Toast;

import com.dup.beauty.R;
import com.dup.beauty.ui.activity.MainActivity;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by DP on 2016/10/27.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static CrashHandler INSTANCE;

    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    //用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<>();

    public static CrashHandler getInstance() {
        if (INSTANCE == null) {
            synchronized (CrashHandler.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CrashHandler();
                }
            }
        }
        return INSTANCE;
    }

    public void init(Context context) {
        this.mContext = context;
        //获取系统默认的uncaughtException
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        L.e("未捕获异常：" + e.getMessage());
        if (!handleException(e) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(t, e);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            ActivityTrackUtil.getInstance().exit();
        }

    }

    private boolean handleException(final Throwable e) {
        if (e == null) {
            return false;
        }
        // 使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, R.string.crash_toast, Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        return true;
    }

}

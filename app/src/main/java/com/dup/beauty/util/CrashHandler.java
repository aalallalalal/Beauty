package com.dup.beauty.util;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.dup.beauty.R;

import java.util.HashMap;
import java.util.Map;

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
            ActivityStackUtil.getInstance().exit();
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

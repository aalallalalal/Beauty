package com.dup.beauty.util;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by DP on 2016/10/28.
 */
public class ActivityTrackUtil {
    public List<Activity> activityList = new LinkedList<>();

    public static ActivityTrackUtil INSTANCE;

    public static ActivityTrackUtil getInstance() {
        if (INSTANCE == null) {
            synchronized (ActivityTrackUtil.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ActivityTrackUtil();
                }
            }
        }
        return INSTANCE;
    }

    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityList.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束所有activity，退出app
     */
    public void exit() {
        while (activityList.size() > 0) {
            removeActivity(activityList.get(activityList.size() - 1));
        }
        System.exit(0);
    }

    /**
     * 退到顶级页面
     */
    public void exitTillOne() {
        while (activityList.size() > 1) {
            removeActivity(activityList.get(activityList.size() - 1));
        }
    }

    /**
     * 根据class name 获取activity
     * @param name
     * @return
     */
    public Activity getActivityByClassName(String name) {
        for (Activity ac : activityList) {
            if (ac.getClass().getName().indexOf(name) >= 0) {
                return ac;
            }
        }
        return null;
    }

    /**
     * 根据class name 获取activity
     * @param cs
     * @return
     */
    public Activity getActivityByClass(Class cs) {
        for (Activity ac : activityList) {
            if (ac.getClass().equals(cs)) {
                return ac;
            }
        }
        return null;
    }

}

package com.dup.beauty.view;

/**
 * Created by DP on 2016/10/14.
 */
public interface ISplashView {
    void onGetNetState(int state, boolean onlyWifi, boolean isFirstTimeUse);

    void onSplash();
}

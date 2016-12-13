package com.dup.beauty.mvp.view;

/**
 * Created by DP on 2016/10/14.
 */
public interface ISplashView extends IBaseView {
    void onGetNetState(int state, boolean onlyWifi, boolean isFirstTimeUse);

    void onSplash();
}

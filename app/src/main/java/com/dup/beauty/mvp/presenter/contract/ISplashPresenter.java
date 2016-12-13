package com.dup.beauty.mvp.presenter.contract;

/**
 * 图库界面GalleryActivity 的presenter
 * Created by DP on 2016/9/18.
 */
public interface ISplashPresenter extends IBasePresenter {
    /**
     * 判断网络状态,和联网模式
     */
    void checkNetStateAndNetMode();

    void startDelaySplash();

    void autoLogin();

    void cancelLogin();

    String getAppVersion();
}

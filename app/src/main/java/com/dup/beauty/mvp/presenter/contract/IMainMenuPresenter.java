package com.dup.beauty.mvp.presenter.contract;

/**
 * Created by DP on 2016/10/15.
 */
public interface IMainMenuPresenter extends IBasePresenter {

    /**
     * 是否仅wifi联网。
     */
    boolean getNetMode();

    /**
     * 设置网络模式
     *
     * @param isWifiOnly
     */
    boolean changeNetMode(boolean isWifiOnly);

    /**
     * 是否是离线模式
     *
     * @return
     */
    boolean getOfflineMode();

    /**
     * 设置是否是离线模式
     *
     * @return
     */
    boolean changeOfflineMode(boolean isOffline);
}

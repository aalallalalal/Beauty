package com.dup.beauty.presenter.contract;

/**
 * Created by DP on 2016/10/15.
 */
public interface IMainMenuPresenter {

    /**
     * 获取网络模式
     */
    public void getNetMode();

    /**
     * 设置网络模式
     * @param isWifiOnly
     */
    public void changeNetMode(boolean isWifiOnly);
}

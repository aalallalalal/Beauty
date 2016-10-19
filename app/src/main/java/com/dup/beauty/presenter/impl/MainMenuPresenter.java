package com.dup.beauty.presenter.impl;

import android.app.Activity;

import com.dup.beauty.model.util.SPUtil;
import com.dup.beauty.presenter.contract.IMainMenuPresenter;
import com.dup.beauty.view.IMainMenuView;

/**
 * Created by DP on 2016/10/15.
 */
public class MainMenuPresenter implements IMainMenuPresenter {

    private Activity mActivity;
    private IMainMenuView mView;

    public MainMenuPresenter(Activity activity, IMainMenuView view) {
        this.mActivity = activity;
        this.mView = view;
    }

    /**
     * 是否仅wifi联网
     *
     * @return
     */
    @Override
    public boolean getNetMode() {
        boolean isWifiMode = SPUtil.getBoolean(SPUtil.KEY_NET_MODE, false);
        return isWifiMode;
    }

    @Override
    public boolean changeNetMode(boolean isWifiOnly) {
        boolean b = SPUtil.setInfo(SPUtil.KEY_NET_MODE, isWifiOnly);
        return b;
    }

    @Override
    public boolean getOfflineMode() {
        boolean isOfflineMode = SPUtil.getBoolean(SPUtil.KEY_OFFLINE_MODE, false);
        return isOfflineMode;
    }

    @Override
    public boolean changeOfflineMode(boolean isOffline) {
        boolean b = SPUtil.setInfo(SPUtil.KEY_OFFLINE_MODE, isOffline);
        return b;
    }


}

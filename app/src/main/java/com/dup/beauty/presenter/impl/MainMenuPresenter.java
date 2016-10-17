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

    @Override
    public void getNetMode() {
        boolean isWifiMode = SPUtil.getBoolean(SPUtil.KEY_NET_MODE, false);
        mView.onNetMode(isWifiMode);
    }

    @Override
    public void changeNetMode(boolean isWifiOnly) {
        boolean b = SPUtil.setInfo(SPUtil.KEY_NET_MODE, isWifiOnly);
        if (b) {
            mView.onNetMode(isWifiOnly);
        }else {
            mView.onNetMode(!isWifiOnly);
        }
    }
}

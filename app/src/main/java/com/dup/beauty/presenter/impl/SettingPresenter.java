package com.dup.beauty.presenter.impl;

import android.app.Activity;
import android.content.Context;

import com.dup.beauty.presenter.contract.ISettingPresenter;
import com.dup.beauty.util.CacheUtil;
import com.dup.beauty.view.ISettingView;

/**
 * 设置界面的 presenter
 * Created by DP on 2016/10/26.
 */
public class SettingPresenter implements ISettingPresenter {

    private Activity mActivity;
    private ISettingView mView;

    public SettingPresenter(Activity activity, ISettingView view) {
        this.mActivity = activity;
        this.mView = view;
    }

    /**
     * 清空图片本地缓存
     */
    @Override
    public void clearImageCache() {
        CacheUtil.clearImageCache(mActivity);
    }

}

package com.dup.beauty.mvp.presenter.impl;

import android.app.Activity;

import com.dup.beauty.mvp.presenter.contract.ISettingPresenter;
import com.dup.beauty.util.CacheUtil;
import com.dup.beauty.mvp.view.ISettingView;

import javax.inject.Inject;

/**
 * 设置界面的 presenter
 * Created by DP on 2016/10/26.
 */
public class SettingPresenter extends BasePresenter<ISettingView> implements ISettingPresenter {

    private Activity mActivity;

    @Inject
    public SettingPresenter(Activity activity) {
        this.mActivity = activity;
    }

    /**
     * 清空图片本地缓存
     */
    @Override
    public void clearImageCache() {
        CacheUtil.clearImageCache(mActivity);
    }

}

package com.dup.beauty.presenter.impl;

import android.app.Activity;

import com.dup.beauty.R;
import com.dup.beauty.app.Constant;
import com.dup.beauty.model.util.SPUtil;
import com.dup.beauty.model.util.UserUtil;
import com.dup.beauty.presenter.contract.ISplashPresenter;
import com.dup.beauty.util.NetUtil;
import com.dup.beauty.util.T;
import com.dup.beauty.view.ISplashView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by DP on 2016/10/14.
 */
public class SplashPresenter implements ISplashPresenter {

    private Activity activity;
    private ISplashView view;

    public SplashPresenter(Activity activity, ISplashView view) {
        this.activity = activity;
        this.view = view;
    }

    @Override
    public void checkNetStateAndNetMode() {
        int networkType = NetUtil.getNetworkType();
        boolean onlyWifi = SPUtil.getBoolean(SPUtil.KEY_NET_MODE, false);
        boolean isFirstTimeUse = SPUtil.getBoolean(SPUtil.KEY_FIRST_TIME_USE, true);
        view.onGetNetState(networkType, onlyWifi, isFirstTimeUse);
        SPUtil.setInfo(SPUtil.KEY_FIRST_TIME_USE, false);
    }

    @Override
    public void startDelaySplash() {
        Observable.timer(Constant.SPLASH_DELAY, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        view.onSplash();
                    }
                });
    }

    @Override
    public void autoLogin() {
        UserUtil.getInstance().autoLogin(activity, new UserUtil.OnResultListener() {
            @Override
            public void onResult(String message, boolean isSuccess) {
                if (!isSuccess) {
                    T.e(activity, R.string.login_failed);
                }
            }
        });
    }

    @Override
    public void cancelLogin() {
        UserUtil.getInstance().removeResultListener();
    }
}

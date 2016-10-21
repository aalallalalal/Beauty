package com.dup.beauty.presenter.impl;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.dup.beauty.model.util.UserUtil;
import com.dup.beauty.presenter.contract.ILoginRegisterPresenter;
import com.dup.beauty.view.ILoginRegisterView;

/**
 * Created by DP on 2016/10/20.
 */
public class LoginRegisterPresenter implements ILoginRegisterPresenter {
    private Activity mActivity;
    private ILoginRegisterView mView;

    public LoginRegisterPresenter(Activity act, ILoginRegisterView view) {
        this.mActivity = act;
        this.mView = view;
    }

    /**
     * 进行己方登陆或注册
     *
     * @param email      登陆注册都要有
     * @param account    仅注册时必有
     * @param pwd        登陆注册都要有
     * @param confirmPwd 仅注册时必有
     * @param type       类型：1：注册 0:登陆
     */
    @Override
    public void formCommit(@NonNull final String email, @Nullable final String account, @NonNull final String pwd, @Nullable String confirmPwd, @NonNull final int type) {
        if (type == 1) {
            //注册
            UserUtil.register(mActivity, email, account, pwd, confirmPwd, new UserUtil.OnResultListener() {
                @Override
                public void onResult(String message, boolean isSuccess) {
                    mView.onSendFormResult(message, isSuccess);
                }
            });
        } else {
            //登录
            UserUtil.login(mActivity, email, pwd, new UserUtil.OnResultListener() {
                @Override
                public void onResult(String message, boolean isSuccess) {
                    mView.onSendFormResult(message, isSuccess);
                }
            });
        }
    }


}

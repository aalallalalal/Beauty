package com.dup.beauty.mvp.presenter.impl;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.dup.beauty.mvp.model.util.UserUtil;
import com.dup.beauty.mvp.presenter.contract.ILoginRegisterPresenter;
import com.dup.beauty.mvp.view.ILoginRegisterView;

import javax.inject.Inject;

/**
 * Created by DP on 2016/10/20.
 */
public class LoginRegisterPresenter extends BasePresenter<ILoginRegisterView> implements ILoginRegisterPresenter {

    @Inject
    public LoginRegisterPresenter(Context context) {
        super(context);
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
            UserUtil.getInstance().register(getContext(), email, account, pwd, confirmPwd, new UserUtil.OnResultListener() {
                @Override
                public void onResult(String message, boolean isSuccess) {
                    getView().onSendFormResult(message, isSuccess);
                }
            });
        } else {
            //登录
            UserUtil.getInstance().login(getContext(), email, pwd, new UserUtil.OnResultListener() {
                @Override
                public void onResult(String message, boolean isSuccess) {
                    getView().onSendFormResult(message, isSuccess);
                }
            });
        }
    }


}

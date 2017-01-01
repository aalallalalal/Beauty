package com.dup.beauty.mvp.presenter.contract;

/**
 * 登陆注册 presenter
 * Created by DP on 2016/10/20.
 */
public interface ILoginRegisterPresenter  {
    void formCommit(String email, String account, String pwd, String confirmPwd, int type);

}

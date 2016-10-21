package com.dup.beauty.view;

/**
 * Created by DP on 2016/10/20.
 */
public interface ILoginRegisterView {
    /**
     * 请求注册或登陆结果回调
     * @param message
     * @param isSuccess
     */
    void onSendFormResult(String message, boolean isSuccess);
}

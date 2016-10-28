package com.dup.beauty.model.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.dup.beauty.R;
import com.dup.beauty.model.api.ApiClient;
import com.dup.beauty.model.api.ApiDefine;
import com.dup.beauty.model.entity.Oauth;
import com.dup.beauty.model.entity.User;
import com.dup.beauty.util.StringUtil;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.EventBus;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 原本写登陆注册功能是为了收藏后方便用户使用。写完了才发现只有收藏api，没有获取收藏列表数据的api。先割掉收藏功能，注册登陆可以正常使用。
 * Created by DP on 2016/10/20.
 */
public class UserUtil {

    private static UserUtil INSTANCE;

    /**
     * 登陆,注册回调监听
     */
    private OnResultListener mListener;

    public static UserUtil getInstance() {
        if (INSTANCE == null) {
            synchronized (UserUtil.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UserUtil();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 用户登陆/注册成功后,将状态设置为已登录,存储accesstoken,保存用户数据.
     *
     * @param oauth
     * @param name
     * @param account
     * @param pwd
     */
    private void userLoginSuccess(Oauth oauth, String name, @Nullable String account, String pwd) {
        CrashReport.setUserId(name);//如果登陆成功，设置崩溃反馈 用户注册邮箱。方便记录。
        SPUtil.setInfo(SPUtil.KEY_IS_LOGINING, true);
        SPUtil.setInfo(SPUtil.KEY_USER_ACCESS_TOKEN, oauth.getAccessToken());
        User user = new User(0, name, name, account, pwd, "", "", "");
        DBUtil.getInstance().insertUser(user);
        EventBus.getDefault().postSticky(true);
    }

    /**
     * 获取accesstoken
     *
     * @return
     */
    public String getAccToken() {
        return SPUtil.getString(SPUtil.KEY_USER_ACCESS_TOKEN, "");
    }

    public boolean isLogined() {
        return SPUtil.getBoolean(SPUtil.KEY_IS_LOGINING, false);
    }

    public User getCurrUser() {
        return DBUtil.getInstance().queryUser();
    }

    /**
     * 退出登陆。当自动登陆失败时调用
     * <ul>
     * <li>1.清空accesstoken</li>
     * <li>2.将登陆状态设为离线</li>
     * <li>3.清除登陆的用户数据</li>
     * </ul>
     */
    public void loginOut() {
        CrashReport.setUserId("");//如果退出登陆，重置崩溃反馈 用户注册邮箱。
        SPUtil.setInfo(SPUtil.KEY_USER_ACCESS_TOKEN, "");
        SPUtil.setInfo(SPUtil.KEY_IS_LOGINING, false);
        DBUtil.getInstance().deleteUser();
        EventBus.getDefault().postSticky(false);
    }

    /**
     * 自动登陆
     */
    public void autoLogin(Context context, OnResultListener listener) {
        mListener = listener;
        User currUser = getCurrUser();
        if (currUser != null) {
            login(context, currUser.getName(), currUser.getPwd(), mListener);
        }
    }

    /**
     * 注册
     *
     * @param context
     * @param email
     * @param account
     * @param pwd
     * @param confirmPwd
     * @param listener
     */
    public void register(final Context context, @NonNull final String email, @Nullable final String account, @NonNull final String pwd, @Nullable String confirmPwd, final OnResultListener listener) {
        mListener = listener;
        if (judgeRegisterParams(context, email, account, pwd, confirmPwd)) {
            // 注册返回结果
            Observable<Oauth> register = ApiClient.getApiService(context).register(ApiDefine.Client_Id, ApiDefine.Client_Secret,
                    email, account, pwd);
            register.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io()).subscribe(new Observer<Oauth>() {
                @Override
                public void onCompleted() {
                    mListener = null;
                }

                @Override
                public void onError(Throwable e) {
                    sendResult(StringUtil.getStrRes(context, R.string.register_failed), false);
                    mListener = null;
                }

                @Override
                public void onNext(Oauth oauth) {
                    if (oauth != null && oauth.isStatus()) {
                        userLoginSuccess(oauth, email, account, pwd);
                        sendResult(StringUtil.getStrRes(context, R.string.register_success), true);
                    } else {
                        sendResult(oauth.getMsg(), false);
                    }
                }
            });
        }
    }

    /**
     * 登陆
     *
     * @param context
     * @param name     可能为email 、account
     * @param pwd
     * @param listener
     */
    public void login(final Context context, @NonNull final String name, @NonNull final String pwd, final OnResultListener listener) {
        mListener = listener;
        if (judgeLoginParams(context, name, pwd)) {
            //登陆返回结果
            Observable<Oauth> login = ApiClient.getApiService(context).login(ApiDefine.Client_Id, ApiDefine.Client_Secret, name, pwd);
            login.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io()).subscribe(
                    new Observer<Oauth>() {
                        @Override
                        public void onCompleted() {
                            //防止内存泄露
                            mListener = null;
                        }

                        @Override
                        public void onError(Throwable e) {
                            sendResult(StringUtil.getStrRes(context, R.string.login_failed), false);
                            mListener = null;
                        }

                        @Override
                        public void onNext(Oauth oauth) {
                            if (oauth != null && oauth.isStatus()) {
                                userLoginSuccess(oauth, name, name, pwd);
                                sendResult(StringUtil.getStrRes(context, R.string.login_success), true);
                            } else {
                                sendResult(oauth.getMsg(), false);
                            }
                        }
                    });
        }
    }

    /**
     * 判断注册时参数是否正确
     *
     * @param email
     * @param account
     * @param pwd
     * @param confirmPwd
     */
    private boolean judgeRegisterParams(Context context, String email, String account, String pwd, String confirmPwd) {
        boolean isRight;
        if (StringUtil.isEmail(email)) {
            if (account.length() > 0) {
                if (pwd.length() >= 6) {
                    if (pwd.equals(confirmPwd)) {
                        isRight = true;
                    } else {
                        sendResult(StringUtil.getStrRes(context, R.string.pwdconfirm_error), false);
                        isRight = false;
                    }
                } else {
                    sendResult(StringUtil.getStrRes(context, R.string.pwd_error), false);
                    isRight = false;
                }
            } else {
                sendResult(StringUtil.getStrRes(context, R.string.account_error), false);
                isRight = false;
            }
        } else {
            sendResult(StringUtil.getStrRes(context, R.string.email_error), false);
            isRight = false;
        }
        return isRight;
    }

    /**
     * 判断登陆时参数是否正确
     *
     * @param account
     * @param pwd
     */
    private boolean judgeLoginParams(Context context, String account, String pwd) {
        boolean isRight;
        if (!StringUtil.isEmpty(account)) {
            if (pwd.length() >= 6) {
                isRight = true;
            } else {
                sendResult(StringUtil.getStrRes(context, R.string.pwd_error), false);
                isRight = false;
            }
        } else {
            sendResult(StringUtil.getStrRes(context, R.string.account_error), false);
            isRight = false;
        }
        return isRight;
    }

    /**
     * 调用listener。主要用来统一判断非null
     *
     * @param msg
     * @param isSuccess
     */
    private void sendResult(String msg, boolean isSuccess) {
        if (mListener != null) {
            mListener.onResult(msg, isSuccess);
        }
    }

    /**
     * 如果在调用回调接口之前，结束了界面，要调用此方法，防止回调接口导致界面内存泄露。
     * <br>
     *  如果成功调用回调接口，在回调接口调用完成后，将回调设为null，防内存泄露。
     */
    public void removeResultListener() {
        this.mListener = null;
    }

    public interface OnResultListener {
        void onResult(String message, boolean isSuccess);
    }

}

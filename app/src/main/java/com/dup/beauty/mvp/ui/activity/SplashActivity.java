package com.dup.beauty.mvp.ui.activity;

import android.content.Intent;
import android.widget.TextView;

import com.dup.beauty.R;
import com.dup.beauty.app.BaseActivity;
import com.dup.beauty.mvp.presenter.contract.ISplashPresenter;
import com.dup.beauty.mvp.presenter.impl.SplashPresenter;
import com.dup.beauty.mvp.view.ISplashView;
import com.dup.beauty.util.NetUtil;
import com.dup.beauty.util.StringUtil;
import com.dup.beauty.util.T;
import com.dup.changeskin.SkinManager;
import com.jaeger.library.StatusBarUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 闪屏页
 * <ul>
 * <li>{@link ISplashPresenter}</li>
 * <li>{@link SplashPresenter}</li>
 * <li>{@link ISplashView}</li>
 * </ul>
 */
public class SplashActivity extends BaseActivity implements ISplashView {

    @BindView(R.id.splash_version)
    public TextView versionTv;

    @Inject
    public SplashPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected boolean isSupportSwipeBack() {
        return false;
    }

    @Override
    protected void bindViewToPresenters() {
        super.bindViewToPresenters();
        mPresenter.attachView(this);
    }

    @Override
    protected void unBindPresentersView() {
        super.unBindPresentersView();
        mPresenter.clearRef();
    }

    @Override
    protected void initDI() {
        super.initDI();
        mActivityComponent.inject(this);
    }

    @Override
    protected void initData() {
        super.initData();
        ButterKnife.bind(this);
        StatusBarUtil.setColor(this, SkinManager.getInstance().getResourceManager().getColor("status_bar_bg"), 0);
        String versionName = mPresenter.getAppVersion();
        versionTv.setText(StringUtil.getFormatStrRes(this, R.string.version, versionName));
        mPresenter.checkNetStateAndNetMode();
        mPresenter.autoLogin();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.cancelLogin();
    }

    /**
     * 返回网络状态
     *
     * @param state
     * @param onlyWifi       是否仅在wifi下联网  @see com.dup.beauty.util.NetUtil#NETTYPE_CMNET
     * @param isFirstTimeUse 是否第一次使用app
     */
    @Override
    public void onGetNetState(int state, boolean onlyWifi, boolean isFirstTimeUse) {
        switch (state) {
            case NetUtil.NETTYPE_CMNET:
            case NetUtil.NETTYPE_CMWAP:
                if (onlyWifi) {
//                    T.s(this,R.string.mobile_safe);
                } else {
                    if (isFirstTimeUse) {
                        //第一次使用，并且还是以流量打开，给提示
                        T.w(SplashActivity.this, R.string.mobile_warning);
                    }
                }
                break;
            case NetUtil.NETTYPE_WIFI:
                break;
            case NetUtil.NETTYPE_NONE:
                T.s(SplashActivity.this, R.string.no_net);
                break;

        }
        continueSplash();
    }

    /**
     * 延迟结束
     */
    @Override
    public void onSplash() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    /**
     * 延迟跳转
     */
    private void continueSplash() {
        mPresenter.startDelaySplash();
    }

    @Override
    public void onBackPressed() {
    }
}

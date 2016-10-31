package com.dup.beauty.ui.activity;

import android.content.Intent;
import android.widget.TextView;

import com.dup.beauty.R;
import com.dup.beauty.app.BaseActivity;
import com.dup.beauty.model.entity.User;
import com.dup.beauty.model.util.UserUtil;
import com.dup.beauty.presenter.contract.ISplashPresenter;
import com.dup.beauty.presenter.impl.SplashPresenter;
import com.dup.beauty.util.NetUtil;
import com.dup.beauty.util.StringUtil;
import com.dup.beauty.util.T;
import com.dup.beauty.view.ISplashView;
import com.dup.changeskin.SkinManager;
import com.jaeger.library.StatusBarUtil;

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

    private ISplashPresenter presenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected boolean isSupportSwipeBack() {
        return false;
    }

    @Override
    protected void bindPresenters() {
        super.bindPresenters();
        presenter = new SplashPresenter(this, this);
    }

    @Override
    protected void initData() {
        super.initData();
        ButterKnife.bind(this);
        StatusBarUtil.setColor(this, SkinManager.getInstance().getResourceManager().getColor("status_bar_bg"),0);
        String versionName = presenter.getAppVersion();
        versionTv.setText(StringUtil.getFormatStrRes(this,R.string.version,versionName));
        presenter.checkNetStateAndNetMode();
        presenter.autoLogin();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.cancelLogin();
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
        presenter.startDelaySplash();
    }

    @Override
    public void onBackPressed() {
    }
}

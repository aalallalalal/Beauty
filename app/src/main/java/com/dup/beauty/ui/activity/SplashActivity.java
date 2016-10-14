package com.dup.beauty.ui.activity;


import android.content.Intent;

import com.dup.beauty.R;
import com.dup.beauty.app.BaseActivity;
import com.dup.beauty.presenter.contract.ISplashPresenter;
import com.dup.beauty.presenter.impl.SplashPresenter;
import com.dup.beauty.util.NetUtil;
import com.dup.beauty.util.StringUtil;
import com.dup.beauty.view.ISplashView;
import com.sdsmdg.tastytoast.TastyToast;

public class SplashActivity extends BaseActivity implements ISplashView {

    private ISplashPresenter presenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void bindPresenters() {
        super.bindPresenters();
        presenter = new SplashPresenter(this, this);
    }

    @Override
    protected void initData() {
        super.initData();
        presenter.checkNetStateAndNetMode();
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
//                    TastyToast.makeText(this, StringUtil.getStrRes(this, R.string.mobile_safe),
//                            TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                } else {
                    if(isFirstTimeUse){
                        //第一次使用，并且还是以流量打开，给提示
                        TastyToast.makeText(this, StringUtil.getStrRes(this, R.string.mobile_warning),
                                TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                    }
                }
                break;
            case NetUtil.NETTYPE_WIFI:
                break;
            case NetUtil.NETTYPE_NONE:
                TastyToast.makeText(this, StringUtil.getStrRes(this, R.string.no_net),
                            TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
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


}

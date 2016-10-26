package com.dup.beauty.app;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.dup.beauty.R;
import com.dup.beauty.ui.widget.MySlidingPaneLayout;
import com.dup.changeskin.SkinManager;

import java.lang.reflect.Field;


/**
 * 基础activity。
 * 实现状态栏、侧滑结束功能
 * Created by DP on 2016/9/18.
 */
public abstract class BaseActivity extends AppCompatActivity implements MySlidingPaneLayout.PanelSlideListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        initSwipeBackFinish();

        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }

        bindPresenters();
        initView();
        initData();
        initAction();
        SkinManager.getInstance().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SkinManager.getInstance().unregister(this);
    }

    /**
     * 初始化侧滑结束功能
     */
    private void initSwipeBackFinish() {
        if (isSupportSwipeBack()) {
            MySlidingPaneLayout slidingPaneLayout = new MySlidingPaneLayout(this);
            //通过反射改变mOverhangSize的值为0，这个mOverhangSize值为菜单到右边屏幕的最短距离，默认
            //是32dp，现在给它改成0
            try {
                //属性
                Field f_overHang = MySlidingPaneLayout.class.getDeclaredField("mOverhangSize");
                f_overHang.setAccessible(true);
                f_overHang.set(slidingPaneLayout, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            slidingPaneLayout.setPanelSlideListener(this);
            slidingPaneLayout.setSliderFadeColor(getResources().getColor(android.R.color.transparent));

            //1.将一个透明空的view,当做menu.这样就可以露出下面的activity
            View leftView = new View(this);
            leftView.setLayoutParams( new MySlidingPaneLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            slidingPaneLayout.addView(leftView, 0);

            //2.将当前activity先从decorview中移除掉，后加入slidepane作为content，最后将slidepane加入decorview
            ViewGroup decor = (ViewGroup) getWindow().getDecorView();
            ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
            decorChild.setBackgroundColor(getResources().getColor(android.R.color.white));
            decor.removeView(decorChild);
            decor.addView(slidingPaneLayout);
            slidingPaneLayout.addView(decorChild, 1);
        }
    }

    /**
     * 此界面是否支持侧滑结束
     * @return
     */
    protected abstract boolean isSupportSwipeBack();

    protected void bindPresenters() {
    }

    /**
     * 此activity界面layout
     * @return
     */
    @LayoutRes
    protected abstract int getLayoutId();

    protected void initAction() {
    }

    protected void initView() {
    }

    protected void initData() {
    }

    @Override
    public void onPanelOpened(View view) {
        finish();
        this.overridePendingTransition(0, R.anim.slide_out_right);
    }

    @Override
    public void onPanelClosed(View view) {
    }

    @Override
    public void onPanelSlide(View view, float v) {
    }

}

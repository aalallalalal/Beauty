package com.dup.beauty.ui.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dup.beauty.R;
import com.dup.beauty.app.BaseActivity;
import com.dup.beauty.model.entity.Gallery;
import com.dup.beauty.ui.adapter.PictureViewPagerAdapter;
import com.dup.beauty.ui.widget.PicturesViewPager;
import com.dup.beauty.util.L;
import com.dup.beauty.util.StringUtil;
import com.sdsmdg.tastytoast.TastyToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PictureActivity extends BaseActivity implements PicturesViewPager.OnPageSingleTapListener
        , ViewPager.OnPageChangeListener {
    @BindView(R.id.picture_viewpager)
    public PicturesViewPager viewPager;
    @BindView(R.id.toolbar)
    public ViewGroup toolbar;
    @BindView(R.id.toolbar_title)
    public TextView titleTv;

    private Gallery mGallery;
    private int mPosition;
    private PictureViewPagerAdapter mAdapter;

    /**
     * toolbar高度,用来显示隐藏toolbar
     */
    private int toolbarHeight = 0;
    /**
     * 用来显示隐藏toolbar的动画
     */
    private ValueAnimator anim;
    /**
     * 控制动画显隐
     */
    private boolean animShowing = true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_picture;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            //粘性沉浸式
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    protected void initView() {
        super.initView();
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        super.initData();
        Bundle bundle = getIntent().getExtras();
        mGallery = (Gallery) bundle.getSerializable("GALLERY");
        mPosition = bundle.getInt("POSITION", 0);
        if (mGallery == null || mGallery.getList() == null || mGallery.getList().isEmpty()) {
            TastyToast.makeText(this, StringUtil.getStrRes(this, R.string.gallery_error), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            finish();
            return;
        }
        //初始化adpater
        mAdapter = new PictureViewPagerAdapter(this, mGallery.getList());
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(mPosition, false);
        //初始化title
        setToolBarTitle(mPosition);
        //初始化toolbar显示隐藏动画
        initAnimation();

    }

    @Override
    protected void initAction() {
        super.initAction();
        viewPager.setOnSingleTapUpListener(this);
        viewPager.addOnPageChangeListener(this);
    }

    /**
     * 初始化toolbar显示隐藏动画
     */
    private void initAnimation() {
        anim = ValueAnimator.ofFloat(0, 1);
        anim.setDuration(400);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float ratio = (float) valueAnimator.getAnimatedValue();
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
                if (animShowing) {
                    //隐藏
                    params.setMargins(0, (int) (-ratio * toolbarHeight), 0, 0);
                } else {
                    //显示
                    params.setMargins(0, (int) (-(1 - ratio) * toolbarHeight), 0, 0);
                }
                toolbar.setLayoutParams(params);
            }
        });
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                if (!animShowing) {
                    toolbar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (animShowing) {
                    toolbar.setVisibility(View.GONE);
                    animShowing = false;
                } else {
                    animShowing = true;
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
    }

    /**
     * 退回按钮点击事件
     *
     * @param view
     */
    @OnClick(R.id.toolbar_back)
    public void onBackPress(View view) {
        finish();
    }

    /**
     * 下载按钮点击事件
     *
     * @param view
     */
    @OnClick(R.id.toolbar_download)
    public void onDownLoadPress(View view) {
        //TODO 下载图片
    }

    /**
     * 点击viewpager事件.隐藏显示bar
     */
    @Override
    public void onPageClick() {
        toolbarHeight = toolbar.getHeight();
        if (anim == null) {
            return;
        }
        if (anim.isRunning()) {
            return;
        } else {
            anim.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (anim != null) {
            anim.cancel();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        setToolBarTitle(position);
    }

    /**
     * 设置title
     *
     * @param position
     */
    private void setToolBarTitle(int position) {
        String formatStrRes = StringUtil.getFormatStrRes(this, R.string.picture_index, position + 1 + "");
        titleTv.setText(formatStrRes);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }


}

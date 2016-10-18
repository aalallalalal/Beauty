package com.dup.beauty.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.dup.beauty.R;
import com.dup.beauty.app.BaseActivity;
import com.dup.beauty.model.entity.Gallery;
import com.dup.beauty.model.entity.Picture;
import com.dup.beauty.presenter.contract.IGalleryPresenter;
import com.dup.beauty.presenter.impl.GalleryPresenter;
import com.dup.beauty.ui.adapter.PicturesAdapter;
import com.dup.beauty.util.DisplayUtil;
import com.dup.beauty.util.L;
import com.dup.beauty.view.IGalleryView;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * 图库界面，显示图库中图片们
 */
public class GalleryActivity extends BaseActivity implements IGalleryView, PicturesAdapter.OnItemClickListener {
    @BindView(R.id.toolbar_title)
    public TextView titleTv;
    @BindView(R.id.gallery_recyclerview)
    public RecyclerView recyclerView;
    @BindView(R.id.bottom_bar_pre)
    public TextView bottomPre;
    @BindView(R.id.bottom_bar_after)
    public TextView bottomAfter;


    private IGalleryPresenter mPresenter;

    private Gallery mGallery;
    private PicturesAdapter mAdapter;
    private ArrayList<Gallery> mGalleries;
    private int currentPosition = 0;

    private int direction = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_gallery;
    }

    @Override
    protected boolean isSupportSwipeBack() {
        return true;
    }

    @Override
    protected void bindPresenters() {
        mPresenter = new GalleryPresenter(this, this);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
        ButterKnife.bind(GalleryActivity.this);
    }

    @Override
    protected void initData() {
        super.initData();
        Bundle extras = getIntent().getExtras();
        //这里获取到的gallery没有list图片数据
        mGallery = (Gallery) extras.getSerializable("GALLERY");
        mGalleries = (ArrayList<Gallery>) extras.getSerializable("GALLERIES");
        currentPosition = extras.getInt("POSITION");

        if (mGallery == null) {
            finish();
            return;
        }
        //设置上下 bar 标题
        initBarText();

        //获取网络数据
        mPresenter.fetchGalleryWithId(mGallery.getId());
    }

    /**
     * 初始化 bar
     */
    private void initBarText() {
        titleTv.setText(mGalleries.get(currentPosition).getTitle());

        if (mGalleries == null || mGalleries.size() == 0 || mGalleries.size() == 1) {
            bottomPre.setVisibility(View.GONE);
            bottomAfter.setVisibility(View.GONE);
            return;
        }

        if (currentPosition == 0) {
            bottomPre.setVisibility(View.GONE);
            bottomAfter.setVisibility(View.VISIBLE);
            bottomAfter.setText(mGalleries.get(currentPosition + 1).getTitle());
            return;
        }
        if (currentPosition == mGalleries.size() - 1) {
            bottomPre.setVisibility(View.VISIBLE);
            bottomAfter.setVisibility(View.GONE);
            bottomPre.setText(mGalleries.get(currentPosition - 1).getTitle());
            return;
        }

        bottomPre.setVisibility(View.VISIBLE);
        bottomAfter.setVisibility(View.VISIBLE);
        bottomPre.setText(mGalleries.get(currentPosition - 1).getTitle());
        bottomAfter.setText(mGalleries.get(currentPosition + 1).getTitle());
    }

    /**
     * 跳至前一项
     *
     * @param view
     */
    @OnClick(R.id.bottom_bar_pre)
    public void onPrePress(View view) {
        direction = -1;
        currentPosition--;
        mPresenter.fetchGalleryWithId(mGalleries.get(currentPosition).getId());
        initBarText();
    }

    /**
     * 跳至后一项
     *
     * @param view
     */
    @OnClick(R.id.bottom_bar_after)
    public void onAfterPress(View view) {
        direction = 1;
        currentPosition++;
        mPresenter.fetchGalleryWithId(mGalleries.get(currentPosition).getId());
        initBarText();
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

    @OnCheckedChanged(R.id.toolbar_favorite)
    public void OnFavoritePress(CompoundButton compoundButton, boolean checked) {
        //TODO 收藏功能
    }

    @Override
    protected void initAction() {
        super.initAction();
    }

    @Override
    public void onItemClick(int position, Picture picture) {
        Intent intent = new Intent();
        intent.putExtra("GALLERY", mGallery);
        intent.putExtra("POSITION", position);
        intent.setClass(this, PictureActivity.class);
        startActivity(intent);
    }

    @Override
    public void onGalleryWithId(Gallery gallery, long id) {
        //这里获取到的gallery有list图片数据
        mGallery = gallery;

        //设置recyclerview 和 adapter
        LayoutAnimationController lac;
        if(direction>0){
            lac = AnimationUtils.loadLayoutAnimation(this,R.anim.layout_slide_in_right);
        }else {
            lac = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_slide_in_left);
        }

        recyclerView.setLayoutAnimation(lac);
        mAdapter = new PicturesAdapter(this, gallery.getList(), DisplayUtil.getScreenWidthPx(this));
        mAdapter.setItemClickListener(this);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));//解决no adapter warning
        recyclerView.swapAdapter(mAdapter,false);
    }
}

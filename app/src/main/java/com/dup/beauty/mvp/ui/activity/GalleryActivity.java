package com.dup.beauty.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.dup.beauty.R;
import com.dup.beauty.app.BaseActivity;
import com.dup.beauty.mvp.model.entity.Gallery;
import com.dup.beauty.mvp.model.entity.Picture;
import com.dup.beauty.mvp.presenter.contract.IGalleryPresenter;
import com.dup.beauty.mvp.presenter.impl.GalleryPresenter;
import com.dup.beauty.mvp.ui.adapter.PicturesAdapter;
import com.dup.beauty.mvp.ui.widget.ColorSwipeRefreshLayout;
import com.dup.beauty.mvp.view.IGalleryView;
import com.dup.changeskin.SkinManager;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * 图库界面，显示一个图库中图片们
 * <ul>
 * <li>{@link IGalleryPresenter}</li>
 * <li>{@link GalleryPresenter}</li>
 * <li>{@link IGalleryView}</li>
 * </ul>
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
    @BindView(R.id.swipe_refresh)
    public ColorSwipeRefreshLayout refreshLayout;

    @Inject
    public GalleryPresenter mPresenter;


    private Gallery mGallery;
    private PicturesAdapter mAdapter;
    private ArrayList<Gallery> mGalleries;
    private int currentPosition = 0;

    /**
     * 标示此页是从后而来,还是前面的.1:后面,列表项从右滑入.-1:前面,列表项从左滑入.0:没动画
     */
    private int direction = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_gallery;
    }

    @Override
    protected boolean isSupportSwipeBack() {
        return true;
    }

    @Override
    protected void bindViewToPresenters() {
        super.bindViewToPresenters();
        mPresenter.attachView(this);
    }

    @Override
    protected void initDI() {
        super.initDI();
        mActivityComponent.inject(this);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtil.setColor(this, SkinManager.getInstance().getResourceManager().getColor("status_bar_bg"), 0);
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
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                direction = 0;
                mPresenter.fetchGalleryWithId(mGalleries.get(currentPosition).getId());
            }
        });
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
        LayoutAnimationController lac = null;
        if (direction > 0) {
            lac = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_slide_in_right);
        } else if (direction < 0) {
            lac = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_slide_in_left);
        } else {

        }
        recyclerView.setLayoutAnimation(lac);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));//解决no adapter warning

        mAdapter = new PicturesAdapter(this, gallery.getList());
        mAdapter.setItemClickListener(this);
        recyclerView.swapAdapter(mAdapter, false);
    }

    @Override
    public void onDataLoad(boolean isFinish) {
        refreshLayout.setRefreshing(!isFinish);
    }
}

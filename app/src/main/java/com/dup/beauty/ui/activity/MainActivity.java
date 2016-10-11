package com.dup.beauty.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.dup.beauty.R;
import com.dup.beauty.app.BaseActivity;
import com.dup.beauty.app.Constant;
import com.dup.beauty.model.entity.Category;
import com.dup.beauty.model.entity.Gallery;
import com.dup.beauty.presenter.contract.IMainPresenter;
import com.dup.beauty.presenter.impl.MainPresenter;
import com.dup.beauty.ui.adapter.BannerAdapter;
import com.dup.beauty.ui.adapter.CategoriesAdapter;
import com.dup.beauty.ui.adapter.GalleriesAdapter;
import com.dup.beauty.util.Blur;
import com.dup.beauty.util.L;
import com.dup.beauty.view.IMainView;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements IMainView, BGABanner.OnItemClickListener,
        GalleriesAdapter.OnItemClickListener, CategoriesAdapter.OnItemClickListener {

    @BindView(R.id.main_banner)
    public BGABanner banner;
    @BindView(R.id.main_recyclerview_categories)
    public RecyclerView recyclerViewCategories;
    @BindView(R.id.main_recyclerview_hot)
    public XRecyclerView recyclerViewHot;
    @BindView(R.id.main_appbarlayout)
    public AppBarLayout appBarLayout;
    @BindView(R.id.main_blur_iv)
    public ImageView blurImageView;

    private IMainPresenter mMainPresenter;
    private GalleriesAdapter mGalleriesAdapter;

    /**
     * 控制banner的停 动
     */
    private boolean isPlaying = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();
        ButterKnife.bind(MainActivity.this);

        recyclerViewHot.setPullRefreshEnabled(false);
        recyclerViewHot.setLoadingMoreEnabled(true);
        recyclerViewHot.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);
        recyclerViewHot.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
    }

    @Override
    protected void initData() {
        super.initData();

        requestData();
    }


    @Override
    protected void initAction() {
        super.initAction();
        banner.setOnItemClickListener(this);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange() * Constant.BANNER_AUTO_VALVE) {
                    //这里设置Banner_auto_valve的原因是：设置一定的阈值，防止用户快速滑动，导致下面方法很容易就开始执行。
                    if (isPlaying) {
                        banner.stopAutoPlay();
                        isPlaying = false;
                        generateBannerImg();
                    }
                    setBlurImageAlpha(verticalOffset, appBarLayout.getTotalScrollRange());
                } else {
                    if (!isPlaying) {
                        banner.startAutoPlay();
                        isPlaying = true;
                    }
                }
            }
        });

        recyclerViewHot.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
                mMainPresenter.fetchMoreHotImgs();
            }
        });

    }

    /**
     * 设置BlurImageview 透明度
     *
     * @param verticalOffset
     * @param total
     */
    private void setBlurImageAlpha(int verticalOffset, float total) {
        float ratio =
                (Math.abs(verticalOffset) - total * Constant.BANNER_AUTO_VALVE) /
                        (total * (1f - Constant.BANNER_AUTO_VALVE));

        blurImageView.setAlpha(ratio);
    }

    /**
     * 生成banner映像,模糊后放到banner上方的imageview中
     * 由于blur较为耗时,用rx 处理耗时操作后 转入主线程 设置view.
     */
    private void generateBannerImg() {
        banner.setDrawingCacheEnabled(true);
        banner.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        banner.buildDrawingCache();
        final Bitmap drawingCache = banner.getDrawingCache();
        if (drawingCache != null) {
            Observable.create(new Observable.OnSubscribe<Bitmap>() {
                @Override
                public void call(Subscriber<? super Bitmap> subscriber) {
                    Bitmap fastBlur = Blur.fastblur(MainActivity.this.getApplicationContext(), drawingCache, 25);
                    subscriber.onNext(fastBlur);
                    drawingCache.recycle();
                }
            }).subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Bitmap>() {
                        @Override
                        public void call(Bitmap fastBlur) {
                            if (fastBlur != null) {
                                blurImageView.setImageBitmap(fastBlur);
                            }
                            banner.setDrawingCacheEnabled(false);
                        }
                    });
        } else {
            L.e("生成Banner影像 出错!");
        }
    }

    /**
     * 數據请求
     */
    private void requestData() {
        //请求banner图片数据
        mMainPresenter.fetchBannerAndHotImgs();
        //请求分类列表
        mMainPresenter.fetchCatalog();
    }

    /**
     * 绑定presenters
     */
    @Override
    protected void bindPresenters() {
        mMainPresenter = new MainPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    /**
     * 获取到banner图片数据
     *
     * @param listBanner banner图片数据
     * @param listHot    主页hot第一页图片数据
     */
    @Override
    public void onBannerAndHotImgs(ArrayList<Gallery> listBanner, ArrayList<Gallery> listHot) {
        if (listBanner == null || listHot == null) {
            return;
        }

        /*1.设置banner*/
        ArrayList<String> tips = new ArrayList<>();
        for (Gallery gallery : listBanner) {
            tips.add(gallery.getTitle());
        }

        banner.setAdapter(new BannerAdapter(this));
        banner.setData(listBanner, tips);

        /*2.设置hot*/
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mGalleriesAdapter = new GalleriesAdapter(MainActivity.this, listHot, metric.widthPixels);
        mGalleriesAdapter.setItemClickListener(this);
        recyclerViewHot.setAdapter(mGalleriesAdapter);

    }

    /**
     * 获取到图片分类数据
     *
     * @param list 图片分类数据
     */
    @Override
    public void onCategories(List<Category> list) {
        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(this, list);
        categoriesAdapter.setItemClickListener(this);
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(MainActivity.this, OrientationHelper.HORIZONTAL, false));
        recyclerViewCategories.setAdapter(categoriesAdapter);
    }

    /**
     * 获取到更多图片数据
     *
     * @param list 注意是新增图片数据，不是全部图片
     */
    @Override
    public void onMoreHotImgs(List<Gallery> list, int page) {
        recyclerViewHot.loadMoreComplete();
        int nowCount = mGalleriesAdapter.getItemCount();
        mGalleriesAdapter.getData().addAll(list);
        mGalleriesAdapter.notifyItemRangeInserted(nowCount + 1, list.size());//这里加1是考虑进了header
    }

    /**
     * banner 点击回调方法
     *
     * @param banner   广告view
     * @param view     广告item中的view
     * @param model    每item 的数据
     * @param position 点击位置
     */
    @Override
    public void onBannerItemClick(BGABanner banner, View view, Object model, int position) {
        Gallery gallery = (Gallery) model;
        Intent intent = new Intent();
        intent.putExtra("GALLERY", gallery);
        intent.setClass(this, GalleryActivity.class);
        startActivity(intent);
    }

    /**
     * 热图item点击监听事件
     *
     * @param position
     */
    @Override
    public void onItemClick(int position, Gallery gallery) {
        Intent intent = new Intent();
        intent.putExtra("GALLERY", gallery);
        intent.setClass(this, GalleryActivity.class);
        startActivity(intent);
    }

    /**
     * 分类item点击监听事件
     *
     * @param position
     * @param category
     */
    @Override
    public void onItemClick(int position, Category category) {
        Intent intent = new Intent();
        intent.putExtra("CATEGORY", category);
        intent.setClass(this, CategoryActivity.class);
        startActivity(intent);
    }
}

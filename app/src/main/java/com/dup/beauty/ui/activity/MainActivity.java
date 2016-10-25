package com.dup.beauty.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.dup.beauty.R;
import com.dup.beauty.app.BaseActivity;
import com.dup.beauty.app.Constant;
import com.dup.beauty.model.entity.Category;
import com.dup.beauty.model.entity.Gallery;
import com.dup.beauty.model.util.UserUtil;
import com.dup.beauty.presenter.contract.IMainContentPresenter;
import com.dup.beauty.presenter.contract.IMainMenuPresenter;
import com.dup.beauty.presenter.impl.MainContentPresenter;
import com.dup.beauty.presenter.impl.MainMenuPresenter;
import com.dup.beauty.ui.adapter.BannerAdapter;
import com.dup.beauty.ui.adapter.CategoriesAdapter;
import com.dup.beauty.ui.adapter.GalleriesAdapter;
import com.dup.beauty.ui.widget.FunSwitch;
import com.dup.beauty.ui.widget.MySlidingPaneLayout;
import com.dup.beauty.util.Blur;
import com.dup.beauty.util.DisplayUtil;
import com.dup.beauty.util.L;
import com.dup.beauty.view.IMainContentView;
import com.dup.beauty.view.IMainMenuView;
import com.dup.changeskin.SkinManager;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements IMainContentView, IMainMenuView, BGABanner.OnItemClickListener,
        GalleriesAdapter.OnItemClickListener, CategoriesAdapter.OnItemClickListener,
        ColorChooserDialog.ColorCallback {

    @BindView(R.id.sliding_pane)
    public MySlidingPaneLayout slidingPaneLayout;
    //Content
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

    //Menu
    @BindView(R.id.main_menu_only_wifi_switcher)
    public FunSwitch wifiOnlySwitch;
    @BindView(R.id.main_menu_offline_switcher)
    public FunSwitch offlineSwitch;
    @BindView(R.id.main_menu_self_login_register_layout)
    public ViewGroup menuRegisterLayout;
    @BindView(R.id.main_menu_self_logined_layout)
    public ViewGroup menuLoginedLayout;
    @BindView(R.id.main_menu_self_name)
    public TextView menuSelfNameTv;
    @BindView(R.id.main_menu_self_login_out)
    public Button menuSelfLoginOut;

    /**
     * 主界面content的presenter
     */
    private IMainContentPresenter mMainContentPresenter;

    /**
     * 主界面menu的presenter
     */
    private IMainMenuPresenter mMainMenuPresenter;

    private GalleriesAdapter mGalleriesAdapter;


    /**
     * 控制banner轮播 的停 动
     */
    private boolean isPlaying = true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected boolean isSupportSwipeBack() {
        return false;
    }

    @Override
    protected void initView() {
        super.initView();
        ButterKnife.bind(MainActivity.this);
        recyclerViewHot.setPullRefreshEnabled(false);
        recyclerViewHot.setLoadingMoreEnabled(true);
        recyclerViewHot.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);
    }

    @Override
    protected void initData() {
        super.initData();
        EventBus.getDefault().register(this);
        boolean netMode = mMainMenuPresenter.getNetMode();//获取网络模式
        wifiOnlySwitch.setState(netMode);
        boolean offlineMode = mMainMenuPresenter.getOfflineMode();//获取离线模式
        offlineSwitch.setState(offlineMode);

        recyclerViewHot.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mGalleriesAdapter = new GalleriesAdapter(MainActivity.this, DisplayUtil.getScreenWidthPx(this));
        mGalleriesAdapter.setItemClickListener(this);
        recyclerViewHot.setAdapter(mGalleriesAdapter);

        requestData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initAction() {
        super.initAction();

        /**
         * wifionly 开关监听
         */
        wifiOnlySwitch.setStateChangeListener(new FunSwitch.OnStateChangeListener() {
            @Override
            public void onStateChanged(boolean isOpen) {
                boolean b = mMainMenuPresenter.changeNetMode(isOpen);
                if (!b) {
                    isOpen = !isOpen;
                    wifiOnlySwitch.setState(!isOpen);
                }

                if (!isOpen) {
                    //关闭仅wifi联网，重写获取主界面数据
                    requestData();
                }
            }
        });

        /**
         * offline 开关监听
         */
        offlineSwitch.setStateChangeListener(new FunSwitch.OnStateChangeListener() {
            @Override
            public void onStateChanged(boolean isOpen) {
                boolean b = mMainMenuPresenter.changeOfflineMode(isOpen);
                if (!b) {
                    isOpen = !isOpen;
                    offlineSwitch.setState(!isOpen);
                }

                if (!isOpen) {
                    //关闭离线模式，重写获取主界面数据
                    requestData();
                }
            }
        });

        /**
         * 广告栏item点击监听
         */
        banner.setOnItemClickListener(this);

        /**
         * appbar移动距离监听，实现广告图片模糊功能
         */
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

        /**
         * 推荐图片列表上拉下拉刷新监听
         */
        recyclerViewHot.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
                mMainContentPresenter.fetchMoreHotImgs();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (slidingPaneLayout.isOpen()) {
            slidingPaneLayout.closePane();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * menu item 点击事件
     *
     * @param view
     */
    @OnClick({R.id.main_menu_item_setting, R.id.main_menu_item_skin,
            R.id.main_menu_item_download, R.id.main_menu_item_about
            , R.id.main_menu_item_favorite, R.id.main_menu_self_login
            , R.id.main_menu_self_register, R.id.main_menu_self_login_out
    })
    public void clickMenuItem(View view) {
        switch (view.getId()) {
            case R.id.main_menu_item_setting:

                break;

            case R.id.main_menu_item_skin:
                new ColorChooserDialog.Builder(this, R.string.theme_dialog_title)
                        .customColors(R.array.theme_color, null)
                        .doneButton(R.string.done)
                        .cancelButton(R.string.cancel)
                        .allowUserColorInput(false)
                        .allowUserColorInputAlpha(false)
                        .show();
                break;
            case R.id.main_menu_item_download:
                Intent intentDownload = new Intent(this, DownloadActivity.class);
                startActivity(intentDownload);
                break;
            case R.id.main_menu_item_about:
                new MaterialDialog.Builder(this)
                        .title(R.string.about_title)
                        .content(R.string.about_content)
                        .positiveText(R.string.done)
                        .show();
                break;
            case R.id.main_menu_item_favorite:

                break;

            case R.id.main_menu_self_login:
                Intent intent = new Intent(this, LoginRegisterActivity.class);
                intent.putExtra("TYPE", 0);
                startActivity(intent);
                break;

            case R.id.main_menu_self_register:
                Intent intent1 = new Intent(this, LoginRegisterActivity.class);
                intent1.putExtra("TYPE", 1);
                startActivity(intent1);
                break;
            case R.id.main_menu_self_login_out:
                UserUtil.loginOut();
                break;
        }


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
        mMainContentPresenter.fetchBannerAndHotImgs();
        //请求分类列表
        mMainContentPresenter.fetchCatalog();
    }

    /**
     * 绑定presenters
     */
    @Override
    protected void bindPresenters() {
        mMainContentPresenter = new MainContentPresenter(this, this);
        mMainMenuPresenter = new MainMenuPresenter(this, this);
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
        mGalleriesAdapter.setData(listHot);
        mGalleriesAdapter.notifyDataSetChanged();
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
        intent.putExtra("POSITION", position);
        intent.putExtra("GALLERIES", mMainContentPresenter.getBannerGalleries());
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
        intent.putExtra("POSITION", position - 1);//考虑header
        intent.putExtra("GALLERIES", mMainContentPresenter.getHotGalleries());
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
        intent.putExtra("POSITION", position);
        intent.putExtra("CATEGORIES", mMainContentPresenter.getCategory());
        intent.setClass(this, CategoryActivity.class);
        startActivity(intent);
    }

    /**
     * 选择主题色后 dialog的回调
     *
     * @param dialog
     * @param selectedColor
     */
    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor) {

        if (selectedColor == getResources().getColor(R.color.status_bar_bg)) {
            SkinManager.getInstance().removeAnySkin();
        } else if (selectedColor == getResources().getColor(R.color.status_bar_bg_black)) {
            SkinManager.getInstance().changeSkin("black");
        } else if (selectedColor == getResources().getColor(R.color.status_bar_bg_green)) {
            SkinManager.getInstance().changeSkin("green");
        } else if (selectedColor == getResources().getColor(R.color.status_bar_bg_pink)) {
            SkinManager.getInstance().changeSkin("pink");
        } else if (selectedColor == getResources().getColor(R.color.status_bar_bg_purple)) {
            SkinManager.getInstance().changeSkin("purple");
        } else if (selectedColor == getResources().getColor(R.color.status_bar_bg_red)) {
            SkinManager.getInstance().changeSkin("red");
        }
        //这里需要清空recyclerview的缓存view，否则缓存的item不会改变颜色。这就不清空了，影响性能。
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onUserStateChangedEvent(Boolean isUserLogining) {
        //接收用户登陆状态改变监听
        if (isUserLogining) {
            menuLoginedLayout.setVisibility(View.VISIBLE);
            menuRegisterLayout.setVisibility(View.GONE);
            menuSelfNameTv.setText(UserUtil.getCurrUser().getName());
        } else {
            menuLoginedLayout.setVisibility(View.GONE);
            menuRegisterLayout.setVisibility(View.VISIBLE);
        }
    }
}

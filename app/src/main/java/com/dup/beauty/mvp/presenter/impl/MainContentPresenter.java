package com.dup.beauty.mvp.presenter.impl;

import android.app.Activity;
import android.content.Context;

import com.dup.beauty.R;
import com.dup.beauty.app.Constant;
import com.dup.beauty.mvp.model.api.ApiClient;
import com.dup.beauty.mvp.model.entity.Categories;
import com.dup.beauty.mvp.model.entity.Category;
import com.dup.beauty.mvp.model.entity.Galleries;
import com.dup.beauty.mvp.model.entity.Gallery;
import com.dup.beauty.mvp.model.util.DBUtil;
import com.dup.beauty.mvp.model.util.RUtil;
import com.dup.beauty.mvp.presenter.contract.IMainContentPresenter;
import com.dup.beauty.util.L;
import com.dup.beauty.util.T;
import com.dup.beauty.mvp.view.IMainContentView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by DP on 2016/9/18.
 */
public class MainContentPresenter extends BasePresenter<IMainContentView> implements IMainContentPresenter {


    //主界面推荐列表
    private ArrayList<Gallery> hotGalleries;
    //主界面轮播列表
    private ArrayList<Gallery> bannerGalleries;
    //主界面分类列表
    private ArrayList<Category> categoryList;
    //记录该加载第几页了
    private int pageNum = 2;

    @Inject
    public MainContentPresenter(Context context) {
        super(context);
    }


    /**
     * 获取banner数据
     */
    @Override
    public void fetchBannerAndHotImgs() {
        //1.如果banner数据存在，则不进行网络请求，直接加载
        if (bannerGalleries != null && bannerGalleries.size() == Constant.BANNER_NUM) {
            getView().onBannerAndHotImgs(bannerGalleries, hotGalleries);
            L.d("从内存中 获取banner和hot图片数据 成功");
            return;
        }

        //2.如果banner数据不存在,但hot列表存在,则筛选数据后加载
        if (hotGalleries != null && hotGalleries.size() >= Constant.BANNER_NUM) {
            bannerGalleries = new ArrayList<>();
            Observable.from(hotGalleries).take(Constant.BANNER_NUM).subscribe(new Action1<Gallery>() {
                @Override
                public void call(Gallery gallery) {
                    bannerGalleries.add(gallery);
                }
            });

            getView().onBannerAndHotImgs(bannerGalleries, hotGalleries);
            L.d("从Hot列表中 获取banner和hot图片数据 成功");
            return;
        }

        //3.没有缓存存在,则直接网络请求加载
        ApiClient.getApiService(getContext()).getGalleries(1, Constant.PAGE_COUNT, 0)
                .map(new Func1<Galleries, ArrayList<Gallery>>() {
                    @Override
                    public ArrayList<Gallery> call(final Galleries galleries) {
                        hotGalleries = galleries.getGalleries();

                        //根据浏览数筛选出banner数据
                        ArrayList<Gallery> tempList = new ArrayList<>();
                        tempList.addAll(galleries.getGalleries());

                        Comparator<Gallery> comparator = new Comparator<Gallery>() {
                            @Override
                            public int compare(Gallery gallery, Gallery t1) {
                                if (gallery.getCount() >= t1.getCount()) {
                                    return -1;
                                } else {
                                    return 1;
                                }
                            }
                        };

                        Collections.sort(tempList, comparator);

                        final ArrayList<Gallery> list = new ArrayList<>();
                        Observable.from(tempList).take(Constant.BANNER_NUM).subscribe(new Action1<Gallery>() {
                            @Override
                            public void call(Gallery gallery) {
                                list.add(gallery);
                            }
                        });

                        return list;
                    }
                })
                .compose(RUtil.<ArrayList<Gallery>>threadTrs())
                .subscribe(new Observer<ArrayList<Gallery>>() {
                    @Override
                    public void onCompleted() {
                        L.d("从网络 获取banner和hot图片数据 成功");
                        getView().onBannerAndHotImgs(bannerGalleries, hotGalleries);
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.e("从网络 获取banner和hot图片数据失败." + e.getMessage());
                        T.e(getContext().getApplicationContext(), R.string.img_error);
                    }

                    @Override
                    public void onNext(ArrayList<Gallery> list) {
                        bannerGalleries = list;
                    }
                });

    }

    /**
     * 获取分类数据
     */
    @Override
    public void fetchCatalog() {
        //1.缓存信息
        if (categoryList != null && categoryList.size() > 0) {
            getView().onCategories(categoryList);
            L.d("从内存 获取分类列表数据 成功");
            return;
        }

        //2.数据库获取信息
        categoryList = (ArrayList<Category>) DBUtil.getInstance().queryCategoryList();
        if (categoryList != null && categoryList.size() > 0) {
            getView().onCategories(categoryList);
            L.d("从数据库 获取分类列表数据 成功");
            return;
        }

        //3.如果数据库数据不存在,则网络请求.并存入数据库
        ApiClient.getApiService(getContext()).getCategories()
                .compose(RUtil.<Categories>threadTrs())
                .subscribe(new Observer<Categories>() {
                    @Override
                    public void onCompleted() {
                        L.d("从网络  获取分类列表数据 成功");
                        //1.放入数据库
                        DBUtil.getInstance().insertCategoryList(categoryList);
                        //2.回调ui
                        getView().onCategories(categoryList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.e("从网络 获取分类列表数据失败." + e.getMessage());
                        T.e(getContext().getApplicationContext(), R.string.category_error);
                    }

                    @Override
                    public void onNext(Categories categories) {
                        categoryList = categories.getCategories();
                    }
                });

    }

    /**
     * 获取下一页更多hot图片
     */
    @Override
    public void fetchMoreHotImgs() {
        ApiClient.getApiService(getContext()).getGalleries(pageNum, Constant.PAGE_COUNT, 0)
                .compose(RUtil.<Galleries>threadTrs())
                .subscribe(new Observer<Galleries>() {
                    @Override
                    public void onCompleted() {
                        pageNum++;
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.e("从网络 获取更多数据失败." + e.getMessage());
                        T.e(getContext().getApplicationContext(), R.string.loadmore_error);
                    }

                    @Override
                    public void onNext(Galleries galleries) {
                        getView().onMoreHotImgs(galleries.getGalleries(), pageNum);
                    }
                });
    }

    @Override
    public ArrayList<Gallery> getBannerGalleries() {
        if (bannerGalleries != null) {
            return bannerGalleries;
        }
        return null;
    }

    @Override
    public ArrayList<Gallery> getHotGalleries() {
        if (hotGalleries != null) {
            return hotGalleries;
        }
        return null;
    }

    @Override
    public ArrayList<Category> getCategory() {
        if (categoryList != null) {
            return categoryList;
        }
        return null;
    }
}

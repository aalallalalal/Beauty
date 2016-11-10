package com.dup.beauty.presenter.impl;

import android.app.Activity;

import com.dup.beauty.R;
import com.dup.beauty.app.Constant;
import com.dup.beauty.model.api.ApiClient;
import com.dup.beauty.model.entity.Galleries;
import com.dup.beauty.model.entity.Gallery;
import com.dup.beauty.model.util.RUtil;
import com.dup.beauty.presenter.contract.ICategoryPresenter;
import com.dup.beauty.util.DialogUtil;
import com.dup.beauty.util.L;
import com.dup.beauty.util.T;
import com.dup.beauty.view.ICategoryView;

import java.util.ArrayList;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by DP on 2016/9/18.
 */
public class CategoryPresenter implements ICategoryPresenter {
    private Activity mActivity;
    private ICategoryView mCategoryView;

    //记录该加载第几页了
    private int pageNum = 2;
    //记录分类id
    private long id = 0;

    private ArrayList<Gallery> mData = new ArrayList<>();

    public CategoryPresenter(Activity activity, ICategoryView categoryView) {
        this.mActivity = activity;
        this.mCategoryView = categoryView;
    }

    @Override
    public void fetchGalleriesWithId(final long id) {
        ApiClient.getApiService(mActivity).getGalleries(1, Constant.PAGE_COUNT, id)
                .compose(RUtil.<Galleries>threadTrs(new Action0() {
                    @Override
                    public void call() {
                        mCategoryView.onDataLoad(false);
                    }
                }))
                .subscribe(new RUtil.DialogObserver<Galleries>() {

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        pageNum++;
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        L.e("从网络 获取该分类 图库数据失败." + e.getMessage());
                        T.e(mActivity.getApplicationContext(), R.string.gallery_error);
                    }

                    @Override
                    public void onNext(Galleries galleries) {
                        mData.addAll(galleries.getGalleries());
                        mCategoryView.onGalleriesWithId(mData, pageNum, id);
                    }

                    @Override
                    protected void dismissDialog() {
                        mCategoryView.onDataLoad(true);
                    }
                });
    }

    @Override
    public void fetchMoreGalleriesWithId() {
        ApiClient.getApiService(mActivity).getGalleries(pageNum, Constant.PAGE_COUNT, 0)
                .compose(RUtil.<Galleries>threadTrs())
                .subscribe(new Observer<Galleries>() {
                    @Override
                    public void onCompleted() {
                        pageNum++;
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.e("从网络 获取该分类 更多图库数据失败." + e.getMessage());
                        T.e(mActivity.getApplicationContext(), R.string.loadmore_error);
                    }

                    @Override
                    public void onNext(Galleries galleries) {
                        mCategoryView.onMoreGalleriesWithId(galleries.getGalleries(), pageNum);
                    }
                });
    }

    @Override
    public ArrayList<Gallery> getGalleries() {
        return mData;
    }

}

package com.dup.beauty.mvp.presenter.impl;

import android.content.Context;

import com.dup.beauty.R;
import com.dup.beauty.app.Constant;
import com.dup.beauty.mvp.model.api.ApiClient;
import com.dup.beauty.mvp.model.entity.Galleries;
import com.dup.beauty.mvp.model.entity.Gallery;
import com.dup.beauty.mvp.model.util.RUtil;
import com.dup.beauty.mvp.presenter.contract.ICategoryPresenter;
import com.dup.beauty.mvp.view.ICategoryView;
import com.dup.beauty.util.L;
import com.dup.beauty.util.T;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Observer;
import rx.functions.Action0;

/**
 * Created by DP on 2016/9/18.
 */
public class CategoryPresenter extends BasePresenter<ICategoryView> implements ICategoryPresenter {

    //记录该加载第几页了
    private int pageNum = 2;
    //记录分类id
    private long id = 0;

    private ArrayList<Gallery> mData = new ArrayList<>();

    @Inject
    public CategoryPresenter(Context context) {
        super(context);
    }

    @Override
    public void fetchGalleriesWithId(final long id) {
        ApiClient.getApiService(getContext()).getGalleries(1, Constant.PAGE_COUNT, id)
                .compose(RUtil.<Galleries>threadTrs(new Action0() {
                    @Override
                    public void call() {
                        getView().onDataLoad(false);
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
                        T.e(getContext().getApplicationContext(), R.string.gallery_error);
                    }

                    @Override
                    public void onNext(Galleries galleries) {
                        mData.addAll(galleries.getGalleries());
                        getView().onGalleriesWithId(mData, pageNum, id);
                    }

                    @Override
                    protected void dismissDialog() {
                        getView().onDataLoad(true);
                    }
                });
    }

    @Override
    public void fetchMoreGalleriesWithId() {
        ApiClient.getApiService(getContext()).getGalleries(pageNum, Constant.PAGE_COUNT, 0)
                .compose(RUtil.<Galleries>threadTrs())
                .subscribe(new Observer<Galleries>() {
                    @Override
                    public void onCompleted() {
                        pageNum++;
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.e("从网络 获取该分类 更多图库数据失败." + e.getMessage());
                        T.e(getContext().getApplicationContext(), R.string.loadmore_error);
                    }

                    @Override
                    public void onNext(Galleries galleries) {
                        getView().onMoreGalleriesWithId(galleries.getGalleries(), pageNum);
                    }
                });
    }

    @Override
    public ArrayList<Gallery> getGalleries() {
        return mData;
    }

}

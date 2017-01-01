package com.dup.beauty.mvp.presenter.impl;

import android.app.Activity;
import android.content.Context;

import com.dup.beauty.R;
import com.dup.beauty.mvp.model.api.ApiClient;
import com.dup.beauty.mvp.model.entity.Gallery;
import com.dup.beauty.mvp.model.util.RUtil;
import com.dup.beauty.mvp.presenter.contract.IGalleryPresenter;
import com.dup.beauty.util.L;
import com.dup.beauty.util.T;
import com.dup.beauty.mvp.view.IGalleryView;

import javax.inject.Inject;

import rx.functions.Action0;

/**
 * Created by DP on 2016/9/18.
 */
public class GalleryPresenter extends BasePresenter<IGalleryView> implements IGalleryPresenter {

    @Inject
    public GalleryPresenter(Context context) {
        super(context);
    }

    /**
     * 点击item，获取次item图库中的 图片们
     */
    @Override
    public void fetchGalleryWithId(final long id) {
        ApiClient.getApiService(getContext()).getPictures(id)
                .compose(RUtil.<Gallery>threadTrs(new Action0() {
                    @Override
                    public void call() {
                        getView().onDataLoad(false);
                    }
                }))
                .subscribe(new RUtil.DialogObserver<Gallery>() {
                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        L.d("从网络  获取 图库 " + id + " 成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        L.e("从网络 获取 图库 " + id + " 失败." + e.getMessage());
                        T.e(getContext().getApplicationContext(), R.string.gallery_error);
                    }

                    @Override
                    public void onNext(Gallery gallery) {
                        getView().onGalleryWithId(gallery, id);
                    }

                    protected void dismissDialog() {
                        getView().onDataLoad(true);
                    }
                });
    }
}

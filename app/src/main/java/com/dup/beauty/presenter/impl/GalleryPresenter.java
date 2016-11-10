package com.dup.beauty.presenter.impl;

import android.app.Activity;

import com.dup.beauty.R;
import com.dup.beauty.model.api.ApiClient;
import com.dup.beauty.model.entity.Gallery;
import com.dup.beauty.model.util.RUtil;
import com.dup.beauty.presenter.contract.IGalleryPresenter;
import com.dup.beauty.util.L;
import com.dup.beauty.util.T;
import com.dup.beauty.view.IGalleryView;

import rx.functions.Action0;

/**
 * Created by DP on 2016/9/18.
 */
public class GalleryPresenter implements IGalleryPresenter {
    private Activity mActivity;
    private IGalleryView mGalleryView;

    public GalleryPresenter(Activity activity, IGalleryView galleryView) {
        this.mActivity = activity;
        this.mGalleryView = galleryView;
    }

    /**
     * 点击item，获取次item图库中的 图片们
     */
    @Override
    public void fetchGalleryWithId(final long id) {
        ApiClient.getApiService(mActivity).getPictures(id)
                .compose(RUtil.<Gallery>threadTrs(new Action0() {
                    @Override
                    public void call() {
                        mGalleryView.onDataLoad(false);
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
                        T.e(mActivity.getApplicationContext(), R.string.gallery_error);
                    }

                    @Override
                    public void onNext(Gallery gallery) {
                        mGalleryView.onGalleryWithId(gallery, id);
                    }

                    protected void dismissDialog() {
                        mGalleryView.onDataLoad(true);
                    }
                });
    }
}

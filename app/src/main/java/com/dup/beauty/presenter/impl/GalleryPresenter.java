package com.dup.beauty.presenter.impl;

import android.app.Activity;

import com.dup.beauty.R;
import com.dup.beauty.model.api.ApiClient;
import com.dup.beauty.model.entity.Gallery;
import com.dup.beauty.presenter.contract.IGalleryPresenter;
import com.dup.beauty.util.L;
import com.dup.beauty.util.StringUtil;
import com.dup.beauty.view.IGalleryView;
import com.sdsmdg.tastytoast.TastyToast;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
        ApiClient.getApiService(mActivity).getPictures(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<Gallery>() {
                    @Override
                    public void onCompleted() {
                        L.d("从网络  获取 图库 " + id + " 成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.e("从网络 获取 图库 " + id + " 失败." + e.getMessage());
                        TastyToast.makeText(mActivity.getApplicationContext(),
                                StringUtil.getStrRes(mActivity.getApplicationContext(), R.string.gallery_error)
                                , TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                    }

                    @Override
                    public void onNext(Gallery gallery) {
                        mGalleryView.onGalleryWithId(gallery, id);
                    }
                });
    }
}

package com.dup.beauty.presenter.impl;

import android.app.Activity;

import com.dup.beauty.presenter.contract.IGalleryPresenter;
import com.dup.beauty.view.IGalleryView;

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
}

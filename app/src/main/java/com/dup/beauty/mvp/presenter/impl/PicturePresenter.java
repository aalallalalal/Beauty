package com.dup.beauty.mvp.presenter.impl;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.dup.beauty.R;
import com.dup.beauty.mvp.model.api.ApiDefine;
import com.dup.beauty.mvp.model.entity.Gallery;
import com.dup.beauty.mvp.model.util.DownLoadUtil;
import com.dup.beauty.mvp.presenter.contract.IPicturePresenter;
import com.dup.beauty.mvp.view.IPictureView;
import com.dup.beauty.util.T;
import com.dup.beauty.util.UMShareUtil;

import javax.inject.Inject;


/**
 * Created by DP on 2016/10/26.
 */
public class PicturePresenter extends BasePresenter<IPictureView> implements IPicturePresenter {

    @Inject
    public PicturePresenter(Context context) {
        super(context);
    }

    @Override
    public void shareNetImage(Gallery gallery, int position) {
        String url = ApiDefine.getImageUrlWithNoSize(gallery.getList().get(position).getSrc());
        T.i(getContext(), R.string.waiting_load_share);
        Glide.with(getContext()).load(url).asBitmap().priority(Priority.IMMEDIATE).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                UMShareUtil.getInstance().openSharePane((Activity) getContext(), resource);
            }
        });
    }

    @Override
    public void downloadImage(Gallery gallery, int position) {
        T.i(getContext().getApplicationContext(), R.string.start_download);
        final String fileName = gallery.getTitle() + "(" + (position + 1) + ").jpg";
        String url = ApiDefine.getImageUrlWithNoSize(gallery.getList().get(position).getSrc());
        DownLoadUtil.getInstance().downloadImage(getContext().getApplicationContext(), url,
                fileName, new DownLoadUtil.OnImageDownLoadListener() {
                    @Override
                    public void onDownLoadSuccess() {
                        getView().onDownloadResult(true, fileName);
                    }

                    @Override
                    public void onDownLoadFailed() {
                        getView().onDownloadResult(false, fileName);
                    }
                }
        );
    }
}

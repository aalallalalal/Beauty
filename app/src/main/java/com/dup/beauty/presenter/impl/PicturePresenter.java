package com.dup.beauty.presenter.impl;

import android.app.Activity;

import com.dup.beauty.R;
import com.dup.beauty.model.api.ApiDefine;
import com.dup.beauty.model.entity.Gallery;
import com.dup.beauty.model.util.DownLoadUtil;
import com.dup.beauty.presenter.contract.IPicturePresenter;
import com.dup.beauty.util.T;
import com.dup.beauty.util.UMShareUtil;
import com.dup.beauty.view.IPictureView;


/**
 * Created by DP on 2016/10/26.
 */
public class PicturePresenter implements IPicturePresenter {
    private Activity mActivity;
    private IPictureView mView;

    public PicturePresenter(Activity activity, IPictureView view) {
        this.mActivity = activity;
        this.mView = view;
    }

    @Override
    public void shareNetImage(Gallery gallery, int position) {
        String url = ApiDefine.getImageUrlWithNoSize(gallery.getList().get(position).getSrc());
        UMShareUtil.getInstance().openSharePane(mActivity, url);
    }

    @Override
    public void downloadImage(Gallery gallery, int position) {
        T.i(mActivity.getApplicationContext(),R.string.start_download);
        final String fileName = gallery.getTitle() + "(" + (position + 1) + ").jpg";
        String url = ApiDefine.getImageUrlWithNoSize(gallery.getList().get(position).getSrc());
        DownLoadUtil.getInstance().downloadImage(mActivity.getApplicationContext(), url,
                fileName, new DownLoadUtil.OnImageDownLoadListener() {
                    @Override
                    public void onDownLoadSuccess() {
                        mView.onDownloadResult(true,fileName);
                    }

                    @Override
                    public void onDownLoadFailed() {
                        mView.onDownloadResult(false,fileName);
                    }
                }
        );
    }
}

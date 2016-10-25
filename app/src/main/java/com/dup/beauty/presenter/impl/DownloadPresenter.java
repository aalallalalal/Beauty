package com.dup.beauty.presenter.impl;

import com.dup.beauty.model.util.DownLoadUtil;
import com.dup.beauty.presenter.contract.IDownloadPresenter;
import com.dup.beauty.ui.activity.DownloadActivity;
import com.dup.beauty.view.IDownloadView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by DP on 2016/10/24.
 */
public class DownloadPresenter implements IDownloadPresenter {

    private DownloadActivity mActivity;
    private IDownloadView mView;

    public DownloadPresenter(DownloadActivity activity, IDownloadView view) {
        this.mActivity = activity;
        this.mView = view;
    }

    /**
     * 获取下载的图片列表uri
     */
    @Override
    public void fetchDownloadImages() {
        DownLoadUtil.getInstance().fetchDownloadImages(mActivity.getApplicationContext(), new DownLoadUtil.OnDownloadImagesFetchListener() {
            @Override
            public void onFetchImages(ArrayList<File> list) {
                mView.onFetchDownloadImages(list);
            }
        });
    }
}

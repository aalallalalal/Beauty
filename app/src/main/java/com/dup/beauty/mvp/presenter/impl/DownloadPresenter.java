package com.dup.beauty.mvp.presenter.impl;

import android.app.Activity;

import com.dup.beauty.mvp.model.util.DownLoadUtil;
import com.dup.beauty.mvp.presenter.contract.IDownloadPresenter;
import com.dup.beauty.mvp.view.IDownloadView;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by DP on 2016/10/24.
 */
public class DownloadPresenter extends BasePresenter<IDownloadView> implements IDownloadPresenter {

    private Activity mActivity;

    @Inject
    public DownloadPresenter(Activity activity) {
        this.mActivity = activity;
    }

    /**
     * 获取下载的图片列表uri
     */
    @Override
    public void fetchDownloadImages() {
        view.onDataLoad(false);
        DownLoadUtil.getInstance().fetchDownloadImages(mActivity.getApplicationContext(), new DownLoadUtil.OnDownloadImagesFetchListener() {
            @Override
            public void onFetchImages(ArrayList<File> list) {
                view.onFetchDownloadImages(list);
                view.onDataLoad(true);
            }
        });
    }
}

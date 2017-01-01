package com.dup.beauty.mvp.presenter.contract;

import com.dup.beauty.mvp.model.entity.Gallery;

/**
 * 网络 大图查看界面的presenter
 * Created by DP on 2016/10/26.
 */
public interface IPicturePresenter {
    /**
     * 分享网络图片
     */
    void shareNetImage(Gallery gallery, int position);

    /**
     * 下载图片
     */
    void downloadImage(Gallery gallery, int position);

}

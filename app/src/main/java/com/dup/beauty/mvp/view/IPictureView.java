package com.dup.beauty.mvp.view;

/**
 * Created by DP on 2016/10/26.
 */
public interface IPictureView extends IBaseView {
    void onDownloadResult(boolean isSuccess, String fileName);
}

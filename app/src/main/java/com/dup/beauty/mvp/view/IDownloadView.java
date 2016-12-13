package com.dup.beauty.mvp.view;


import java.io.File;
import java.util.ArrayList;

/**
 * 下载图片 查看界面view
 * Created by DP on 2016/10/24.
 */
public interface IDownloadView extends IBaseView {
    void onFetchDownloadImages(ArrayList<File> list);

    /**
     * 数据加载提示
     *
     * @param isFinish true:关闭等待层
     *                 false:打开等待层
     */
    void onDataLoad(boolean isFinish);
}

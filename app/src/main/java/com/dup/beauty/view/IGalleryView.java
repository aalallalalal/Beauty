package com.dup.beauty.view;

import com.dup.beauty.model.entity.Gallery;

/**
 * 图库界面 GalleryActivity 的view
 * Created by DP on 2016/9/18.
 */
public interface IGalleryView {
    /**
     * 根据galleryID获取到此带图片列表数据的图库数据
     * @param gallery 带图片们数据
     * @param id
     */
    void onGalleryWithId(Gallery gallery, long id);

    /**
     * 数据加载提示
     * @param isFinish
     * true:关闭等待层
     * false:打开等待层
     */
    void onDataLoad(boolean isFinish);
}

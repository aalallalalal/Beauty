package com.dup.beauty.mvp.view;

import com.dup.beauty.mvp.model.entity.Gallery;

import java.util.List;

/**
 * 分类 图库界面 CategoryActivity 的view
 * Created by DP on 2016/9/18.
 */
public interface ICategoryView extends IBaseView {
    /**
     * 根据分类ID获取到此分类的图库第一页数据
     *
     * @param galleries 带图片们数据
     * @param page      第几页
     * @param id
     */
    void onGalleriesWithId(List<Gallery> galleries, int page, long id);

    /**
     * 根据分类ID获取到此分类的图库更多页数据
     *
     * @param galleries 带图片们数据
     * @param page      第几页
     */
    void onMoreGalleriesWithId(List<Gallery> galleries, int page);

    /**
     * 数据加载提示
     *
     * @param isFinish true:关闭等待层
     *                 false:打开等待层
     */
    void onDataLoad(boolean isFinish);

}

package com.dup.beauty.presenter.contract;

import com.dup.beauty.model.entity.Gallery;

import java.util.ArrayList;

/**
 * 分类 图库界面CategoryActivity 的presenter
 * Created by DP on 2016/9/18.
 */
public interface ICategoryPresenter {
    /**
     * 点击分类item，获取此分类的第一页的图库们
     */
    void fetchGalleriesWithId(long id);

    /**
     * 获取分类 下一页 图库们
     */
    void fetchMoreGalleriesWithId();

    /**
     * 获取当前获取到的galleries
     * @return
     */
    ArrayList<Gallery> getGalleries();

}

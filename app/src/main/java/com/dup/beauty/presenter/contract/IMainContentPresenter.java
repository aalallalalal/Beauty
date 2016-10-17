package com.dup.beauty.presenter.contract;

import com.dup.beauty.model.entity.Category;
import com.dup.beauty.model.entity.Gallery;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面MainActivity 的presenter
 * Created by DP on 2016/9/18.
 */
public interface IMainContentPresenter {

    /**
     * 获取banner数据
     */
    void fetchBannerAndHotImgs();

    /**
     * 获取分类列表数据
     */
    void fetchCatalog();

    /**
     *  获取首页hot图片数据
     */
    void fetchMoreHotImgs();

    /**
     * 获取banner的数据
     * @return
     */
    ArrayList<Gallery> getBannerGalleries();

    /**
     * 获取首页推荐数据
     * @return
     */
    ArrayList<Gallery> getHotGalleries();

    /**
     * 获取到分类列表
     * @return
     */
    ArrayList<Category> getCategory();


}

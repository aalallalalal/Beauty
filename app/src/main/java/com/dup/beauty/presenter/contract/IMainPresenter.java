package com.dup.beauty.presenter.contract;

/**
 * 主界面MainActivity 的presenter
 * Created by DP on 2016/9/18.
 */
public interface IMainPresenter {

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

}

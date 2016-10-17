package com.dup.beauty.view;

import com.dup.beauty.model.entity.Category;
import com.dup.beauty.model.entity.Gallery;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面 MainActivity 的view
 * Created by DP on 2016/9/18.
 */
public interface IMainContentView {

    void onBannerAndHotImgs(ArrayList<Gallery> listBanner,ArrayList<Gallery> listHot);

    void onCategories(List<Category> list);

    void onMoreHotImgs(List<Gallery> list,int page);

}

package com.dup.beauty.view;

import com.dup.beauty.model.entity.Category;
import com.dup.beauty.model.entity.Gallery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DP on 2016/9/18.
 */
public interface IMainView {

    void onBannerAndHotImgs(ArrayList<Gallery> listBanner,ArrayList<Gallery> listHot);

    void onCategories(List<Category> list);

    void onMoreHotImgs(List<Gallery> list,int page);

}

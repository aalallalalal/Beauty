package com.dup.beauty.presenter.contract;

/**
 * 图库界面GalleryActivity 的presenter
 * Created by DP on 2016/9/18.
 */
public interface IGalleryPresenter {
    /**
     * 点击图库item，获取次item图库中的 图片们
     */
    void fetchGalleryWithId(long id);
}

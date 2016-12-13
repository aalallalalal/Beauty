package com.dup.beauty.mvp.presenter.contract;

import com.dup.beauty.mvp.view.IBaseView;

/**
 * 基presenter
 * Created by DP on 2016/12/12.
 */
public interface IBasePresenter {
    void attachView(IBaseView view);
}

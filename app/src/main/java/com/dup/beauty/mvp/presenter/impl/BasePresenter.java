package com.dup.beauty.mvp.presenter.impl;

import com.dup.beauty.mvp.presenter.contract.IBasePresenter;
import com.dup.beauty.mvp.view.IBaseView;

/**
 * 基Presenter
 * Created by DP on 2016/12/12.
 */
public class BasePresenter<T extends IBaseView> implements IBasePresenter {
    protected T view;

    /**
     * 通过此方法设置view,不用构造器.方便dagger2注入(不需要在单独再提供View).
     *
     * @param view
     */
    @Override
    public void attachView(IBaseView view) {
        this.view = (T) view;
    }
}

package com.dup.beauty.mvp.presenter.contract;

import android.content.Context;

/**
 * 基presenter
 * Created by DP on 2016/12/12.
 */
public interface IBasePresenter<T> {
    void attachView(T view);

    T getView();

    void attachContext(Context context);

    Context getContext();

    void clearRef();
}

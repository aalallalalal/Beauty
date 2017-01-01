package com.dup.beauty.mvp.presenter.impl;

import android.content.Context;

import com.dup.beauty.mvp.presenter.contract.IBasePresenter;
import com.dup.beauty.mvp.view.IBaseView;

import java.lang.ref.WeakReference;

/**
 * 基Presenter
 * Created by DP on 2016/12/12.
 */
public class BasePresenter<T extends IBaseView> implements IBasePresenter<T> {
    protected WeakReference<T> ref_view;
    protected WeakReference<Context> ref_context;

    public BasePresenter(Context context) {
        attachContext(context);
    }

    /**
     * 通过此方法设置view,不用构造器.方便dagger2注入(不需要在单独再提供View).
     *
     * @param view
     */
    @Override
    public void attachView(T view) {
        ref_view = new WeakReference<>(view);
    }

    @Override
    public T getView() {
        if (ref_view != null) {
            return ref_view.get();
        }
        return null;
    }

    @Override
    public void attachContext(Context context) {
        ref_context = new WeakReference<Context>(context);
    }

    @Override
    public Context getContext() {
        if (ref_context != null) {
            return ref_context.get();
        }
        return null;
    }

    @Override
    public void clearRef() {
        if (ref_context != null) {
            ref_context.clear();
            ref_context = null;
        }

        if (ref_view != null) {
            ref_view.clear();
            ref_view = null;
        }
    }


}

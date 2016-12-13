package com.dup.beauty.mvp.model.util;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Retrofit与RxJava封装工具类
 * Created by DP on 2016/11/9.
 * doOnSubscribe及线程转换 参考博客：http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/1012/3572.html#toc_24
 */
public class RUtil {

    /**
     * 网络请求进程转化器
     *
     * @param startAction 注测observer时回调方法。（网络请求前回调方法，用以等待层显示）
     * @return
     */
    public static <T> Observable.Transformer<T, T> threadTrs(final Action0 startAction) {
        Observable.Transformer transformer = new Observable.Transformer<T, T>() {
            @Override
            public Observable call(Observable<T> o) {
                Observable<T> reObservable = o.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .doOnSubscribe(startAction)
                        .subscribeOn(AndroidSchedulers.mainThread())//doOnSubscribe会使用此线程，但只有第一个 subscribeOn()起作用。
                        .observeOn(AndroidSchedulers.mainThread());
                return reObservable;
            }
        };
        return transformer;
    }

    /**
     * 网络请求进程转化器
     *
     * @return
     */
    public static <T> Observable.Transformer<T, T> threadTrs() {
        Observable.Transformer transformer = new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> o) {
                Observable<T> reObservable = o.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
                return reObservable;
            }
        };
        return transformer;
    }

    /**
     * 封装管理等待层的观察者
     *
     * @param <T>
     */
    public abstract static class DialogObserver<T> implements Observer<T> {

        @Override
        public void onCompleted() {
            dismissDialog();
        }

        @Override
        public void onError(Throwable e) {
            dismissDialog();
        }

        public abstract void onNext(T o);

        protected abstract void dismissDialog();

    }

}

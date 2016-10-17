package com.dup.beauty.model.util;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dup.beauty.R;
import com.dup.beauty.model.util.glide.OnPercentToTarget;
import com.dup.beauty.model.util.glide.ProgressListener;
import com.dup.beauty.model.util.glide.ProgressModelLoader;
import com.dup.beauty.util.L;
import com.dup.beauty.util.StringUtil;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * 封装Glide.
 * <ul>
 * <li>glide基本配置</li>
 * <li>进度功能</li>
 * </ul>
 * <b>注意：网络请求下不可以设置thumbnail，加载本地图片可以。因为设置了thumbnail会导致多个线程
 * 去下载网络图片，会导致进度数据时而显示thumbnail进度，时而原图数据进度。
 * 经测试原因可能如上，能力有限，不知道怎么解决...</b>
 * <p/>
 * Created by DP on 2016/10/13.
 */
public class GlideUtil {

    /**
     * 基本设置
     *
     * @param context
     * @param model   图片地址
     * @return
     */
    public static DrawableRequestBuilder begin(Context context, String model) {
        return Glide.with(context).load(model).diskCacheStrategy(DiskCacheStrategy.ALL).
                priority(Priority.IMMEDIATE);
    }

    /**
     * 有下载进度 回调 监听(子线程)
     *
     * @param context
     * @param model
     * @param listener 下载进度监听
     * @return <b>注意回调执行在非主线程</b>
     */
    public static DrawableRequestBuilder begin(Context context, String model, ProgressListener listener) {
        return Glide.with(context).using(new ProgressModelLoader(listener))
                .load(model).diskCacheStrategy(DiskCacheStrategy.ALL).
                        priority(Priority.IMMEDIATE);
    }

    /**
     * 使用Rx 将回调切换至主线程
     *
     * @param context
     * @param model
     * @param callback 改变回调，在里面实现具体操作进度数据。回调数据为0-100。
     * @param <T>
     * @return
     */
    public static <T> DrawableRequestBuilder begin(final Context context, String model, final OnPercentToTarget callback) {
        return Glide.with(context).using(new ProgressModelLoader(new ProgressListener() {
            @Override
            public void update(String url, long bytesRead, long contentLength, boolean done) {
                final float percent = (float) bytesRead / contentLength;
                L.e("percent:" + percent);
                Observable.just(percent)
                        .map(new Func1<Float, Float>() {
                            @Override
                            public Float call(Float aFloat) {
                                return aFloat * 100;
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Float>() {
                            @Override
                            public void call(Float aFloat) {
                                if (callback != null) {
                                    callback.onPercent(aFloat);
                                }
                            }
                        });
            }
        }))
                .load(model).diskCacheStrategy(DiskCacheStrategy.ALL).
                        priority(Priority.IMMEDIATE);
    }


    /**
     * 传入放图片的ViewGroup和进度textview,会自动将进度textview放在ViewGroup中心.
     * <b>根据本项目中需求封装</b>
     *
     * @param context
     * @param model
     * @return
     */
    public static DrawableRequestBuilder begin(final Context context, String model, final TextView textView) {
        textView.setTag(model);
        return Glide.with(context).using(new ProgressModelLoader(new ProgressListener() {
            @Override
            public void update(final String url, long bytesRead, long contentLength, boolean done) {
                handlerProgress(context, textView, url, bytesRead, contentLength, done);
            }
        }))
                .load(model).diskCacheStrategy(DiskCacheStrategy.ALL).
                        priority(Priority.IMMEDIATE);
    }

    /**
     * 传入放图片的ViewGroup和进度textview,会自动将进度textview放在ViewGroup中心.
     * <b>asBitmap etc</b>
     * <b>根据本项目中需求封装</b>
     *
     * @param context
     * @param model
     * @return
     */
    public static DrawableTypeRequest<String> beginAsOther(final Context context, String model, final TextView textView) {
        textView.setTag(model);
        return Glide.with(context).using(new ProgressModelLoader(new ProgressListener() {
            @Override
            public void update(final String url, long bytesRead, long contentLength, boolean done) {
                handlerProgress(context, textView, url, bytesRead, contentLength, done);
            }
        }))
                .load(model);
    }

    /**
     * 处理获取到的进度数据，并转到主线程，设置到textview上
     *
     * @param context
     * @param textView
     * @param url
     * @param bytesRead
     * @param contentLength
     * @param done
     */
    private static void handlerProgress(final Context context, final TextView textView, final String url, long bytesRead, long contentLength, boolean done) {
        final float percent = (float) bytesRead / contentLength;
        Observable.just(percent)
                .map(new Func1<Float, Float>() {
                    @Override
                    public Float call(Float aFloat) {
                        return aFloat * 100;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Float>() {
                    @Override
                    public void call(Float percent) {
                        int i = percent.intValue();
                        if (url.equals(textView.getTag())) {
                            textView.setText(StringUtil.getFormatStrRes(context, R.string.loading_percent, i + ""));
                            if (percent == 100) {
                                textView.setVisibility(View.GONE);
                            }
                        } else {
                            L.d("进度view TAG不匹配");
                        }
                    }
                });
    }

}

package com.dup.beauty.model.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dup.beauty.R;
import com.dup.beauty.app.Constant;
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
 * <li>封装网络图片下载进度功能</li>
 * </ul>
 * <b>注意：网络请求下设置了thumbnail(float)会导致有一个全图请求和缩略图请求，
 * 去下载网络图片，会导致进度数据时而显示thumbnail进度，时而原图数据进度。<br>
 * 经测试原因可能如上，目前解决办法是thumbnail（新请求）这个方法。这样原图和缩略图成为了两个不关联的请求。进度是原图下载进度.</b>
 * <p/>
 * Created by DP on 2016/10/13.
 */
public class GlideUtil {

    /**
     * 初始化glide一些参数
     *
     * @param applicationContext
     */
    public static void init(Context applicationContext) {
        Glide.get(applicationContext).setMemoryCategory(MemoryCategory.HIGH);
        GlideBuilder builder = new GlideBuilder(applicationContext);
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    /**
     * 有下载进度回调监听(子线程)
     *
     * @param context
     * @param model
     * @param listener 下载进度监听
     * @return <b>注意回调执行在非主线程</b>
     */
    public static DrawableRequestBuilder begin(Context context, String model, ProgressListener listener) {
        return Glide.with(context).using(new ProgressModelLoader(listener))
                .load(model).diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    /**
     * 使用Rx 将回调切换至主线程
     *
     * @param context
     * @param model
     * @param callback 改变回调，在里面实现具体操作进度数据。回调数据为0-100。
     * @return
     */
    public static DrawableRequestBuilder begin(final Context context, String model, final OnPercentToTarget callback) {
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
                .load(model).diskCacheStrategy(DiskCacheStrategy.ALL);
    }


    /**
     * 非独立请求缩略图，不带进度。
     *
     * @param context
     * @param model
     * @return
     */
    public static DrawableRequestBuilder beginNoProgress(final Context context, String model) {
        return Glide.with(context)
                .load(model).diskCacheStrategy(DiskCacheStrategy.ALL).thumbnail(Constant.THUMBNAIL);
    }


    /**
     * 无请求缩略图，带进度。
     * <b>注意：如果加载的是本地图片，不会有进度回调</b>
     *
     * @param context
     * @param model
     * @return
     */
    public static DrawableRequestBuilder<String> beginProgress(final Context context, String model, final TextView textView) {
        textView.setTag(R.id.tag_glide_progress, model);
        textView.setVisibility(View.VISIBLE);
        textView.setText(StringUtil.getStrRes(context, R.string.image_loading));
        return Glide.with(context).using(new ProgressModelLoader(new ProgressListener() {
            @Override
            public void update(final String url, long bytesRead, long contentLength, boolean done) {
                handlerProgress(context, textView, url, bytesRead, contentLength, done);
            }
        }))
                .load(model).diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        if (model.equals(textView.getTag(R.id.tag_glide_progress))) {
                            textView.setText(StringUtil.getStrRes(context, R.string.image_loading_error));
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (model.equals(textView.getTag(R.id.tag_glide_progress))) {
                            textView.setVisibility(View.GONE);
                        }
                        return false;
                    }
                });
    }

    /**
     * 无请求缩略图，带进度。viewpager用。
     * <b>注意：如果加载的是本地图片，不会有进度回调</b>
     *
     * @param context
     * @param model
     * @param textView  进度view
     * @param tagSuffix tag后缀：主要用来防止两个相同的请求，进度view 的tag也相同，导致的混乱。
     * @return
     */
    public static DrawableRequestBuilder<String> beginViewpagerLoad(final Context context, String model, final TextView textView, final String tagSuffix) {
        textView.setTag(R.id.tag_glide_progress, model + tagSuffix);
        textView.setVisibility(View.VISIBLE);
        textView.setText(StringUtil.getStrRes(context, R.string.image_loading));
        return Glide.with(context).using(new ProgressModelLoader(new ProgressListener() {
            @Override
            public void update(final String url, long bytesRead, long contentLength, boolean done) {
                handlerProgress(context, textView, url + tagSuffix, bytesRead, contentLength, done);
            }
        }))
                .load(model).diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        if ((model + tagSuffix).equals(textView.getTag(R.id.tag_glide_progress))) {
                            textView.setText(StringUtil.getStrRes(context, R.string.image_loading_error));
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if(!isFirstResource) {
                            //缩略图加载成功时忽略，当全图成功时再隐藏。
                            if ((model + tagSuffix).equals(textView.getTag(R.id.tag_glide_progress))) {
                                textView.setVisibility(View.GONE);
                            }
                        }
                        return false;
                    }
                });
    }

    /**
     * 独立请求缩略图，带进度。
     * <b>注意：如果加载的是本地图片，不会有进度回调</b>
     *
     * @param context
     * @param model
     * @param urlThumbnail
     * @param textView     进度view
     * @return
     */
    public static BitmapRequestBuilder<String, Bitmap> beginAsBitmap(final Context context, String model, String urlThumbnail, final TextView textView) {
        textView.setTag(R.id.tag_glide_progress, model);
        textView.setVisibility(View.VISIBLE);
        textView.setText(StringUtil.getStrRes(context, R.string.image_loading));
        //缩略图采用 可设置图片大小的接口请求。
        BitmapRequestBuilder<String, Bitmap> thumbnailRequest = Glide
                .with(context)
                .load(urlThumbnail).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(false);

        return Glide.with(context).using(new ProgressModelLoader(new ProgressListener() {
            @Override
            public void update(final String url, long bytesRead, long contentLength, boolean done) {
                handlerProgress(context, textView, url, bytesRead, contentLength, done);
            }
        }))
                .load(model).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(thumbnailRequest)
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        if (model.equals(textView.getTag(R.id.tag_glide_progress))) {
                            textView.setText(StringUtil.getStrRes(context, R.string.image_loading_error));
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if(!isFirstResource){
                            //缩略图加载成功时忽略，当全图成功时再隐藏。
                            if (model.equals(textView.getTag(R.id.tag_glide_progress))) {
                                textView.setVisibility(View.GONE);
                            }
                        }
                        return false;
                    }
                });
    }

    /**
     * 处理获取到的进度数据，并转到主线程，设置到textview上
     * 如果图片本地存在，则不会走这个回调。
     *
     * @param context
     * @param textView
     * @param tag
     * @param bytesRead
     * @param contentLength
     * @param done
     */
    private static void handlerProgress(final Context context, final TextView textView, final String tag, long bytesRead, long contentLength, boolean done) {
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
                        if (tag.equals(textView.getTag(R.id.tag_glide_progress))) {

                            if (i == 100) {
                                textView.setVisibility(View.GONE);
                            } else {
                                textView.setVisibility(View.VISIBLE);
                            }

                            textView.setText(StringUtil.getFormatStrRes(context, R.string.loading_percent, i + ""));

                        } else {
                            L.d("进度view TAG不匹配");
                        }
                    }
                });
    }

}

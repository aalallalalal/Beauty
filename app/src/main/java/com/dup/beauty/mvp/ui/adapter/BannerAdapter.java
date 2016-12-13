package com.dup.beauty.mvp.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.dup.beauty.R;
import com.dup.beauty.app.Constant;
import com.dup.beauty.mvp.model.api.ApiDefine;
import com.dup.beauty.mvp.model.entity.Gallery;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * 主界面banner适配器
 * Created by DP on 2016/9/29.
 */
public class BannerAdapter implements BGABanner.Adapter {
    private Context context;

    public BannerAdapter(Context context) {
        this.context = context;
    }

    /**
     * banner初始化item回调方法
     *
     * @param banner
     * @param view
     * @param model
     * @param position
     */
    @Override
    public void fillBannerItem(BGABanner banner, View view, Object model, int position) {
        Gallery gallery = (Gallery) model;
        String url = ApiDefine.getImageUrlWithNoSize(gallery.getImg());
        Glide.with(context).load(url).
                priority(Priority.IMMEDIATE).placeholder(R.drawable.icon_photo_empty)
                .error(R.drawable.icon_photo_error)
                .thumbnail(Constant.THUMBNAIL)
                .centerCrop().crossFade().into((ImageView) view);
    }
}

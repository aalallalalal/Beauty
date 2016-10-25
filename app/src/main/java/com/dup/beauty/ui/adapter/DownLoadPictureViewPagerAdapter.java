package com.dup.beauty.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dup.beauty.R;
import com.dup.beauty.model.api.ApiDefine;
import com.dup.beauty.model.entity.Picture;
import com.dup.beauty.model.util.GlideUtil;
import com.dup.changeskin.SkinManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * 下载的图片 大图查看viewpager Adapter
 * Created by DP on 2016/10/12.
 */
public class DownloadPictureViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<File> mData;

    public DownloadPictureViewPagerAdapter(Context context, ArrayList<File> data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_viewpager_picture, null);

        SkinManager.getInstance().injectSkin(view);

        //添加view佈局
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        container.addView(view, params);

        ImageView iv = (ImageView) view.findViewById(R.id.item_viewpager_picture_photoview);

        Glide.with(mContext).load(mData.get(position)).crossFade().error(R.drawable.icon_photo_error)
                .into(iv);

        return view;
    }
}

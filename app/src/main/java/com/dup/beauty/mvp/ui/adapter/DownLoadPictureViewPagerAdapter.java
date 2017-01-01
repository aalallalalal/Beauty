package com.dup.beauty.mvp.ui.adapter;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.dup.beauty.R;
import com.dup.changeskin.SkinManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * 下载的图片 大图查看viewpager Adapter
 * Created by DP on 2016/10/12.
 */
public class DownLoadPictureViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<File> mData;

    private ArrayMap<Integer, View> mViews;

    public DownLoadPictureViewPagerAdapter(Context context, ArrayList<File> data) {
        this.mContext = context;
        this.mData = data;
        this.mViews = new ArrayMap<>();
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

        View view = mViews.get(position);
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_viewpager_picture, null);
            SkinManager.getInstance().injectSkin(view);
            ImageView iv = (ImageView) view.findViewById(R.id.item_viewpager_picture_photoview);

            Glide.with(mContext).load(mData.get(position)).priority(Priority.IMMEDIATE).crossFade().error(R.drawable.icon_photo_error)
                    .into(iv);
            mViews.put(position, view);
        }

        //添加view佈局
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        container.addView(view, params);
        return view;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
    }
}

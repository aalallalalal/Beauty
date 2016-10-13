package com.dup.beauty.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dup.beauty.R;
import com.dup.beauty.model.api.ApiDefine;
import com.dup.beauty.model.entity.Picture;
import com.dup.beauty.model.util.GlideUtil;

import java.util.List;


/**
 * 大图查看viewpager Adapter
 * Created by DP on 2016/10/12.
 */
public class PictureViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<Picture> mData;

    public PictureViewPagerAdapter(Context context, List<Picture> data) {
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

        //添加view佈局
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        container.addView(view, params);

        ImageView iv = (ImageView) view.findViewById(R.id.item_viewpager_picture_photoview);
        TextView tvProgress = (TextView) view.findViewById(R.id.item_viewpager_picture_progress);
        //这里不要指定大小.因为如果需求大小大于图片大小,那么图片将不显示.但是如果不指定大小,加载的图片也不是原图.
        String url = ApiDefine.getImageUrlWithNoSize(mData.get(position).getSrc());

        //添加Textview指示下载进度
        GlideUtil.begin(mContext, url, tvProgress)
                .thumbnail(0.6f)
                .crossFade()
                .error(R.drawable.icon_photo_error)
                .into(iv);

        return view;
    }
}

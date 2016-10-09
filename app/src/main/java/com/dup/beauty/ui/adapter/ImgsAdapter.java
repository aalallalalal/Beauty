package com.dup.beauty.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.dup.beauty.R;
import com.dup.beauty.model.api.ApiDefine;
import com.dup.beauty.model.entity.Gallery;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 图片列表 adapter
 * Created by DP on 2016/9/27.
 */
public class ImgsAdapter extends RecyclerView.Adapter<ImgsAdapter.MyViewHolder> {
    private Context context;
    /**
     * 存放图片数据
     */
    private ArrayList<Gallery> mData;

    /**
     * 存放加载过的图片大小数据
     * <url，size>
     */
    private ArrayMap<String, ItemSize> sizeMap = new ArrayMap<>();
    /**
     * 屏幕宽的0.5
     */
    private int itemWidth;


    public ImgsAdapter(Context context, List<Gallery> data, int width) {
        this.mData = (ArrayList<Gallery>) data;
        this.context = context;
        this.itemWidth = width / 2;
    }


    public ArrayList<Gallery> getData() {
        return mData;
    }

    public void setData(ArrayList<Gallery> mData) {
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_imgs, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Gallery gallery = mData.get(position);
        String url = ApiDefine.getImageUrlWithSize(gallery.getImg(), (int) (itemWidth / 1.8f), 0);

        if (sizeMap.containsKey(url) && !sizeMap.get(url).isNull()) {
            /*当图片大小数据已得到,先改变item大小,后加载图片*/
            setItemSize(sizeMap.get(url), holder.iv);
            Glide.with(context).load(url)
                    .thumbnail(0.2f)
                    .priority(Priority.IMMEDIATE)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.icon_photo_empty)
                    .crossFade()
                    .error(R.drawable.icon_photo_error)
                    .into(holder.iv);
        } else {
            /*当图片大小数据没得到,通过target回调,根据图片大小改变item大小*/
            Glide.with(context).load(url)
                    .asBitmap()
                    .thumbnail(0.2f)
                    .priority(Priority.IMMEDIATE)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.icon_photo_empty)
                    .error(R.drawable.icon_photo_error)
                    .into(new ImageViewTarget(holder, url));
        }

        //设置文本信息
        String count = String.format(context.getResources().getString(R.string.imgs_visitor_count), gallery.getCount());
        String size = String.format(context.getResources().getString(R.string.imgs_size), gallery.getSize());
        holder.tvTitle.setText(gallery.getTitle() + "");
        holder.tvCount.setText(count);
        holder.tvSize.setText(size);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_imgs_iv)
        public ImageView iv;
        @BindView(R.id.item_imgs_tv_title)
        public TextView tvTitle;
        @BindView(R.id.item_imgs_tv_size)
        public TextView tvSize;
        @BindView(R.id.item_imgs_tv_count)
        public TextView tvCount;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    /**
     * 图片异步加载回调类.在此类中设置item大小
     */
    private class ImageViewTarget extends BitmapImageViewTarget {
        private MyViewHolder holder;
        private String url;

        public ImageViewTarget(ImageView view) {
            super(view);
        }

        public ImageViewTarget(MyViewHolder holder, String url) {
            super(holder.iv);
            this.holder = holder;
            this.url = url;
        }

        @Override
        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
            //1.获取imageview的应有大小
            int viewWidth = holder.iv.getWidth();
            float scale = resource.getWidth() / (viewWidth * 1.0f);
            int viewHeight = (int) (resource.getHeight() / scale);

            //2.创建大小实体类,并为view设置大小
            ItemSize sm = new ItemSize(viewWidth, viewHeight);
            setItemSize(sm, holder.iv);

            //3.将获取到的大小数据,放入map中。之后item复用时，就提前设置view大小。
            sizeMap.put(url, sm);

            super.onResourceReady(resource, glideAnimation);
        }
    }

    /**
     * 设置item大小具体方法
     *
     * @param size
     */
    private void setItemSize(ItemSize size, View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = size.getWidth();
        layoutParams.height = size.getHeight();
        view.setLayoutParams(layoutParams);
    }


    /**
     * item大小实体类
     */
    public class ItemSize {

        private int height;
        private int width;

        public ItemSize(int width, int height) {
            this.height = height;
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public boolean isNull() {
            if (height <= 0 || width <= 0) {
                return true;
            } else {
                return false;
            }
        }

    }

}

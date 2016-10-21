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

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.dup.beauty.R;
import com.dup.beauty.app.Constant;
import com.dup.beauty.model.api.ApiDefine;
import com.dup.beauty.model.entity.Gallery;
import com.dup.beauty.model.util.GlideUtil;
import com.dup.changeskin.SkinManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 图库封面列表 adapter
 * Created by DP on 2016/9/27.
 */
public class GalleriesAdapter extends RecyclerView.Adapter<GalleriesAdapter.MyViewHolder> {
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

    public GalleriesAdapter(Context context,  int width) {
        this.context = context;
        this.itemWidth = (width / 2) > Constant.PIC_MAX_WIDTH ? Constant.PIC_MAX_WIDTH : (width / 2);//如果需求值 大于 原图片最大值，图片将不显示。所以这控制一下
    }

    public GalleriesAdapter(Context context, List<Gallery> data, int width) {
        this.mData = (ArrayList<Gallery>) data;
        this.context = context;
        this.itemWidth = (width / 2) > Constant.PIC_MAX_WIDTH ? Constant.PIC_MAX_WIDTH : (width / 2);//如果需求值 大于 原图片最大值，图片将不显示。所以这控制一下
    }


    public ArrayList<Gallery> getData() {
        return mData;
    }

    public void setData(ArrayList<Gallery> mData) {
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery, parent, false);
        SkinManager.getInstance().injectSkin(view);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Gallery gallery = mData.get(position);
        //这里设置图片宽度 为屏幕一半除1.8 宽
        String url = ApiDefine.getImageUrlWithSize(gallery.getImg(), (int) (itemWidth / Constant.PIC_WIDTH_RATIO), 0);

        if (sizeMap.containsKey(url) && !sizeMap.get(url).isNull()) {
            /*当图片大小数据已得到,先改变item大小,后加载图片*/
            setItemSize(sizeMap.get(url), holder.iv);
            GlideUtil.begin(context, url, holder.tvProgress)
                    .thumbnail(0.2f).placeholder(R.drawable.icon_photo_empty)
                    .crossFade()
                    .error(R.drawable.icon_photo_error)
                    .into(holder.iv);
        } else {
             /*当图片大小数据没得到,通过target回调,根据图片大小改变item大小*/
            GlideUtil.beginAsOther(context, url, holder.tvProgress)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.IMMEDIATE)
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

        //设置点击监听
        holder.setItemClickListener(holder.getLayoutPosition());
    }

    @Override
    public int getItemCount() {
        if(mData==null)
            return 0;
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View itemView;
        @BindView(R.id.item_gallery_iv)
        public ImageView iv;
        @BindView(R.id.item_gallery_tv_title)
        public TextView tvTitle;
        @BindView(R.id.item_gallery_tv_size)
        public TextView tvSize;
        @BindView(R.id.item_gallery_tv_count)
        public TextView tvCount;
        @BindView(R.id.item_gallery_progress)
        public TextView tvProgress;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }

        public void setItemClickListener(final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(position, mData.get(position - 1));//-1是因为xrecycler有header
                    }
                }
            });
        }
    }

    /**
     * 图片异步加载回调类.在此类中设置item大小
     */
    private class ImageViewTarget extends BitmapImageViewTarget {
        private MyViewHolder holder;
        private String url;

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

    private OnItemClickListener mItemClickListener;

    public void setItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, Gallery gallery);
    }

}

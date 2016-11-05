package com.dup.beauty.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.dup.beauty.R;
import com.dup.beauty.app.Constant;
import com.dup.changeskin.SkinManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 下载的图片列表 adapter
 * Created by DP on 2016/9/27.
 */
public class DownloadImagesAdapter extends RecyclerView.Adapter<DownloadImagesAdapter.MyViewHolder> {
    private Context context;
    /**
     * 存放图片数据
     */
    private ArrayList<File> mData;

    /**
     * 存放加载过的图片大小数据
     * <url，size>
     */
    private ArrayMap<String, ItemSize> sizeMap = new ArrayMap<>();


    public DownloadImagesAdapter(Context context, List<File> data) {
        this.mData = (ArrayList<File>) data;
        this.context = context;
    }


    public ArrayList<File> getData() {
        return mData;
    }

    public void setData(ArrayList<File> mData) {
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picture, parent, false);
        SkinManager.getInstance().injectSkin(view);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        String path = mData.get(position).toString();

        if (sizeMap.containsKey(path) && !sizeMap.get(path).isNull()) {
            /*当图片大小数据已得到,先改变item大小,后加载图片*/
            setItemSize(sizeMap.get(path), holder.iv);
            Glide.with(context).load(mData.get(position))
                    .thumbnail(Constant.THUMBNAIL)
                    .placeholder(R.drawable.icon_photo_empty)
                    .crossFade()
                    .error(R.drawable.icon_photo_error)
                    .into(holder.iv);
        } else {
            /*当图片大小数据没得到,通过target回调,根据图片大小改变item大小*/
            Glide.with(context).load(mData.get(position))
                    .asBitmap()
                    .thumbnail(Constant.THUMBNAIL)
                    .placeholder(R.drawable.icon_photo_empty)
                    .error(R.drawable.icon_photo_error)
                    .into(new ImageViewTarget(holder, path));
        }

        //设置点击监听
        holder.setItemClickListener(holder.getLayoutPosition());
    }

    @Override
    public int getItemCount() {
        if (mData == null)
            return 0;
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View itemView;
        @BindView(R.id.item_picture_iv)
        public ImageView iv;
        @BindView(R.id.item_picture_progress)
        public TextView progress;

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
                        mItemClickListener.onItemClick(position, Uri.fromFile(mData.get(position)).toString());
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
        private String uri;

        public ImageViewTarget(MyViewHolder holder, String uri) {
            super(holder.iv);
            this.holder = holder;
            this.uri = uri;
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
            sizeMap.put(uri, sm);

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
        void onItemClick(int position, String uri);
    }

}

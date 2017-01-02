package com.dup.beauty.mvp.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.dup.beauty.util.L;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

/**
 * Created by DP on 2017/1/1.
 * 控制列表在fling时，暂停图片请求。
 * <b>XRecyclerView</b>
 */
public class AutoLoadXRecyclerView extends XRecyclerView {

    private boolean isScrollListenerEnable = true;
    private Context context;

    public AutoLoadXRecyclerView(Context context) {
        this(context, null);
    }

    public AutoLoadXRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoLoadXRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    private void init() {

        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (context == null) {
                    return;
                }

                if (!isScrollListenerEnable) {
                    return;
                }

                switch (newState) {
                    case SCROLL_STATE_IDLE:
                    case SCROLL_STATE_DRAGGING:
                        Glide.with(context).resumeRequests();

                        break;
                    case SCROLL_STATE_SETTLING:
                        Glide.with(context).pauseRequests();
                        break;
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    /**
     * 控制是否在滚动时，停止图片加载
     *
     * @param isScrollListenerEnable
     */
    public void isScrollListenerEnable(boolean isScrollListenerEnable) {
        this.isScrollListenerEnable = isScrollListenerEnable;
    }

}

package com.dup.beauty.mvp.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.bumptech.glide.Glide;

/**
 * Created by DP on 2017/1/1.
 */
public class AutoLoadRecyclerView extends RecyclerView {

    private boolean isScrollListenerEnable = true;
    private Context context;

    public AutoLoadRecyclerView(Context context) {
        this(context, null);
    }

    public AutoLoadRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoLoadRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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

    public void setContext(Context context) {
        this.context = context;
    }
}

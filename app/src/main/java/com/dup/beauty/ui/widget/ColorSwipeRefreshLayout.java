package com.dup.beauty.ui.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import com.dup.changeskin.SkinManager;

/**
 * 进度圈 跟随主题色
 * Created by DP on 2016/10/31.
 */
public class ColorSwipeRefreshLayout extends SwipeRefreshLayout {
    public ColorSwipeRefreshLayout(Context context) {
        super(context);
        init();
    }

    public ColorSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        int bg = SkinManager.getInstance().getResourceManager().getColor("title_bg");
        int circle = SkinManager.getInstance().getResourceManager().getColor("title_text");
        this.setColorSchemeColors(circle);
        this.setProgressBackgroundColorSchemeColor(bg);
    }

}

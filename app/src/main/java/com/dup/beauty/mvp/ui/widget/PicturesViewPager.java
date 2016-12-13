package com.dup.beauty.mvp.ui.widget;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


/**
 * 查看大图功能中的ViewPager 。为了解决 图片放大查看时出现崩溃。
 * 支持点击，长按 监听
 *
 * @author dup
 */
public class PicturesViewPager extends ViewPager implements
        GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    /**
     * 是否支持单点
     */
    private boolean isSingleTapUp = true;
    /**
     * 是否支持长按
     */
    private boolean isLongPress = false;
    private OnPageSingleTapListener mListener;
    private OnPageLongPressListener mLongPressListener;
    private GestureDetector mDetector;

    public PicturesViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDetector = new GestureDetector(context, this);
        mDetector.setOnDoubleTapListener(this);
        setPageTransformer(true, new MyPagerTransform());
    }

    public PicturesViewPager(Context context) {
        this(context, null);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        //解决viewpager滑动崩溃bug
        try {
            return super.onInterceptTouchEvent(arg0);
        } catch (IllegalArgumentException e) {
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mDetector != null) {
            mDetector.onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        if (mListener != null && isSingleTapUp) {
            mListener.onPageClick();
        }
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        if (mLongPressListener != null && isLongPress) {
            mLongPressListener.onPageLongPress();
        }
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    /**
     * 设置单点是否起作用
     *
     * @param isSingleTapUp
     */
    public void setSingleTapUp(boolean isSingleTapUp) {
        this.isSingleTapUp = isSingleTapUp;
    }

    /**
     * 设置长按是否起作用
     *
     * @param isLongPress
     */
    public void setLongPress(boolean isLongPress) {
        this.isLongPress = isLongPress;
    }

    public void setOnSingleTapUpListener(OnPageSingleTapListener mListener) {
        this.mListener = mListener;
    }

    public void setOnLongPressListener(OnPageLongPressListener mLongPressListener) {
        this.mLongPressListener = mLongPressListener;
    }

    /**
     * 点击监听
     */
    public interface OnPageSingleTapListener {
        void onPageClick();
    }

    /**
     * 长按监听
     */
    public interface OnPageLongPressListener {
        void onPageLongPress();
    }


    /**
     * 挤压效果
     */
    private class MyPagerTransform implements PageTransformer {
        @Override
        public void transformPage(View page, float position) {
            page.scrollTo((int) (page.getWidth() / 2 * position), 0);
        }
    }

}

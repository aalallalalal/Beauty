package com.dup.beauty.app;

/**
 * Created by DP on 2016/9/18.
 */
public class Constant {
    public static final String TAG = "TAG";

    /**
     * banner个数
     */
    public static final int BANNER_NUM = 5;

    /**
     * banner开始自动滚动和停止 与Collapsinglayout最大移动距离的阈值 百分比。即移动超过0.4就停止滚动。
     */
    public static final float BANNER_AUTO_VALVE = 0.4f;

    /**
     * 每页默认20张
     */
    public static final int PAGE_COUNT = 20;

    /**
     * 获取列表中 图片宽度为屏幕二分之一宽 / PIC_WIDTH_RATIO
     */
    public static final float PIC_WIDTH_RATIO = 1.8f;

    /**
     * 获取列表中 图片宽度最大为800px
     */
    public static final int PIC_MAX_WIDTH = 800;

    /**
     * 闪屏页延迟时间
     */
    public static long SPLASH_DELAY = 1500;

}

package com.dup.beauty.model.api;

import com.dup.beauty.util.L;

/**
 * API定义、常量
 */
public final class ApiDefine {

    private ApiDefine() {
    }

    public static final String HOST_BASE_URL = "http://www.tngou.net";

    public static final String HOST_BASE_IMG_WITH_SIZE = "http://tnfs.tngou.net/image";


    /**
     * 组装imgurl，获取特定大小的图片
     * <b>注意，如果图片大小参数小于 需求大小，那么图片将获取失败</b>
     * @param imgUrl
     * @param width
     * @param height
     * @return
     */
    public static final String getImageUrlWithSize(String imgUrl, int width, int height) {
        String s = ApiDefine.HOST_BASE_IMG_WITH_SIZE + imgUrl + "_" + width + "x" + height;
        L.d("Request img url----->" + s);
        return s;
    }

    /**
     * 组装imgurl，获取图片原图
     *
     * @param imgUrl
     * @return
     */
    public static final String getImageUrlWithNoSize(String imgUrl) {
        String s = ApiDefine.HOST_BASE_IMG_WITH_SIZE + imgUrl ;
        L.d("Request img url----->" + s);
        return s;
    }
}

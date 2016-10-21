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
    public static final String HOST_BASE_IMG_WITH_NO_SIZE = "http://tnfs.tngou.net/img";

    //图片三方api授权ID
    public static final String Client_Id = "7503590";
    //应用授权secret
    public static final String Client_Secret = "7dd8e8baf7dab4c2d7c3202be3b97d27";

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
     * 组装imgurl，获取默认大小图片(注意这里非原图、最大图)
     *
     * @param imgUrl
     * @return
     */
    public static final String getImageUrlWithNoSize(String imgUrl) {
        String s = ApiDefine.HOST_BASE_IMG_WITH_NO_SIZE + imgUrl ;
        L.d("Request img url----->" + s);
        return s;
    }
}

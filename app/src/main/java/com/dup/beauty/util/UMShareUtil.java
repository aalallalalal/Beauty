package com.dup.beauty.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import com.dup.beauty.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.File;

/**
 * 分享工具类
 * Created by DP on 2016/10/25.
 */
public class UMShareUtil {

    private static UMShareUtil INSTANCE;

    public static UMShareUtil getInstance() {
        if (INSTANCE == null) {
            synchronized (UMShareUtil.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UMShareUtil();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 根据图片url分享
     *
     * @param activity
     * @param url
     */
    public void openSharePane(Activity activity, String url) {
        new ShareAction(activity).setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN,
                SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE, SHARE_MEDIA.MORE)
                .withMedia(new UMImage(activity, url))
                .setCallback(new CustomUMShareListener(activity.getApplicationContext()))
                .open();
    }

    /**
     * 分享图片本地file
     *
     * @param activity
     * @param imgFile
     */
    public void openSharePane(Activity activity, File imgFile) {
        new ShareAction(activity).setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN,
                SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE, SHARE_MEDIA.MORE)
                .withMedia(new UMImage(activity, imgFile))
                .setCallback(new CustomUMShareListener(activity.getApplicationContext()))
                .open();
    }

    /**
     * 分享网络图片
     *
     * @param activity
     * @param resource 通过glide加载出来的bitmap
     */
    public void openSharePane(Activity activity, Bitmap resource) {
        new ShareAction(activity).setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN,
                SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE, SHARE_MEDIA.MORE)
                .withMedia(new UMImage(activity, resource))
                .setCallback(new CustomUMShareListener(activity.getApplicationContext()))
                .open();
    }

    private static class CustomUMShareListener implements UMShareListener {
        private Context context;

        public CustomUMShareListener(Context context) {
            this.context = context;
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            L.d("分享成功！");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            T.e(context, R.string.share_failed);
            if (t != null) {
                L.e("分享错误信息：" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            T.i(context, R.string.share_canceled);
        }
    }

}

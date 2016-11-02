package com.dup.beauty.util;

import android.app.Activity;

import com.dup.beauty.ui.widget.LoadingDialog;

/**
 * 等待层工具
 * @deprecated
 * Created by DP on 2016/10/11.
 */
public class DialogUtil {
    private static DialogUtil INSTANCE;

    private LoadingDialog dialog;

    public DialogUtil() {
    }

    public static DialogUtil getInstance() {
        if (INSTANCE == null) {
            synchronized (DialogUtil.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DialogUtil();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 显示等待层
     *
     * @param activity
     */
    public void showDialog(Activity activity) {
        if (activity == null) {
            return;
        }

        try {
            if (dialog == null) {
                dialog = new LoadingDialog(activity);
            }
            if (!dialog.isShowing() && !activity.isFinishing()) {
                dialog.show();
            }
        } catch (Exception e) {
            L.e("等待层 显示出错");
        }
    }

    /**
     * 消失等待层
     */
    public void dismissDialog() {
        if (null != dialog && dialog.isShowing()) {
            dialog.closeDialog();
            dialog = null;
        }
    }

}

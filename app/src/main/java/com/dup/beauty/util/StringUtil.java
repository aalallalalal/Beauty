package com.dup.beauty.util;

import android.content.Context;
import android.support.annotation.StringRes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by DP on 2016/9/28.
 */
public class StringUtil {
    /**
     * 根據res id获取string
     *
     * @param context
     * @param res
     * @return
     */
    public static String getStrRes(Context context, @StringRes int res) {
        String string = context.getResources().getString(res);
        return string;
    }

    /**
     * 根據res id获取规格化string
     *
     * @param context
     * @param res
     * @param strs    参数
     * @return
     */
    public static String getFormatStrRes(Context context, @StringRes int res, Object... strs) {
        String format = String.format(context.getResources().getString(res), strs);
        return format;
    }

    /**
     * 判断string是否为空
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    /**
     * 判断是否是邮箱地址
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (email == null) {
            return false;
        }
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

}

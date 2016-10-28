package com.dup.beauty.util;

import android.content.Context;
import android.support.annotation.StringRes;

import com.sdsmdg.tastytoast.TastyToast;

/**
 * 封装tastytoast 工具类
 * Created by DP on 2016/10/26.
 */
public class T {

    public static void s(Context context, @StringRes int strRes, Object... strs) {
        TastyToast.makeText(context.getApplicationContext(), StringUtil.getFormatStrRes(context, strRes, strs), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
    }

    public static void s(Context context, String msg) {
        TastyToast.makeText(context.getApplicationContext(), msg, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
    }

    public static void s(Context context, @StringRes int strRes) {
        TastyToast.makeText(context.getApplicationContext(), StringUtil.getStrRes(context, strRes), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
    }

    public static void w(Context context, @StringRes int strRes, Object... strs) {
        TastyToast.makeText(context.getApplicationContext(), StringUtil.getFormatStrRes(context, strRes, strs), TastyToast.LENGTH_SHORT, TastyToast.WARNING);
    }

    public static void w(Context context, String msg) {
        TastyToast.makeText(context.getApplicationContext(), msg, TastyToast.LENGTH_SHORT, TastyToast.WARNING);
    }

    public static void w(Context context, @StringRes int strRes) {
        TastyToast.makeText(context.getApplicationContext(), StringUtil.getStrRes(context, strRes), TastyToast.LENGTH_SHORT, TastyToast.WARNING);
    }

    public static void e(Context context, @StringRes int strRes, Object... strs) {
        TastyToast.makeText(context.getApplicationContext(), StringUtil.getFormatStrRes(context, strRes, strs), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
    }

    public static void e(Context context, String msg) {
        TastyToast.makeText(context.getApplicationContext(), msg, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
    }

    public static void e(Context context, @StringRes int strRes) {
        TastyToast.makeText(context.getApplicationContext(), StringUtil.getStrRes(context, strRes), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
    }

    public static void d(Context context, @StringRes int strRes, Object... strs) {
        TastyToast.makeText(context.getApplicationContext(), StringUtil.getFormatStrRes(context, strRes, strs), TastyToast.LENGTH_SHORT, TastyToast.DEFAULT);
    }

    public static void d(Context context, String msg) {
        TastyToast.makeText(context.getApplicationContext(), msg, TastyToast.LENGTH_SHORT, TastyToast.DEFAULT);
    }

    public static void d(Context context, @StringRes int strRes) {
        TastyToast.makeText(context.getApplicationContext(), StringUtil.getStrRes(context, strRes), TastyToast.LENGTH_SHORT, TastyToast.DEFAULT);
    }

    public static void i(Context context, @StringRes int strRes, Object... strs) {
        TastyToast.makeText(context.getApplicationContext(), StringUtil.getFormatStrRes(context, strRes, strs), TastyToast.LENGTH_SHORT, TastyToast.INFO);
    }

    public static void i(Context context, String msg) {
        TastyToast.makeText(context.getApplicationContext(), msg, TastyToast.LENGTH_SHORT, TastyToast.INFO);
    }

    public static void i(Context context, @StringRes int strRes) {
        TastyToast.makeText(context.getApplicationContext(), StringUtil.getStrRes(context, strRes), TastyToast.LENGTH_SHORT, TastyToast.INFO);
    }

    public static void c(Context context, @StringRes int strRes, Object... strs) {
        TastyToast.makeText(context.getApplicationContext(), StringUtil.getFormatStrRes(context, strRes, strs), TastyToast.LENGTH_SHORT, TastyToast.CONFUSING);
    }

    public static void c(Context context, String msg) {
        TastyToast.makeText(context.getApplicationContext(), msg, TastyToast.LENGTH_SHORT, TastyToast.CONFUSING);
    }

    public static void c(Context context, @StringRes int strRes) {
        TastyToast.makeText(context.getApplicationContext(), StringUtil.getStrRes(context, strRes), TastyToast.LENGTH_SHORT, TastyToast.CONFUSING);
    }

}

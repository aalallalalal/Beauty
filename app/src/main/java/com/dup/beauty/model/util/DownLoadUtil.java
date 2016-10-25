package com.dup.beauty.model.util;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.dup.beauty.util.DialogUtil;
import com.dup.beauty.util.FileUtil;
import com.dup.beauty.util.L;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 下载图片功能工具类
 * Created by DP on 2016/10/24.
 */
public class DownLoadUtil {
    private static final String PIC_PATH = "Beauty";

    private Executor executor;

    public static DownLoadUtil INSTANCE;

    public DownLoadUtil() {
        executor = Executors.newFixedThreadPool(5);
    }

    public static DownLoadUtil getInstance() {
        if (INSTANCE == null) {
            synchronized (DialogUtil.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DownLoadUtil();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 下载图片
     *
     * @param context  用application的context
     * @param imageUrl 图片URL
     * @param listener
     */
    public void downloadImage(final Context context, String imageUrl, final String filename, final OnImageDownLoadListener listener) {
        final File downLoadTempFile = FileUtil.getDownLoadTempFile(context, PIC_PATH, filename);
        if (downLoadTempFile==null||downLoadTempFile.exists()) {
            L.w("缓存文件生成失败，或图片正在下载，缓存文件已存在。此下载请求无效。");
            return;
        }

        Glide.with(context).load(imageUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Observable.just(saveImage(resource, FileUtil.getDownLoadImageFile(context, PIC_PATH, filename), downLoadTempFile))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean aBoolean) {
                                if (listener != null) {
                                    if (aBoolean) {
                                        listener.onDownLoadSuccess();
                                    } else {
                                        listener.onDownLoadFailed();
                                    }
                                }
                            }
                        });
            }
        });
    }

    /**
     * 获取下载的图片列表。
     * 使用Rxjava将查询处理图片列表放入子线程，最后回调转为主线程。
     *
     * @param context
     * @param listener 获取下载图片列表uri回调
     * @return
     */
    public void fetchDownloadImages(final Context context, final OnDownloadImagesFetchListener listener) {
        Observable.create(new Observable.OnSubscribe<ArrayList<File>>() {
            //异步获取下载的图片uri。获取成功后转到主线程调用回调。
            @Override
            public void call(Subscriber<? super ArrayList<File>> subscriber) {
                ArrayList<File> list = null;
                //下载的图片文件夹
                File downLoadFolder = FileUtil.getDownLoadFolder(context, PIC_PATH);
                if (downLoadFolder != null) {
                    File[] files = downLoadFolder.listFiles(new FileFilter() {
                        @Override
                        public boolean accept(File pathname) {
                            String name = pathname.getName();
                            if (!name.isEmpty() && name.endsWith(".jpg")) {
                                return true;
                            }
                            return false;
                        }
                    });

                    if (files != null) {
                        list = new ArrayList<>();
                        for (File file : files) {
                            list.add(file);
                        }
                        Collections.sort(list, new Comparator<File>() {
                            @Override
                            public int compare(File file1, File file2) {
                                if(file1.lastModified()<file2.lastModified()){
                                    return 1;
                                }
                                return -1;
                            }
                        });
                    }
                }

                subscriber.onNext(list);
            }
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ArrayList<File>>() {
                    @Override
                    public void call(ArrayList<File> list) {
                        if (listener != null) {
                            listener.onFetchImages(list);
                        }
                    }
                });

    }

    /**
     * 将bitmap保存至file
     *
     * @param bitmap
     * @param file
     * @return
     */
    private Boolean saveImage(Bitmap bitmap, File file, File tempFile) {
        if(file==null)
            return false;
        try {
            if (file.exists()) {
                file.delete();
            }
            tempFile.createNewFile();

            FileOutputStream fos = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            tempFile.renameTo(file);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 图片下载结果监听
     */
    public interface OnImageDownLoadListener {
        void onDownLoadSuccess();

        void onDownLoadFailed();
    }

    public interface OnDownloadImagesFetchListener {
        void onFetchImages(ArrayList<File> list);
    }

}

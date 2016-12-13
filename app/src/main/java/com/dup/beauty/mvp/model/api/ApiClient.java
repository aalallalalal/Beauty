package com.dup.beauty.mvp.model.api;

import android.content.Context;

import com.dup.beauty.mvp.model.util.HttpUtil;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 获取请求类
 */
public final class ApiClient {

    private ApiClient() {
    }

    private static ApiService service;

    public static ApiService getApiService(Context context) {
        if (service == null) {
            synchronized (ApiClient.class) {
                if (service == null) {
                    service = new Retrofit.Builder()
                            .baseUrl(ApiDefine.HOST_BASE_URL)
                            .client(HttpUtil.getClient(context))
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .build()
                            .create(ApiService.class);
                }
            }
        }
        return service;
    }

}

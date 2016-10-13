package com.dup.beauty.model.util;

import android.content.Context;

import com.dup.beauty.BuildConfig;
import com.dup.beauty.util.L;
import com.dup.beauty.util.NetUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.framed.Header;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * 网络配置类
 * Created by DP on 2016/9/18.
 */
public class HttpUtil {
    private static OkHttpClient client;

    private static final String path = "net_cache";//缓存路径
    private static final int AGE = 60 * 60;//一小时
    private static final int STALE = 60 * 60 * 24 * 28;//一个月
    private static int size = 100;//缓存大小MB

    private static OkHttpClient.Builder builder;

    public static OkHttpClient getClient(Context context) {
        if (client == null) {
            synchronized (HttpUtil.class) {
                if (client == null) {
                    builder = getBaseBuilder()
                            .cache(new Cache(new File(context.getExternalCacheDir(), path), 1024 * 1024 * size))
                            .addInterceptor(createCacheInterceptor(context));
                    client = builder.build();
                }
            }
        }
        return client;
    }

    /**
     * 获得基础网络配置。可以用于glide(不将图片请求缓存)。
     * @return
     */
    public static OkHttpClient.Builder getBaseBuilder() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .readTimeout(10000, TimeUnit.MILLISECONDS)
                .addInterceptor(createHttpLoggingInterceptor())
                .addInterceptor(createResponceInterceptor());
        return builder;
    }

    /**
     * 日志
     *
     * @return
     */
    private static Interceptor createHttpLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        return loggingInterceptor;
    }


    /**
     * 缓存
     * <b>注意，这里只能缓存GET.下面链接是缓存POST方法</b><br>
     * <link>http://blog.csdn.net/iamzgx/article/details/51764848<link/>
     *
     * @param context
     * @return
     */
    private static Interceptor createCacheInterceptor(final Context context) {
        Interceptor i = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!NetUtil.hasNetwork(context)) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                    L.w("无网络!");
                }
                Response originalResponse = chain.proceed(request);
                if (NetUtil.hasNetwork(context)) {
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, max-age=" + AGE + ",max-stale=" + STALE)//设置缓存超时时间
                            .removeHeader("Pragma")
                            .build();
                } else {
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, only-if-cached,max-age=" + AGE + ",max-stale=" + STALE)
                            .removeHeader("Pragma")
                            .build();
                }
            }
        };
        return i;
    }

    /**
     * 错误
     *
     * @return
     */
    private static Interceptor createResponceInterceptor() {
        Interceptor i = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());
                if (!response.isSuccessful()) {
                    L.e("与服务器连接失败" + response.header(Header.RESPONSE_STATUS.toString()));
                }
                return response;
            }
        };
        return i;
    }
}

package com.dup.beauty.model.util;

import android.content.Context;

import com.dup.beauty.BuildConfig;
import com.dup.beauty.app.Constant;
import com.dup.beauty.model.api.ApiDefine;
import com.dup.beauty.util.FileUtil;
import com.dup.beauty.util.L;
import com.dup.beauty.util.NetUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.HttpUrl;
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
                    File baseFile = FileUtil.getNetCacheFile(context);
                    File cacheFile = null;
                    if (baseFile != null) {
                        cacheFile = new File(FileUtil.getNetCacheFile(context), path);
                    }

                    if (cacheFile != null) {
                        builder = getBaseBuilder()
                                .cache(new Cache(cacheFile, 1024 * 1024 * size))
                                .addInterceptor(createCacheInterceptor(context));
                    } else {
                        builder = getBaseBuilder();
                    }

                    client = builder.build();
                }
            }
        }
        return client;
    }

    /**
     * 获得基础网络配置。可以用于glide(不将图片请求缓存)。
     *
     * @return
     */
    public static OkHttpClient.Builder getBaseBuilder() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .readTimeout(10000, TimeUnit.MILLISECONDS)
                .addInterceptor(createHttpLoggingInterceptor())
                .addInterceptor(createNetModeInterceptor());
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
     * 网络模式 拦截器
     *
     * @return
     */
    private static Interceptor createNetModeInterceptor() {
        Interceptor i = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = null;

                String url = chain.request().url().toString();
                if (SPUtil.getBoolean(SPUtil.KEY_NET_MODE, false)) {
                    //如果 是仅wifi 联网模式。判断白名单。
                    if (!netModeWhiteList.isEmpty() && netModeWhiteList.contains(url)) {
                        //如果此url请求 属于白名单，则继续进行网络请求。
                        L.e("白名单：" + url + "继续网络请求");
//                        proceed(response, chain);
                        response = chain.proceed(chain.request());
                        if (!response.isSuccessful()) {
                            L.e("与服务器连接失败" + response.header(Header.RESPONSE_STATUS.toString()));
                        }
                    } else {
                        //如果不属于白名单,则终止请求
                        L.e(url + "被终止网络请求");
                        return response;
                    }
                } else {
                    //如果是非仅wifi联网模式，正常请求
                    L.e(url + "非仅Wifi模式，继续请求");
                    proceed(response, chain);
                }
                return response;
            }

            /**
             * 正常进行请求
             * @param response
             * @param chain
             * @throws IOException
             */
            private void proceed(Response response, Chain chain) throws IOException {
                response = chain.proceed(chain.request());
                if (!response.isSuccessful()) {
                    L.e("与服务器连接失败" + response.header(Header.RESPONSE_STATUS.toString()));
                }
            }
        };
        return i;
    }

    /**
     * 网络模式白名单
     */
    private static ArrayList<String> netModeWhiteList = new ArrayList<>();

    /**
     * 添加网络模式白名单。即不管是什么模式，什么网络，白名单中的url的请求会进行网络请求
     *
     * @param url
     */
    public static void addNetModeWhiteList(String url) {
        netModeWhiteList.add(ApiDefine.HOST_BASE_URL + url);
    }

}

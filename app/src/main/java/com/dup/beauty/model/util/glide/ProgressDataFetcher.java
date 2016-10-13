package com.dup.beauty.model.util.glide;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GlideUrl;
import com.dup.beauty.model.util.HttpUtil;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @see com.bumptech.glide.load.data.HttpUrlFetcher
 * @see <a href="https://github.com/square/okhttp/blob/master/samples/guide/src/main/java/okhttp3/recipes/Progress.java">OkHttp sample</a>
 */
public class ProgressDataFetcher implements DataFetcher<InputStream> {

    private final String url;
    private final ProgressListener listener;

    private Call progressCall;
    private InputStream stream;
    private volatile boolean isCancelled;

    public ProgressDataFetcher(String url, ProgressListener listener) {
        this.url = url;
        this.listener = listener;
    }

    @Override
    public InputStream loadData(Priority priority) throws Exception {
        Request request = new Request.Builder()
                .url(url)
                .build();

        OkHttpClient client = getBaseHttpClient().addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                if (listener == null) {
                    return chain.proceed(chain.request());
                } else {
                    Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                            .body(new ProgressResponseBody(url,originalResponse.body(), listener))
                            .build();
                }
            }
        }).build();

        try {
            progressCall = client.newCall(request);
            Response response = progressCall.execute();
            if (isCancelled) {
                return null;
            }
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            stream = response.body().byteStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stream;
    }

    @Override
    public void cleanup() {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                // Ignore
            }
        }
        if (progressCall != null) {
            progressCall.cancel();
        }
    }

    @Override
    public String getId() {
        return url;
    }

    @Override
    public void cancel() {
        // TODO: we should consider disconnecting the url connection here, but we can't do so directly because cancel is
        // often called on the main thread.
        isCancelled = true;
        if (progressCall != null) {
            progressCall.cancel();
        }
    }

    /**
     * 获取到自定义的OkhttpClient
     *
     * @return
     */
    private OkHttpClient.Builder getBaseHttpClient() {
        return HttpUtil.getBaseBuilder();
    }
}

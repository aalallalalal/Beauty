package com.dup.beauty.model.util.glide;

import com.dup.beauty.util.L;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * @author "https://github.com/square/okhttp/blob/master/samples/guide/src/main/java/okhttp3/recipes/Progress.java"
 * @see <a href="https://github.com/square/okhttp/blob/master/samples/guide/src/main/java/okhttp3/recipes/Progress.java">OkHttp sample</a>
 */
class ProgressResponseBody extends ResponseBody {

    private final ResponseBody responseBody;
    private final ProgressListener progressListener;
    private BufferedSource bufferedSource;
    private String url;

    public ProgressResponseBody(String url, ResponseBody responseBody, ProgressListener progressListener) {
        this.responseBody = responseBody;
        this.progressListener = progressListener;
        this.url = url;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead=0;
                try {
                    bytesRead = super.read(sink, byteCount);
                    if (responseBody.contentLength() > 0) {
                        // read() returns the number of bytes read, or -1 if this source is exhausted.
                        totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                        progressListener.update(url, totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                    } else {
                        progressListener.update(url, 0, -1, bytesRead == -1);
                    }
                } catch (IOException e) {
                    L.e("处理进度流时IoException");
                }
                return bytesRead;
            }
        };
    }
}

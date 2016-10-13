package com.dup.beauty.model.util.glide;
/**
 * @author  "https://github.com/square/okhttp/blob/master/samples/guide/src/main/java/okhttp3/recipes/Progress.java"
 * @see <a href="https://github.com/square/okhttp/blob/master/samples/guide/src/main/java/okhttp3/recipes/Progress.java">OkHttp sample</a>
 */
public interface ProgressListener {
    void update(String url, long bytesRead, long contentLength, boolean done);
}

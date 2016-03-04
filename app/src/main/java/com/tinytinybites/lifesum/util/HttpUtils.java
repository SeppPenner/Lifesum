package com.tinytinybites.lifesum.util;

import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by bundee on 3/1/16.
 */
public class HttpUtils {
    //Tag
    private static final String TAG = HttpUtils.class.getCanonicalName();

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient sOkHttpClient = new OkHttpClient();

    private static Call makePostRequest(String url, List<Pair<String, String>> headers,
                                        RequestBody body, Callback callback) {
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(body);
        addHeaders(builder, headers);
        Request request = builder.build();
        Call call = sOkHttpClient.newCall(request);
        call.enqueue(callback);
        return call;
    }

    public static Call get(String url, List<Pair<String, String>> headers, Callback callback) {
        Log.d(TAG, "GET " + url);
        Request.Builder builder = new Request.Builder()
                .url(url)
                .get();
        addHeaders(builder, headers);
        Request request = builder.build();
        Call call = sOkHttpClient.newCall(request);
        call.enqueue(callback);
        return call;
    }

    private static void addHeaders(Request.Builder builder, List<Pair<String, String>> headers) {
        if (headers != null) {
            for (Pair<String, String> header : headers) {
                if (!TextUtils.isEmpty(header.first) && !TextUtils.isEmpty(header.second)) {
                    builder.addHeader(header.first, header.second);
                }
            }
        }
    }

    public static Call get(String url, Callback callback) {
        return get(url, null, callback);
    }

    public static Call post(String url, List<Pair<String, String>> headers, String content,
                            Callback callback) {
        RequestBody body = RequestBody.create(JSON, content);
        return makePostRequest(url, headers, body, callback);
    }
}

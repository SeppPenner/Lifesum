package com.tinytinybites.lifesum.core;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by bundee on 2/29/16.
 * An implementation of {@link Callback} as per okhttp library
 */
public class ApiCallback implements Callback {
    //Tag
    private static final String TAG = ApiCallback.class.getCanonicalName();

    //Statics
    private static final Gson GSON = new GsonBuilder().setFieldNamingStrategy(new LifeSumFieldNamingStrategy()).create();

    //Variables
    private Class<? extends Api.Response> mResponseClass;

    public ApiCallback() {
    }

    public ApiCallback(Class responseClass) {
        mResponseClass = responseClass;
    }

    /**
     * Called when the network request was successful.
     *
     * @param response The JSON response, parsed into object of mResponseClass
     */
    public void onSuccess(Api.Response response) {
    }

    /**
     * Called when the network request was successful.
     *
     * @param body
     */
    public void onSuccess(JSONObject body) {
    }

    /**
     * Called when the network request was unsuccessful. code will be the HTTP response code. For network failures,
     * body will be the request url, otherwise the HTTP response body.
     *
     * @param code
     * @param body
     */
    public void onError(int code, String body) {
        if (code >= 400 && code <= 499) {
            try {
                JSONObject requestError = new JSONObject(body);
                onParsedError(requestError.getString("title"), requestError.getString("description"));
            } catch (JSONException e) {
                Log.e(TAG, "Error response not in standard JSON format. Response received: " + body);
            }
        } else if (code >= 500 && code <= 599) {
            Log.w(TAG, "Internal error (" + code + "): " + body);
            onParsedError("Error", "Internal error (" + code + ")");
        }
    }

    /**
     * Called when onError can parse the error
     * @param title
     * @param description
     */
    public void onParsedError(String title, String description) {

    }

    private void finish(int code, String body) {
        if (code >= 200 && code <= 299) {
            try {
                if (mResponseClass != null && Api.Response.class.isAssignableFrom(mResponseClass)) {
                    Log.d(TAG, body);
                    Api.Response responseObject = GSON.fromJson(body, mResponseClass);
                    if (responseObject.validate()) {
                        onSuccess(responseObject);
                    } else {
                        onError(code, body);
                    }
                } else {
                    onSuccess(new JSONObject(body));
                }
            } catch (JSONException e) {
                onError(code, body);
            }
        } else {
            onError(code, body);
        }
    }

    @Override
    public void onFailure(okhttp3.Call call, IOException e) {
        Log.e(TAG, "onFailure", e);
        finish(-1, call.request().url().toString());
    }

    @Override
    public void onResponse(okhttp3.Call call, Response response) throws IOException {
        final int code = response.code();
        final String body = response.body().string();
        Log.d(TAG, "onResponse: " + code + " " + response.message());
        finish(code, body);
    }
}


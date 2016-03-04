package com.tinytinybites.lifesum.core;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tinytinybites.lifesum.util.HttpUtils;

/**
 * Created by bundee on 2/29/16.
 */
public class Api {
    //Tag
    private static final String TAG = Api.class.getCanonicalName();

    //Statics
    private static final String HEADER_ACCESS_TOKEN = "Authorization";
    private static final String BASE_API_URL = "https://api.lifesum.com/icebox/v1";
    private static final Gson GSON = new GsonBuilder().create();
    private static final String ACCESS_TOKEN = "14256468:79c695b7608b6fc6c0862b7d002dfb08a475c2efa8bc6a1461a5f3ab54b0ed71";

    public interface Request {
        boolean validate();

        List<String> getErrors();
    }

    public interface Response {
        boolean validate();
    }

    public static String getBaseUrl() {
        return BASE_API_URL;
    }

    private static List<Pair<String, String>> getHeaders() {
        List<Pair<String, String>> headers = new ArrayList<>();
        headers.add(new Pair<>(HEADER_ACCESS_TOKEN, ACCESS_TOKEN));
        return headers;
    }

    private static void makeGetRequest(String route, ApiCallback callback) {
        HttpUtils.get(BASE_API_URL + route, getHeaders(), callback);
    }

    private static void makePostRequest(String route, Request req, ApiCallback callback) {
        HttpUtils.post(BASE_API_URL + route, getHeaders(), GSON.toJson(req), callback);
    }

    public static void getFoodSuggestions(String food, ApiCallback callback) {
        makeGetRequest("/foods/en/se/" + food, callback);
    }

}

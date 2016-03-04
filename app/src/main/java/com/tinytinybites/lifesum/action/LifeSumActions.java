package com.tinytinybites.lifesum.action;

/**
 * Created by bundee on 3/1/16.
 * Definitions on possible actions and bundle key mappings
 */
public interface LifeSumActions {
    //Actions identifiers
    String LIFESUM_STORE = "lifesum-store";
    String LIFESUM_UNSTORE = "lifesum-unstore";
    String LIFESUM_GET_FOOD_SUGGESTIONS = "lifesum-get-food-suggestions";
    String LIFESUM_GET_STORED_FOOD = "lifesum-get-stored_food";

    //Bundle keys
    String KEY_FOOD = "key-food";
    String KEY_FOODS = "key-foods";
    String KEY_SUGGESTIONS = "key-suggestions";
    String KEY_SERVING_CATEGORIES = "key-serving-categories";
    String KEY_SERVING_SIZES = "key-serving-sizes";
    String KEY_TEXT = "key-text";
    String KEY_ID = "key-id";
    String KEY_ERROR = "key-error";
}

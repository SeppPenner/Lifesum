package com.tinytinybites.lifesum.core;

import java.util.List;
import com.tinytinybites.lifesum.model.Food;
import com.tinytinybites.lifesum.model.ServingCategory;
import com.tinytinybites.lifesum.model.ServingSize;

/**
 * Created by bundee on 3/1/16.
 */
public class GetSuggestionResponse implements Api.Response{
    List<Food> mFood;
    List<ServingCategory> mServingCategories;
    List<ServingSize> mServingSizes;
    String mTitleRequested;

    public List<Food> getFoodSuggestions() {
        return mFood;
    }

    public void setFoodSuggestions(List<Food> foodSuggestions) {
        mFood = foodSuggestions;
    }

    public List<ServingCategory> getServingCategories() {
        return mServingCategories;
    }

    public void setServingCategories(List<ServingCategory> mServingCategories) {
        this.mServingCategories = mServingCategories;
    }

    public List<ServingSize> getServingSizes() {
        return mServingSizes;
    }

    public void setServingSizes(List<ServingSize> mServingSizes) {
        this.mServingSizes = mServingSizes;
    }

    public String getTitleRequested() {
        return mTitleRequested;
    }

    public void setTitleRequested(String mTitleRequested) {
        this.mTitleRequested = mTitleRequested;
    }

    @Override
    public boolean validate() {
        return true;
    }
}

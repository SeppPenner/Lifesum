package com.tinytinybites.lifesum.store;

import java.util.ArrayList;
import java.util.List;
import com.squareup.otto.Subscribe;
import com.tinytinybites.lifesum.action.Action;
import com.tinytinybites.lifesum.action.LifeSumActions;
import com.tinytinybites.lifesum.dispatcher.Dispatcher;
import com.tinytinybites.lifesum.event.FoodStoreChangeEvent;
import com.tinytinybites.lifesum.model.Food;
import com.tinytinybites.lifesum.model.ServingCategory;
import com.tinytinybites.lifesum.model.ServingSize;

/**
 * Created by bundee on 3/1/16.
 */
public class FoodStore extends Store{
    //Tag
    private static final String TAG = FoodStore.class.getCanonicalName();

    //Variables
    private static FoodStore _instance;
    private final List<Food> mFoods;
    private final List<ServingCategory> mCategories;
    private final List<ServingSize> mSizes;

    protected FoodStore(Dispatcher dispatcher) {
        super(dispatcher);
        mFoods = new ArrayList<>();
        mCategories = new ArrayList<>();
        mSizes = new ArrayList<>();
    }

    public static FoodStore get(Dispatcher dispatcher) {
        if (_instance == null) {
            _instance = new FoodStore(dispatcher);
        }
        return _instance;
    }

    public List<Food> getFoodSuggestions() {
        return mFoods;
    }

    public List<ServingCategory> getCategories(){
        return mCategories;
    }

    public List<ServingSize> getSizes(){
        return mSizes;
    }


    @Override
    @Subscribe
    @SuppressWarnings("unchecked")
    public void onAction(Action action) {
        switch (action.getType()) {
            case LifeSumActions.LIFESUM_GET_FOOD_SUGGESTIONS:
                //Sanity check
                if(action.getData().containsKey(LifeSumActions.KEY_ERROR)){
                    dispatcher.emitChange(errorChangeEvent(LifeSumActions.LIFESUM_GET_FOOD_SUGGESTIONS, (String) action.getData().get(LifeSumActions.KEY_ERROR)));
                    break;
                }

                mCategories.clear();
                mCategories.addAll((List<ServingCategory>) action.getData().get(LifeSumActions.KEY_SERVING_CATEGORIES));

                mSizes.clear();
                mSizes.addAll((List<ServingSize>) action.getData().get(LifeSumActions.KEY_SERVING_SIZES));

                mFoods.clear();
                mFoods.addAll((List<Food>) action.getData().get(LifeSumActions.KEY_SUGGESTIONS));

                emitStoreChange();
                break;
        }
    }

    @Override
    StoreChangeEvent changeEvent() {
        return new FoodStoreChangeEvent();
    }

    @Override
    StoreChangeEvent errorChangeEvent(String actionType, String errorMessage) {
        return new FoodStoreChangeEvent(actionType, errorMessage);
    }

}

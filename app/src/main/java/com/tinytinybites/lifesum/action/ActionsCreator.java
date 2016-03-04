package com.tinytinybites.lifesum.action;

import java.io.IOException;
import java.util.List;
import com.tinytinybites.lifesum.application.LifesumApplication;
import com.tinytinybites.lifesum.core.Api;
import com.tinytinybites.lifesum.core.ApiCallback;
import com.tinytinybites.lifesum.core.GetSuggestionResponse;
import com.tinytinybites.lifesum.dispatcher.Dispatcher;
import com.tinytinybites.lifesum.model.Food;
import okhttp3.Call;

/**
 * Created by bundee on 3/1/16.
 *
 */
public class ActionsCreator {
    //Tag
    private static final String TAG = ActionsCreator.class.getCanonicalName();

    private static ActionsCreator instance;
    final Dispatcher dispatcher;

    ActionsCreator(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public static ActionsCreator get(Dispatcher dispatcher) {
        if (instance == null) {
            instance = new ActionsCreator(dispatcher);
        }
        return instance;
    }

    public void storeFood(Food food) {
        dispatcher.dispatch(
                LifeSumActions.LIFESUM_STORE,
                LifeSumActions.KEY_FOOD, food
        );
    }

    public void unstoreFood(long id) {
        dispatcher.dispatch(
                LifeSumActions.LIFESUM_UNSTORE,
                LifeSumActions.KEY_ID, id
        );
    }

    public void getStoredFood(){
        List<Food> storedFoods = LifesumApplication.getDaoSessionInstance().getFoodDao().loadAll();

        dispatcher.dispatch(
                LifeSumActions.LIFESUM_GET_STORED_FOOD,
                LifeSumActions.KEY_FOODS, storedFoods
        );
    }

    public void getSuggestions(String term){
        //Get data from api
        Api.getFoodSuggestions(term, new ApiCallback(GetSuggestionResponse.class) {
            @Override
            public void onFailure(Call call, IOException e) {
                super.onFailure(call, e);
                dispatcher.dispatch(
                        LifeSumActions.LIFESUM_GET_FOOD_SUGGESTIONS,
                        LifeSumActions.KEY_ERROR, e.toString()
                );
            }

            @Override
            public void onParsedError(String title, String description) {
                super.onParsedError(title, description);

                dispatcher.dispatch(
                        LifeSumActions.LIFESUM_GET_FOOD_SUGGESTIONS,
                        LifeSumActions.KEY_ERROR, title + " - " + description
                );
            }

            @Override
            public void onSuccess(Api.Response response) {
                super.onSuccess(response);
                final GetSuggestionResponse resp = (GetSuggestionResponse) response;

                dispatcher.dispatch(
                        LifeSumActions.LIFESUM_GET_FOOD_SUGGESTIONS,
                        LifeSumActions.KEY_SUGGESTIONS, resp.getFoodSuggestions(),
                        LifeSumActions.KEY_SERVING_CATEGORIES, resp.getServingCategories(),
                        LifeSumActions.KEY_SERVING_SIZES, resp.getServingSizes(),
                        LifeSumActions.KEY_TEXT, resp.getTitleRequested()
                );
            }
        });
    }
}

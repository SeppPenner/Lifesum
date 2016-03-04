package com.tinytinybites.lifesum.store;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.squareup.otto.Subscribe;
import com.tinytinybites.lifesum.action.Action;
import com.tinytinybites.lifesum.action.LifeSumActions;
import com.tinytinybites.lifesum.application.LifesumApplication;
import com.tinytinybites.lifesum.dispatcher.Dispatcher;
import com.tinytinybites.lifesum.event.StoredFoodChangeEvent;
import com.tinytinybites.lifesum.model.Food;
import com.tinytinybites.lifesum.model.ServingCategory;
import com.tinytinybites.lifesum.model.ServingSize;

/**
 * Created by bundee on 3/1/16.
 */
public class StoredFoodStore extends Store{
    //Tag
    private static final String TAG = StoredFoodStore.class.getCanonicalName();

    //Variables
    private static StoredFoodStore _instance;
    private final List<Food> mStoredFoods;
    private boolean mInitialLoad = false;

    protected StoredFoodStore(Dispatcher dispatcher) {
        super(dispatcher);
        mStoredFoods = new ArrayList<>();
    }

    public static StoredFoodStore get(Dispatcher dispatcher) {
        if (_instance == null) {
            _instance = new StoredFoodStore(dispatcher);
        }
        return _instance;
    }

    public List<Food> getStoredFoods(){ return mStoredFoods;}

    public boolean foodIsStored(long id){
        for(Food food: mStoredFoods){
            if(food.getId() == id){
                return true;
            }
        }
        return false;
    }

    public boolean loadedStoredFood(){
        if(mStoredFoods.size() > 0 || mInitialLoad){
            return true;
        }
        return false;
    }

    @Override
    @Subscribe
    @SuppressWarnings("unchecked")
    public void onAction(Action action) {
        switch (action.getType()) {
            case LifeSumActions.LIFESUM_STORE:
                Food food = ((Food) action.getData().get(LifeSumActions.KEY_FOOD));
                storeFood(food);

                emitStoreChange();
                break;
            case LifeSumActions.LIFESUM_UNSTORE:
                long id = ((long)action.getData().get(LifeSumActions.KEY_ID));
                unstoreFood(id);

                emitStoreChange();
                break;
            case LifeSumActions.LIFESUM_GET_STORED_FOOD:
                mStoredFoods.clear();
                mStoredFoods.addAll((List<Food>) action.getData().get(LifeSumActions.KEY_FOODS));

                mInitialLoad = true;

                emitStoreChange();
                break;
        }
    }

    /**
     * Store a Food entry into db. Also stores its related ServingCategory and ServingSize if not already in db
     * TODO: Update ServingCategory and ServingSize even if already in db
     * @param food
     */
    private void storeFood(Food food) {
        //Check if we already saved category and serving sizes
        if(food.getServingCategory() != null && food.getServingCategory().getOid() != -1L){
            ServingCategory category = LifesumApplication.getDaoSessionInstance().getServingCategoryDao().load(food.getServingCategory().getOid());
            if(category == null){
                //Save new entry as it doesnt seem to be in db
                LifesumApplication.getDaoSessionInstance().getServingCategoryDao().insert(food.getServingCategory());
            }
        }else{
            food.setServingCategory(null);
        }
        if(food.getServingSize() != null && food.getServingSize().getOid() != -1L){
            ServingSize size = LifesumApplication.getDaoSessionInstance().getServingSizeDao().load(food.getServingSize().getOid());
            if(size == null){
                //Save new entry for serving size
                LifesumApplication.getDaoSessionInstance().getServingSizeDao().insert(food.getServingSize());
            }
        }else{
            food.setServingSize(null);
        }

        Food foundFood = LifesumApplication.getDaoSessionInstance().getFoodDao().load(food.getId());
        if(foundFood == null){
            //Save to db
            LifesumApplication.getDaoSessionInstance().getFoodDao().insert(food);
        }

        mStoredFoods.add(food);
    }

    /**
     * Remove Food from db.
     * Ignore ServingCategory and ServingSize, leave it in db as they might be used by other Food
     * TODO: Can attempt to remove redundant category and size by detecting FK constraints when deleting
     * @param id
     */
    public void unstoreFood(long id){
        LifesumApplication.getDaoSessionInstance().getFoodDao().deleteByKey(id);

        Iterator<Food> iter = mStoredFoods.iterator();
        while (iter.hasNext()) {
            Food food = iter.next();
            if (food.getId() == id) {
                iter.remove();
                break;
            }
        }
    }

    @Override
    StoreChangeEvent changeEvent() {
        return new StoredFoodChangeEvent();
    }

    @Override
    StoreChangeEvent errorChangeEvent(String errorType, String errorMessage) {
        return new StoredFoodChangeEvent(); //TODO: Need error event type
    }

}

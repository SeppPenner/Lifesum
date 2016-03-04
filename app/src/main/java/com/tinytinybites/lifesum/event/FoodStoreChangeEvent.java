package com.tinytinybites.lifesum.event;

import com.tinytinybites.lifesum.store.Store;

/**
 * Created by bundee on 3/3/16.
 */
public class FoodStoreChangeEvent implements Store.StoreChangeEvent{
    //Variables
    private String mActionType;
    private String mErrorMessage;

    public FoodStoreChangeEvent() {
    }

    public FoodStoreChangeEvent(String actionType, String errorMessage){
        this.mActionType = actionType;
        this.mErrorMessage = errorMessage;
    }

    public boolean hasError(){
        return (mErrorMessage != null);
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }
}

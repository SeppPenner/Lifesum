package com.tinytinybites.lifesum.store;

import com.tinytinybites.lifesum.action.Action;
import com.tinytinybites.lifesum.dispatcher.Dispatcher;

/**
 * Created by bundee on 3/1/16.
 */
public abstract class Store {

    final Dispatcher dispatcher;

    protected Store(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    void emitStoreChange() {
        dispatcher.emitChange(changeEvent());
    }

    abstract StoreChangeEvent changeEvent();

    abstract StoreChangeEvent errorChangeEvent(String errorType, String errorMessage);

    public abstract void onAction(Action action);

    public interface StoreChangeEvent {}

}

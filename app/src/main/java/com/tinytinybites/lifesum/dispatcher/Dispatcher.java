package com.tinytinybites.lifesum.dispatcher;

import com.squareup.otto.Bus;
import com.tinytinybites.lifesum.action.Action;
import com.tinytinybites.lifesum.store.Store;

/**
 * Created by bundee on 3/1/16.
 */
public class Dispatcher {
    private final Bus mBus;
    private static Dispatcher _instance;

    public static Dispatcher get(Bus bus) {
        if (_instance == null) {
            _instance = new Dispatcher(bus);
        }
        return _instance;
    }

    Dispatcher(Bus bus) {
        this.mBus = bus;
    }

    public void register(final Object cls) {
        mBus.register(cls);
    }

    public void unregister(final Object cls) {
        mBus.unregister(cls);
    }

    public void emitChange(Store.StoreChangeEvent o) {
        post(o);
    }

    public void dispatch(String type, Object... data) {
        if (isEmpty(type)) {
            throw new IllegalArgumentException("Type must not be empty");
        }

        if (data.length % 2 != 0) {
            throw new IllegalArgumentException("Data must be a valid list of key,value pairs");
        }

        Action.Builder actionBuilder = Action.type(type);
        int i = 0;
        while (i < data.length) {
            String key = (String) data[i++];
            Object value = data[i++];
            actionBuilder.bundle(key, value);
        }
        post(actionBuilder.build());
    }

    private boolean isEmpty(String type) {
        return type == null || type.isEmpty();
    }

    private void post(final Object event) {
        mBus.post(event);
    }
}

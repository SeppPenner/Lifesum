package com.tinytinybites.lifesum.application;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tinytinybites.lifesum.dispatcher.Dispatcher;
import com.tinytinybites.lifesum.dispatcher.LifeSumBus;
import com.tinytinybites.lifesum.model.DaoMaster;
import com.tinytinybites.lifesum.model.DaoSession;
import com.tinytinybites.lifesum.store.FoodStore;
import com.tinytinybites.lifesum.store.StoredFoodStore;

/**
 * Created by bundee on 3/1/16.
 */
public class LifesumApplication extends Application {
    //Tag
    private static final String TAG = LifesumApplication.class.getCanonicalName();

    //Variables
    protected static LifesumApplication _instance;
    protected static SQLiteDatabase _dbInstance;
    protected static DaoMaster _daoMasterInstance;
    protected static DaoSession _daoSessionInstance;
    protected static DaoMaster.DevOpenHelper _helperInstance;
    private Dispatcher mDispatcher;


    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;

        getSQLiteDatabaseInstance();
        getDaoMasterInstance();
        getDaoSessionInstance();

        //Register once
        mDispatcher = Dispatcher.get(new LifeSumBus());
        mDispatcher.register(FoodStore.get(mDispatcher));
        mDispatcher.register(StoredFoodStore.get(mDispatcher));
    }

    @Override
    public void onTerminate() {
        mDispatcher.unregister(FoodStore.get(mDispatcher));

        super.onTerminate();

        cleanup();
    }

    /**
     * Get singleton instance of db
     * @return
     */
    public static SQLiteDatabase getSQLiteDatabaseInstance(){
        if(_dbInstance == null){
            _helperInstance = new DaoMaster.DevOpenHelper(_instance, "task-db", null);
            _dbInstance = _helperInstance.getWritableDatabase();
        }
        return _dbInstance;
    }

    /**
     * Get singleton instance of dao master
     * @return
     */
    public static DaoMaster getDaoMasterInstance(){
        if(_daoMasterInstance == null){
            _daoMasterInstance = new DaoMaster(getSQLiteDatabaseInstance());
        }
        return _daoMasterInstance;
    }

    /**
     * Get singleton instance of dao session
     * @return
     */
    public static DaoSession getDaoSessionInstance(){
        if(_daoSessionInstance == null){
            _daoSessionInstance = getDaoMasterInstance().newSession();
        }
        return _daoSessionInstance;
    }

    /**
     * Get singleton instance of application class
     * @return
     */
    public static LifesumApplication getInstance(){
        if(_instance == null){
            RuntimeException t = new RuntimeException("Application not initialized");
            Log.e(TAG, "Failed to initialize app. Check manifest maybe? " + t.toString());
            throw t;
        }
        return _instance;
    }

    /**
     * General clean up
     */
    public void cleanup(){
        _daoSessionInstance.clear();
        _dbInstance.close();
    }

}

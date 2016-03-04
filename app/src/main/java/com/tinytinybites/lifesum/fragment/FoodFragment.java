package com.tinytinybites.lifesum.fragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.Serializable;
import com.squareup.otto.Subscribe;
import com.tinytinybites.lifesum.R;
import com.tinytinybites.lifesum.action.ActionsCreator;
import com.tinytinybites.lifesum.activity.IntentExtras;
import com.tinytinybites.lifesum.dispatcher.Dispatcher;
import com.tinytinybites.lifesum.dispatcher.LifeSumBus;
import com.tinytinybites.lifesum.event.StoredFoodChangeEvent;
import com.tinytinybites.lifesum.model.Food;
import com.tinytinybites.lifesum.store.StoredFoodStore;

/**
 * Created by bundee on 3/3/16.
 */
public class FoodFragment extends BaseFragment{
    //Tag
    private static final String TAG = FoodFragment.class.getCanonicalName();

    //Variables
    private Dispatcher mDispatcher;
    private ActionsCreator mActionsCreator;
    private StoredFoodStore mStoredFoodStore;
    private Food mFood;
    private LayoutInflater mLayoutInflater;

    //Ui Elements
    private View mRoot;
    private TextView mProteinValue;
    private TextView mCarbsValue;
    private TextView mFatValue;
    private TextView mCalories;
    private TextView mServing;
    private LinearLayout mProteinContainer;
    private LinearLayout mCarbsContainer;
    private LinearLayout mFatContainer;
    private LinearLayout mOtherContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupDependencies();

        setHasOptionsMenu(true);

        mLayoutInflater = getActivity().getLayoutInflater();

        //TODO: Need more fault tolerence here
        Serializable obj = getActivity().getIntent().getSerializableExtra(IntentExtras.FOOD);
        if(obj != null){
            if(obj instanceof Food) {
                mFood = (Food) obj;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mDispatcher.register(this);

        if(mFood != null){
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(mFood.getTitle());

            int measurementMultiplier = mFood.getTypeofmeasurement() == 2 ? 100 : 1;

            mCalories.setText((mFood.getCalories() / measurementMultiplier) + " cals");
            mServing.setText(mFood.getServingSize().getName());
            mProteinValue.setText((mFood.getProtein() / measurementMultiplier) + " g");
            mCarbsValue.setText((mFood.getCarbohydrates() / measurementMultiplier) + " g");
            mFatValue.setText((mFood.getFat() / measurementMultiplier) + " g");

            //Setup finer nutritional details
            mCarbsContainer.removeAllViews();
            mCarbsContainer.addView(setupNewNutritionEntry("Fiber", (mFood.getFiber() / measurementMultiplier) + " g"));
            mCarbsContainer.addView(setupNewNutritionEntry("Sugars", (mFood.getSugar() / measurementMultiplier) + " g"));
            mFatContainer.removeAllViews();
            mFatContainer.addView(setupNewNutritionEntry("Saturated fat", (mFood.getSaturatedfat() / measurementMultiplier) + " g"));
            mFatContainer.addView(setupNewNutritionEntry("Unsaturated fat", (mFood.getUnsaturatedfat() / measurementMultiplier) + " g"));
            mOtherContainer.removeAllViews();
            mOtherContainer.addView(setupNewNutritionEntry("Cholesterol", (mFood.getCholesterol() / measurementMultiplier) + " g"));
            mOtherContainer.addView(setupNewNutritionEntry("Sodium", (mFood.getSodium() / measurementMultiplier) + " g"));
            mOtherContainer.addView(setupNewNutritionEntry("Potassium", (mFood.getPotassium() / measurementMultiplier) + " g"));

        }else{
            //TODO: fetch
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mDispatcher.unregister(this);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if(mStoredFoodStore == null){
            (menu.findItem(R.id.action_food_store)).setVisible(false);
        }else{
            //Check if food has been stored
            if(mStoredFoodStore.loadedStoredFood()){
                (menu.findItem(R.id.action_food_store)).setVisible(true);
                if(mStoredFoodStore.foodIsStored(mFood.getId())){
                    (menu.findItem(R.id.action_food_store)).setIcon(R.drawable.ico_stored);
                }else{
                    (menu.findItem(R.id.action_food_store)).setIcon(R.drawable.ico_unstored);
                }
            }else{
                (menu.findItem(R.id.action_food_store)).setVisible(false);

                //Load stored food
                mActionsCreator.getStoredFood();
            }
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_food_store){
            if(mStoredFoodStore.foodIsStored(mFood.getId())){
                //Unstore it
                mActionsCreator.unstoreFood(mFood.getId());
            }else{
                //Store it
                mActionsCreator.storeFood(mFood);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_food, menu);
    }

    private void setupDependencies(){
        mDispatcher = Dispatcher.get(new LifeSumBus());
        mActionsCreator = ActionsCreator.get(mDispatcher);
        mStoredFoodStore = StoredFoodStore.get(mDispatcher);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_food, container, false);

        mProteinValue = (TextView) mRoot.findViewById(R.id.proteinValue);
        mCalories = (TextView) mRoot.findViewById(R.id.calories);
        mCarbsValue = (TextView) mRoot.findViewById(R.id.carbValue);
        mServing = (TextView) mRoot.findViewById(R.id.serving);
        mFatValue = (TextView) mRoot.findViewById(R.id.fatValue);
        mProteinContainer = (LinearLayout) mRoot.findViewById(R.id.proteinContainer);
        mCarbsContainer = (LinearLayout) mRoot.findViewById(R.id.carbsContainer);
        mFatContainer = (LinearLayout) mRoot.findViewById(R.id.fatContainer);
        mOtherContainer = (LinearLayout) mRoot.findViewById(R.id.otherContainer);

        return mRoot;
    }

    /**
     * Generate a view that represents a nutrition entry with label and value
     * @param label
     * @param value
     * @return
     */
    public View setupNewNutritionEntry(String label, String value){
        RelativeLayout newEntry = (RelativeLayout) mLayoutInflater.inflate(R.layout.nutrition_row_layout, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        newEntry.setLayoutParams(lp);

        //Set new entry with the right data
        ((TextView)newEntry.findViewById(R.id.label)).setText(label);
        ((TextView)newEntry.findViewById(R.id.value)).setText(value);

        return newEntry;
    }

    @Subscribe
    public void onStoredFoodChangeEvent(StoredFoodChangeEvent event) {
        getActivity().invalidateOptionsMenu();
    }
}

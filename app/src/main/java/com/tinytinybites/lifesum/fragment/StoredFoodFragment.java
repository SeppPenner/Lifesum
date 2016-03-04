package com.tinytinybites.lifesum.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.squareup.otto.Subscribe;
import com.tinytinybites.lifesum.R;
import com.tinytinybites.lifesum.action.ActionsCreator;
import com.tinytinybites.lifesum.activity.FoodActivity;
import com.tinytinybites.lifesum.activity.IntentExtras;
import com.tinytinybites.lifesum.adapter.StoredFoodRecyclerAdapter;
import com.tinytinybites.lifesum.dispatcher.Dispatcher;
import com.tinytinybites.lifesum.dispatcher.LifeSumBus;
import com.tinytinybites.lifesum.event.StoredFoodChangeEvent;
import com.tinytinybites.lifesum.store.StoredFoodStore;

/**
 * Created by bundee on 3/3/16.
 */
public class StoredFoodFragment extends BaseFragment implements StoredFoodRecyclerAdapter.OnItemClickListener {
    //Tag
    private static final String TAG = StoredFoodFragment.class.getCanonicalName();

    private Dispatcher mDispatcher;
    private ActionsCreator mActionsCreator;
    private StoredFoodStore mFoodStore;
    private StoredFoodRecyclerAdapter mListAdapter;

    //Ui
    private ProgressBar mProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupDependencies();
    }

    @Override
    public void onResume() {
        super.onResume();
        mDispatcher.register(this);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Stored Food");

        if(mListAdapter.getItemCount() == 0){
            if(mFoodStore.loadedStoredFood()){
                mListAdapter.setItems(mFoodStore.getStoredFoods());

                mProgress.setVisibility(View.GONE);
            }else{
                mProgress.setVisibility(View.VISIBLE);

                //Load stored food
                mActionsCreator.getStoredFood();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mDispatcher.unregister(this);

        mProgress.setVisibility(View.INVISIBLE);
    }

    private void setupDependencies(){
        mDispatcher = Dispatcher.get(new LifeSumBus());
        mActionsCreator = ActionsCreator.get(mDispatcher);
        mFoodStore = StoredFoodStore.get(mDispatcher);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_stored_food, container, false);

        RecyclerView storedFoodList = (RecyclerView)root.findViewById(R.id.stored_food_list);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        storedFoodList.setLayoutManager(layoutManager);
        mListAdapter = new StoredFoodRecyclerAdapter(mActionsCreator, this);
        storedFoodList.setAdapter(mListAdapter);

        mProgress = (ProgressBar) root.findViewById(R.id.progress);

        return root;
    }

    private void updateUI(){
        mListAdapter.setItems(mFoodStore.getStoredFoods());
        mProgress.setVisibility(View.INVISIBLE);
    }

    @Subscribe
    public void onStoredFoodChangeEvent(StoredFoodChangeEvent event) {
        updateUI();
    }

    @Override
    public void OnItemClick(View view, int position) {
        Intent intent = new Intent(getActivity(), FoodActivity.class);
        intent.putExtra(IntentExtras.FOOD, mListAdapter.getItem(position));
        getActivity().startActivity(intent);
    }
}

package com.tinytinybites.lifesum.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.tinytinybites.lifesum.R;
import com.tinytinybites.lifesum.action.ActionsCreator;
import com.tinytinybites.lifesum.activity.FoodActivity;
import com.tinytinybites.lifesum.activity.IntentExtras;
import com.tinytinybites.lifesum.activity.StoredFoodActivity;
import com.tinytinybites.lifesum.adapter.FoodRecyclerAdapter;
import com.tinytinybites.lifesum.application.LifesumApplication;
import com.tinytinybites.lifesum.dispatcher.Dispatcher;
import com.tinytinybites.lifesum.dispatcher.LifeSumBus;
import com.tinytinybites.lifesum.event.FoodStoreChangeEvent;
import com.tinytinybites.lifesum.store.FoodStore;

/**
 * Created by bundee on 3/3/16.
 */
public class SearchFragment extends BaseFragment implements FoodRecyclerAdapter.OnItemClickListener{
    //Tag
    private static final String TAG = SearchFragment.class.getCanonicalName();

    private Dispatcher mDispatcher;
    private ActionsCreator mActionsCreator;
    private FoodStore mFoodStore;
    private FoodRecyclerAdapter mListAdapter;

    //Ui
    private View mRoot;
    private ImageView mSearchButton;
    private EditText mSearchText;
    private ProgressBar mProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupDependencies();

        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        mDispatcher.register(this);
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
        mFoodStore = FoodStore.get(mDispatcher);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_stored_food){
            getActivity().startActivity(new Intent(getActivity(), StoredFoodActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_search, container, false);

        RecyclerView foodSuggestionList = (RecyclerView)mRoot.findViewById(R.id.food_suggestion_list);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        foodSuggestionList.setLayoutManager(layoutManager);
        mListAdapter = new FoodRecyclerAdapter(mActionsCreator, this);
        foodSuggestionList.setAdapter(mListAdapter);

        mProgress = (ProgressBar) mRoot.findViewById(R.id.progress);
        mSearchText = (EditText) mRoot.findViewById(R.id.search_edit_text);
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });
        mSearchButton = (ImageView)mRoot.findViewById(R.id.search_button);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mSearchText.getText().toString().length() > 0) {
                    performSearch();
                }
            }
        });

        return mRoot;
    }

    private void updateUI(){
        mListAdapter.setItems(mFoodStore.getFoodSuggestions(), mFoodStore.getCategories(), mFoodStore.getSizes());
        mProgress.setVisibility(View.INVISIBLE);
    }

    private void performSearch(){
        mActionsCreator.getSuggestions(mSearchText.getText().toString());
        mProgress.setVisibility(View.VISIBLE);

        //Dismiss keyboard
        InputMethodManager imm = (InputMethodManager) LifesumApplication.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mSearchText.getWindowToken(), 0);
    }

    @Subscribe
    public void onFoodStoreChangeEvent(FoodStoreChangeEvent event) {
        updateUI();

        if(event.hasError()){
            Snackbar snackbar = Snackbar.make(mRoot, "Unable to get food suggestions: " + event.getErrorMessage(), Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }

    @Override
    public void OnItemClick(View view, int position) {
        Intent intent = new Intent(getActivity(), FoodActivity.class);
        intent.putExtra(IntentExtras.FOOD, mListAdapter.getItem(position));
        getActivity().startActivity(intent);
    }
}

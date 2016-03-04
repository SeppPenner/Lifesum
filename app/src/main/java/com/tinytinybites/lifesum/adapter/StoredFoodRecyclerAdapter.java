package com.tinytinybites.lifesum.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import com.tinytinybites.lifesum.R;
import com.tinytinybites.lifesum.action.ActionsCreator;
import com.tinytinybites.lifesum.model.Food;

/**
 * Created by bundee on 3/1/16.
 */
public class StoredFoodRecyclerAdapter extends RecyclerView.Adapter<StoredFoodRecyclerAdapter.ViewHolder>{
    //Tag
    private static final String TAG = StoredFoodRecyclerAdapter.class.getCanonicalName();

    //Variables
    private static ActionsCreator actionsCreator;
    private List<Food> mFoods;
    private OnItemClickListener mOnItemClickListener;

    public StoredFoodRecyclerAdapter(ActionsCreator actionsCreator,
                                     OnItemClickListener listener) {
        this.mFoods = new ArrayList<>();
        this.mOnItemClickListener = listener;
        this.actionsCreator = actionsCreator;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stored_food_row_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.bindView(mFoods.get(i));
    }

    @Override
    public int getItemCount() {
        return mFoods.size();
    }

    public void setItems(List<Food> foods) {
        this.mFoods = foods;

        notifyDataSetChanged();
    }

    public Food getItem(int position){
        return mFoods.get(position);
    }

    public interface OnItemClickListener{
        public void OnItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView foodTitle;
        public TextView calories;
        public TextView serveSize;
        public TextView category;
        public ImageView unstoreButton;

        public ViewHolder(View v) {
            super(v);
            foodTitle = (TextView) v.findViewById(R.id.food_title_text);
            calories = (TextView) v.findViewById(R.id.calories_text);
            serveSize = (TextView) v.findViewById(R.id.serve_size_text);
            category = (TextView) v.findViewById(R.id.food_category);
            unstoreButton = (ImageView) v.findViewById(R.id.unstoreButton);
            unstoreButton.setOnClickListener(this);
            v.setOnClickListener(this);
        }

        public void bindView(final Food food) {
            foodTitle.setText(food.getTitle());
            calories.setText(String.valueOf((food.getCalories() / (food.getTypeofmeasurement() == 2 ? 100 : 1))) + " cals");
            StringBuilder servingBuilder = new StringBuilder(" - ");
            if(food.getPcstext() != null &&
                    food.getPcstext().length() > 0){
                servingBuilder.append("1 " + food.getPcstext());
            }else{
                servingBuilder.append("1 Serving");
            }
            if(food.getGramsperserving() > 0){
                servingBuilder.append(" (" + food.getGramsperserving() + "g)");
            }
            serveSize.setText(servingBuilder.toString());
            category.setText(food.getServingCategory() != null ? food.getServingCategory().getName() : "");
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.unstoreButton:{
                    actionsCreator.unstoreFood(getItem(getPosition()).getId());
                    break;
                }
                default:{
                    if(mOnItemClickListener != null){
                        mOnItemClickListener.OnItemClick(v, getPosition());
                    }
                    break;
                }
            }

        }
    }
}

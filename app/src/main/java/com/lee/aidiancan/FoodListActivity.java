package com.lee.aidiancan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FoodListActivity extends AppCompatActivity implements FoodAdapter.OnFoodItemClickListener {
    private RecyclerView rvFoodList;
    private FoodAdapter foodAdapter;
    private List<FoodItem> foodItems;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        dbHelper = new DatabaseHelper(this);

        Button btnFastFood = findViewById(R.id.btn_fast_food);
        Button btnFruits = findViewById(R.id.btn_fruits);
        Button btnDrinks = findViewById(R.id.btn_drinks);
        ImageView ivCart = findViewById(R.id.iv_cart);
        rvFoodList = findViewById(R.id.rv_food_list);

        setupRecyclerView();
        loadFoodItems("fast_food");

        btnFastFood.setOnClickListener(v -> loadFoodItems("fast_food"));
        btnFruits.setOnClickListener(v -> loadFoodItems("fruits"));
        btnDrinks.setOnClickListener(v -> loadFoodItems("drinks"));
        ivCart.setOnClickListener(v -> {
            Intent intent = new Intent(FoodListActivity.this, CartActivity.class);
            startActivity(intent);
        });
    }

    private void setupRecyclerView() {
        foodItems = new ArrayList<>();
        foodAdapter = new FoodAdapter(foodItems, this);
        rvFoodList.setLayoutManager(new LinearLayoutManager(this));
        rvFoodList.setAdapter(foodAdapter);
    }

    private void loadFoodItems(String category) {
        foodItems.clear();
        switch (category) {
            case "fast_food":
                foodItems.add(new FoodItem("方便面", "19.9", R.drawable.food_noodles));
                foodItems.add(new FoodItem("蛋糕", "9.9", R.drawable.food_cake));
                foodItems.add(new FoodItem("排骨", "14.9", R.drawable.food_ribs));
                break;
            case "fruits":
                foodItems.add(new FoodItem("樱桃", "39.9", R.drawable.fruit_cherry));
                foodItems.add(new FoodItem("柠檬", "15.9", R.drawable.fruit_lemon));
                foodItems.add(new FoodItem("芒果", "17.9", R.drawable.fruit_mango));
                break;
            case "drinks":
                foodItems.add(new FoodItem("橙汁", "4.9", R.drawable.drink_orange));
                foodItems.add(new FoodItem("西柚汁", "6.9", R.drawable.drink_grapefruit));
                foodItems.add(new FoodItem("奶茶", "8.9", R.drawable.drink_milk_tea));
                break;
        }
        foodAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFoodItemClick(FoodItem foodItem) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_FOOD_NAME, foodItem.getName());
        values.put(DatabaseHelper.COLUMN_FOOD_PRICE, Double.parseDouble(foodItem.getPrice()));
        values.put(DatabaseHelper.COLUMN_QUANTITY, 1);
        values.put(DatabaseHelper.COLUMN_FOOD_IMAGE_RES_ID, foodItem.getImageResourceId());
        db.insert(DatabaseHelper.TABLE_CART, null, values);
        Toast.makeText(this, "已添加到购物车", Toast.LENGTH_SHORT).show();
    }
} 
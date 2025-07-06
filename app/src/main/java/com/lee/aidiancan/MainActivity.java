package com.lee.aidiancan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAskAi = findViewById(R.id.btn_ask_ai);
        Button btnCheckFood = findViewById(R.id.btn_check_food);
        ImageView ivDeepseek = findViewById(R.id.iv_deepseek);

        // 加载Deepseek logo
        ivDeepseek.setImageResource(R.drawable.deepseek);

        btnAskAi.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChatActivity.class);
            startActivity(intent);
        });

        btnCheckFood.setOnClickListener(v -> {
            Intent intent = new Intent(this, FoodListActivity.class);
            startActivity(intent);
        });
    }
}
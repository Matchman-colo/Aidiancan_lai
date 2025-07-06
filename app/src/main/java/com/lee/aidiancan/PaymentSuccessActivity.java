package com.lee.aidiancan;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PaymentSuccessActivity extends AppCompatActivity {

    private TextView tvPaymentType;
    private TextView tvPaymentAmount;
    private Button btnBackToMain;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);

        dbHelper = new DatabaseHelper(this);
        
        initViews();
        displayPaymentInfo();
        setupListeners();
        
        // 清空购物车
        clearCart();
    }

    private void initViews() {
        tvPaymentType = findViewById(R.id.tvPaymentType);
        tvPaymentAmount = findViewById(R.id.tvPaymentAmount);
        btnBackToMain = findViewById(R.id.btnBackToMain);
    }

    private void displayPaymentInfo() {
        Intent intent = getIntent();
        String paymentType = intent.getStringExtra("payment_type");
        double amount = intent.getDoubleExtra("amount", 0.0);

        tvPaymentType.setText("支付方式：" + paymentType);
        tvPaymentAmount.setText(String.format("支付金额：¥%.2f", amount));
    }

    private void setupListeners() {
        btnBackToMain.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
    
    /**
     * 清空购物车
     */
    private void clearCart() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_CART, null, null);
        db.close();
    }
} 
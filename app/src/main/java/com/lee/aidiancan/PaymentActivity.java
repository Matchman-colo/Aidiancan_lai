package com.lee.aidiancan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivity extends AppCompatActivity {
    private static final int PAYMENT_TYPE_WECHAT = 1;
    private static final int PAYMENT_TYPE_ALIPAY = 2;
    private int selectedPaymentType = PAYMENT_TYPE_WECHAT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // 返回按钮
        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        // 设置金额
        String price = getIntent().getStringExtra("price");
        TextView tvTotalAmount = findViewById(R.id.tv_total_amount);
        tvTotalAmount.setText("¥" + price);

        // 支付方式选择
        RadioGroup rgPaymentMethod = findViewById(R.id.rg_payment_method);
        rgPaymentMethod.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_wechat) {
                selectedPaymentType = PAYMENT_TYPE_WECHAT;
            } else if (checkedId == R.id.rb_alipay) {
                selectedPaymentType = PAYMENT_TYPE_ALIPAY;
            }
        });

        // 确认支付按钮
        Button btnConfirmPayment = findViewById(R.id.btn_confirm_payment);
        btnConfirmPayment.setOnClickListener(v -> {
            Intent intent = new Intent(this, PasswordActivity.class);
            intent.putExtra("price", price);
            intent.putExtra("payment_type", selectedPaymentType);
            startActivity(intent);
        });
    }
} 
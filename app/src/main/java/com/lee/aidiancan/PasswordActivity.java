package com.lee.aidiancan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        EditText etPassword = findViewById(R.id.et_password);
        Button btnSubmitPassword = findViewById(R.id.btn_submit_password);
        TextView tvPaymentType = findViewById(R.id.tv_payment_type);

        // 获取支付方式和金额
        int paymentType = getIntent().getIntExtra("payment_type", 1);
        String price = getIntent().getStringExtra("price");
        String paymentTypeText = paymentType == 1 ? "微信支付" : "支付宝";
        tvPaymentType.setText("请输入" + paymentTypeText + "支付密码");

        btnSubmitPassword.setOnClickListener(v -> {
            String password = etPassword.getText().toString();
            if (password.length() == 6) {
                // 在实际应用中，这里应该进行真实的支付处理
                Intent intent = new Intent(this, PaymentSuccessActivity.class);
                intent.putExtra("payment_type", paymentTypeText);
                intent.putExtra("amount", Double.parseDouble(price));
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "请输入6位支付密码", Toast.LENGTH_SHORT).show();
            }
        });
    }
} 
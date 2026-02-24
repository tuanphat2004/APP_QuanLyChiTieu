package com.example.quanlychitieuapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class hosocanhan extends AppCompatActivity {

    private TextView emailTextView;
    private Button btn6, btn7, btn15, btn8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosocanhan);

        emailTextView = findViewById(R.id.emailTextView);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        btn15 = findViewById(R.id.btn15);
        btn8 = findViewById(R.id.btn8);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("email")) {
            String email = intent.getStringExtra("email");
            emailTextView.setText("Email: " + email);
        }

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openThayDoiMatKhauActivity();
            }
        });

        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDangnhapActivity();
            }
        });

        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDeletepActivity();
            }
        });

        btn15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    // Hàm mở Activity thay đổi mật khẩu
    private void openThayDoiMatKhauActivity() {
        String email = emailTextView.getText().toString();
        Intent intent = new Intent(this, doimatkhau.class);
        intent.putExtra("email", email);
        startActivity(intent);
    }

    private void openDangnhapActivity() {
        SharedPreferences prefs = getSharedPreferences("user_info", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("user_id"); // Xóa user_id
        editor.remove("email");  // Xóa email
        editor.remove("password"); // Xóa password
        editor.apply();
        Intent intent1 = new Intent(this, dangnhap.class);
        startActivity(intent1);
    }

    private void openDeletepActivity() {
        Intent intent3 = new Intent(this, delete_account_confirmation.class);
        startActivity(intent3);
    }
}
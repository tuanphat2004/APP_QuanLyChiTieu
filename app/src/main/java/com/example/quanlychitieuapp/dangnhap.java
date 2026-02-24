package com.example.quanlychitieuapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class dangnhap extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private Button registerTextView;
    private UserHelper databaseHelper;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_dangnhap);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerTextView = findViewById(R.id.registerTextView);
        databaseHelper = new UserHelper(this);
        prefs = getSharedPreferences("user_info", MODE_PRIVATE);

        // Kiểm tra xem người dùng đã đăng nhập chưa
        int userId = prefs.getInt("user_id", -1);
        if (userId != -1) {
            // Nếu đã đăng nhập, chuyển đến activity chính
            Intent intent = new Intent(dangnhap.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (databaseHelper.loginUser(email, password)) {
                    int userId = databaseHelper.getUserIdByEmail(email);

                    // Lưu ID người dùng và thông tin đăng nhập vào SharedPreferences
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("user_id", userId);
                    editor.putString("email", email);
                    editor.putString("password", password);
                    editor.apply();

                    Toast.makeText(dangnhap.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    Intent loginIntent1 = new Intent(dangnhap.this, MainActivity.class);
                    startActivity(loginIntent1);
                } else {
                    // Kiểm tra xem email hoặc mật khẩu có đúng không
                    if (databaseHelper.checkEmail(email)) {
                        // Email đúng nhưng mật khẩu sai
                        Toast.makeText(dangnhap.this, "Mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                    } else {
                        // Email sai
                        Toast.makeText(dangnhap.this, "Email không đúng", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang giao diện đăng ký
                Intent registerIntent = new Intent(dangnhap.this, sign_up.class);
                startActivity(registerIntent);
            }
        });
    }
}
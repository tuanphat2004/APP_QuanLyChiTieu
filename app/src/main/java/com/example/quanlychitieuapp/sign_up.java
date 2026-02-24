package com.example.quanlychitieuapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class sign_up extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button dangkyButton;
    private Button loginutton;
    private UserHelper databaseHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        dangkyButton = findViewById(R.id.dangkyButton);
        loginutton = findViewById(R.id.loginutton);

        databaseHelper = new UserHelper(this);

        dangkyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        loginutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginActivity();
            }
        });
    }

    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Kiểm tra các trường trống
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ", Toast.LENGTH_SHORT).show();
            return; // Dừng lại nếu có trường trống
        }

        // Kiểm tra email hợp lệ
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            return; // Dừng lại nếu email không hợp lệ
        }

        // Kiểm tra email đã tồn tại
        if (databaseHelper.isEmailExists(email)) {
            Toast.makeText(this, "Email đã được sử dụng", Toast.LENGTH_SHORT).show();
            return; // Dừng lại nếu email đã tồn tại
        }

        // **GỌI HÀM KIỂM TRA MẬT KHẨU**
        if (!databaseHelper.confirmPassword(password, confirmPassword)) {
            // Nếu confirmPassword() trả về false (mật khẩu không hợp lệ)
            return; // Dừng lại
        }

        // Tiến hành đăng ký nếu tất cả các kiểm tra đều thành công
        boolean isRegistered = databaseHelper.registerUser(email, password, confirmPassword);
        if (isRegistered) {
            Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, dangnhap.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Đăng ký không thành công", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void openLoginActivity() {
        Intent intent = new Intent(this, dangnhap.class);
        startActivity(intent);
    }

}

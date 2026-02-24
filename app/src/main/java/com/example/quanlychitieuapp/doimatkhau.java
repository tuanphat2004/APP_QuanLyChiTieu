package com.example.quanlychitieuapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class doimatkhau extends AppCompatActivity {
    private EditText oldpassword, newpassword, confirmnewbutton;
    private Button doibutton, backButton;
    private TextView tvMessage;
    private UserHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_doimatkhau);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Đổi mật khẩu");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        oldpassword = findViewById(R.id.oldpassword);
        newpassword = findViewById(R.id.newpassword);
        confirmnewbutton = findViewById(R.id.confirmnewbutton);
        doibutton = findViewById(R.id.doibutton);
        tvMessage = findViewById(R.id.tvMessage);
        databaseHelper = new UserHelper(this);
        doibutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("user_info", MODE_PRIVATE);
                int userId = prefs.getInt("user_id", -1);

                if (userId != -1) {
                    String oldPassword = oldpassword.getText().toString();
                    String newPassword = newpassword.getText().toString();
                    String confirmPassword = confirmnewbutton.getText().toString();

                    if (databaseHelper.changePassword(userId, oldPassword, newPassword, confirmPassword)) {
                        Toast.makeText(doimatkhau.this, "Thay đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                        // Chuyển hướng đến giao diện cần thiết sau khi thay đổi mật khẩu
                        Intent intent = new Intent(doimatkhau.this, hosocanhan.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(doimatkhau.this, "Thay đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(doimatkhau.this, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
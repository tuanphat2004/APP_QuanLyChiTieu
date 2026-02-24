package com.example.quanlychitieuapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class delete_account_confirmation extends AppCompatActivity {
    private Button btn_confirm_delete, btn_cancel_delete;
    private UserHelper userHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account_confirmation);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        userHelper = new UserHelper(this);

        btn_confirm_delete = findViewById(R.id.btn_confirm_delete);
        btn_confirm_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xóa tài khoản
                SharedPreferences prefs = getSharedPreferences("user_info", MODE_PRIVATE);
                int userId = prefs.getInt("user_id", -1);
                // Tạo dialog
                android.app.AlertDialog.Builder myDialog = new AlertDialog.Builder(delete_account_confirmation.this);
                myDialog.setTitle("Question");
                myDialog.setMessage("Bạn có chắc chắn muốn xóa tài khoản này không?");

                myDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userHelper.deleteUser(userId);
                        Intent intent = new Intent(delete_account_confirmation.this, dangnhap.class);
                        Toast.makeText(delete_account_confirmation.this, "Xóa tài khoản thành công", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
//                    finish(); // Đóng activity sau khi xóa
                    }
                });

                myDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                myDialog.create().show();

            }
        });

        btn_cancel_delete = findViewById(R.id.btn_cancel_delete);
        btn_cancel_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Quay về Activity trước đó
            }
        });
    }
}
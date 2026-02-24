package com.example.quanlychitieuapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class addWallet extends AppCompatActivity {

    EditText name_wallet, money;
    private walletHelper dbHelper;
    private UserHelper userHelper;
    private int user_id;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_wallet);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Thêm ví");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        name_wallet = findViewById(R.id.nameWallet);
        money = findViewById(R.id.money);
        btnAdd = findViewById(R.id.addWallet);

        // Khởi tạo DatabaseHelper tại đây
        dbHelper = new walletHelper(this);

        // Khởi tạo UserHelper với context và lấy user_id
        userHelper = new UserHelper(this);
        user_id = userHelper.getUserIdFromPreferences();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_id != -1) {
                    themVi();
                    Intent myIntent = new Intent(addWallet.this, chonVi.class);
                    startActivity(myIntent);
                } else {
                    // Xử lý trường hợp không có user_id hợp lệ
                    Toast.makeText(addWallet.this, "Không tìm thấy user_id", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void themVi() {
        String name = name_wallet.getText().toString();
        int money2 = Integer.parseInt(money.getText().toString());

        dbHelper.addWallet(user_id, name, money2);
    }
}

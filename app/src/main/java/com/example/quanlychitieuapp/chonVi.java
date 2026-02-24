package com.example.quanlychitieuapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class chonVi extends AppCompatActivity {

    ListView listView;
    ArrayAdapter<String> adapterDB;
    walletHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chon_vi);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Chọn ví");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        listView = findViewById(R.id.lvWallet);

        showAll();
    }

    private void showAll() {
        database = new walletHelper(this);

        // Lấy danh sách wallet từ cơ sở dữ liệu
        ArrayList<wallet> walletList = database.showWallet();

        int icon = R.drawable.baseline_account_balance_wallet_24;

        // Tạo danh sách tên ví từ danh sách wallet
        ArrayList<String> walletNames = new ArrayList<>();
        for (wallet walletItem : walletList) {
            walletNames.add(walletItem.getName());
        }

        // Sử dụng ArrayAdapter để hiển thị danh sách tên ví trong ListView
        adapterDB = new walletAdapter(this, walletNames, icon);
        listView.setAdapter(adapterDB);

        // Gọi phương thức listenClick từ WalletHelper
        database.listenClick(listView, walletList);
    }

    private int getUserId(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
        return sharedPref.getInt("USER_ID", -1); // -1 là giá trị mặc định nếu không tìm thấy user_id
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuvi, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.add) {
            Intent intent = new Intent(chonVi.this, addWallet.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}

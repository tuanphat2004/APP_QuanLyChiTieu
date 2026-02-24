package com.example.quanlychitieuapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.quanlychitieuapp.Fragment.caiDatFragment;
import com.example.quanlychitieuapp.Fragment.homeFragment;

public class luutrudangnhap extends AppCompatActivity {

    SharedPreferences prefs;
    private UserHelper userHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userHelper = new UserHelper(this);
        prefs = getSharedPreferences("user_info", MODE_PRIVATE);

        // Kiểm tra xem người dùng đã đăng nhập chưa
        int userId = prefs.getInt("user_id", -1);
        if (userId != -1) {
            // Nếu đã đăng nhập, chuyển đến homeFragment
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.fragmentContainer, new homeFragment());
            transaction.commit();
        } else {
            // Nếu chưa đăng nhập, chuyển đến activity đăng nhập
            Intent intent = new Intent(this, dangnhap.class);
            startActivity(intent);
            finish();
        }
    }

//    public void goToFragment(int fragmentId) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        switch (fragmentId) {
//            case 0:
//                transaction.replace(R.id.fragmentContainer, new homeFragment());
//                break;
//            case 1:
//                transaction.replace(R.id.fragmentContainer, new caiDatFragment());
//                break;
//        }
//        transaction.commit();
//    }
}
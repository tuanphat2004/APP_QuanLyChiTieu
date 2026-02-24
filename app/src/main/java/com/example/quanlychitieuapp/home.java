package com.example.quanlychitieuapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.quanlychitieuapp.Fragment.homeFragment;
import com.example.quanlychitieuapp.tab_home.tabHomeAdapter;
import com.google.android.material.tabs.TabLayout;

public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Lấy reference đến FrameLayout
        FrameLayout container = findViewById(R.id.fragment_container);

        // Tạo FragmentTransaction và thêm homeFragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new homeFragment())
                .commit();
    }
}
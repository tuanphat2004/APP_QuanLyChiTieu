package com.example.quanlychitieuapp.tab_thongke;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.quanlychitieuapp.Fragment.thongke;

public class tabThongKeAdapter extends FragmentStatePagerAdapter {
    public tabThongKeAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new chiCircleChart();
            case 1:
                return new thuCircleChart();

            default:
                return new chiCircleChart();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Chi";
            case 1:
                return "Thu";
            default:
                return "Chi";
        }
    }
}
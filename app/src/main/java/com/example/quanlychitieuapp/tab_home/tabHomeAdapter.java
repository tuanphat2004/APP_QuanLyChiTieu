package com.example.quanlychitieuapp.tab_home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


public class tabHomeAdapter extends FragmentStatePagerAdapter {
    public tabHomeAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new home_chi_Fragment();
            case 1:
                return new home_thu_Fragment();
            default:
                return new home_chi_Fragment();

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

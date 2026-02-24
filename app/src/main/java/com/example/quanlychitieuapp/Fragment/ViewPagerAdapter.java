package com.example.quanlychitieuapp.Fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new homeFragment();
            case 1:
                return new viFragment();
            case 2:
                return new addFragment();
            case 3:
                return new thongke();

            case 4:
                return new caiDatFragment();
            default:
                return new homeFragment();

        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}

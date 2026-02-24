package com.example.quanlychitieuapp.tab_vi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class tabPagerAdapter extends FragmentStatePagerAdapter {
    private final int numWeeks;
    private final Calendar calendar = Calendar.getInstance();// Đối tượng Calendar để thao tác với ngày tháng
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM", Locale.getDefault());

    public tabPagerAdapter(@NonNull FragmentManager fm, int behavior, int numWeeks) {
        super(fm, behavior);
        this.numWeeks = numWeeks;
    }

    // Trả về một Fragment cho mỗi trang (tuần)
    @NonNull
    @Override
    public Fragment getItem(int position) {
        // Trả về một instance mới của WeekFragment với tham số tuần tương ứng
        return WeekFragment.newInstance(position + 1, 10);
    }

    @Override
    public int getCount() {
        return numWeeks;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        // Tính toán ngày bắt đầu và ngày kết thúc của tuần
        calendar.set(Calendar.WEEK_OF_YEAR, position + 1);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String startDate = dateFormat.format(calendar.getTime());

        calendar.add(Calendar.DAY_OF_WEEK, 6);
        String endDate = dateFormat.format(calendar.getTime());

        return startDate + " - " + endDate;
    }
}

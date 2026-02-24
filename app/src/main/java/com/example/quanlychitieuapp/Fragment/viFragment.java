package com.example.quanlychitieuapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.quanlychitieuapp.R;
import com.example.quanlychitieuapp.tab_vi.tabPagerAdapter;
import com.example.quanlychitieuapp.walletHelper;
import com.google.android.material.tabs.TabLayout;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

public class viFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private View mView;
    private static final int NUM_WEEKS = 52;
    private int currentWeekIndex;
    TextView money;
    private walletHelper walletData;

    public viFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_vi, container, false);
        tabLayout = mView.findViewById(R.id.tab_layout);
        viewPager = mView.findViewById(R.id.vi_viewpager);
        money = mView.findViewById(R.id.moneyWallet);

        // Khởi tạo walletData
        walletData = new walletHelper(getContext());

        // Lấy tuần hiện tại trong năm
        Calendar calendar = Calendar.getInstance();
        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);

        tabPagerAdapter adapter = new tabPagerAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, NUM_WEEKS);
        viewPager.setAdapter(adapter);

        currentWeekIndex = currentWeek - 1;
        viewPager.setCurrentItem(currentWeekIndex);
        tabLayout.setupWithViewPager(viewPager);

        // Lấy và hiển thị tổng số tiền trong ví
        int totalMoney = walletData.getTotalMoney();
        money.setText(formatCurrency(totalMoney));

        return mView;
    }

    private String formatCurrency(int amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(amount);
    }
}

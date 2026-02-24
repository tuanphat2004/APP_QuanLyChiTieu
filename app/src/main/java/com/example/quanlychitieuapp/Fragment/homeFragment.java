package com.example.quanlychitieuapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.quanlychitieuapp.R;
import com.example.quanlychitieuapp.wallet;
import com.example.quanlychitieuapp.walletAdapter;
import com.example.quanlychitieuapp.walletHelper;
import com.example.quanlychitieuapp.tab_home.tabHomeAdapter;
import com.google.android.material.tabs.TabLayout;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class homeFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private View mView;
    private ListView listView;
    private walletHelper walletData;
    private TextView moneyAll;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public homeFragment() {
        // Required empty public constructor
    }

    public static homeFragment newInstance(String param1, String param2) {
        homeFragment fragment = new homeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        walletData = new walletHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        moneyAll = mView.findViewById(R.id.textView);
        listView = mView.findViewById(R.id.lvWallet);
        tabLayout = mView.findViewById(R.id.tab_baocao_home);
        viewPager = mView.findViewById(R.id.baocao_viewpager);

        tabHomeAdapter adapter = new tabHomeAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);

        int totalMoney = walletData.getTotalMoney();
        moneyAll.setText(formatCurrency(totalMoney));
        tabLayout.setupWithViewPager(viewPager);

        setListViewWallet();

        return mView;
    }

    public void setListViewWallet() {
        int icon = R.drawable.baseline_account_balance_wallet_24;

        ArrayList<wallet> walletList = walletData.showWallet();

        ArrayList<String> walletNames = new ArrayList<>();
        for (wallet walletItem : walletList) {
            walletNames.add(walletItem.getName());
        }

        ArrayAdapter<String> arrayAdapter = new walletAdapter(getContext(), walletNames, icon);
        listView.setAdapter(arrayAdapter);
    }

    private String formatCurrency(int amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(amount);
    }
}

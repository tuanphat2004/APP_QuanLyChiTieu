package com.example.quanlychitieuapp.tab_vi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.quanlychitieuapp.CustomAdapter;
import com.example.quanlychitieuapp.CustomAdapterWallet;
import com.example.quanlychitieuapp.DatabaseHelper;
import com.example.quanlychitieuapp.EmptyExpandableListAdapter;
import com.example.quanlychitieuapp.GiaoDich;
import com.example.quanlychitieuapp.R;
import com.example.quanlychitieuapp.wallet;
import com.example.quanlychitieuapp.walletHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WeekFragment extends Fragment {
    private static final String ARG_WEEK_NUMBER = "week_number";
    private static final String ARG_WALLET_ID = "wallet_id";
    private int weekNumber;
    private int idWalletChose;
    private DatabaseHelper database;
    private ExpandableListView expandableListView;

    int userID;
    TextView tienThu;
    TextView tienChi;
    TextView tongTien;
    Spinner spinnerWallet;
    walletHelper walletData;

    public WeekFragment() {
        // Required empty public constructor
    }

    public static WeekFragment newInstance(int weekNumber, int idWalletChose) {
        WeekFragment fragment = new WeekFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_WEEK_NUMBER, weekNumber);
        args.putInt(ARG_WALLET_ID, idWalletChose); // Đưa id ví được chọn vào Bundle
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            weekNumber = getArguments().getInt(ARG_WEEK_NUMBER);
            idWalletChose = getArguments().getInt(ARG_WALLET_ID); // Lấy id ví được chọn từ Bundle
        }
        database = new DatabaseHelper(getActivity());
        walletData = new walletHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_week, container, false);

        expandableListView = view.findViewById(R.id.expandableListView);
        tienChi = view.findViewById(R.id.tienRa);
        tienThu = view.findViewById(R.id.tienVao);
        tongTien = view.findViewById(R.id.tongTien);
        spinnerWallet = view.findViewById(R.id.spinnerWallet);

        showAllTransactionsForWeek(weekNumber, idWalletChose); // Truyền id ví được chọn vào phương thức

        walletHelper walletHelper = new walletHelper(getContext());
        ArrayList<wallet> walletList = walletHelper.showWallet();

        ArrayList<String> walletNames = new ArrayList<>();
        final Map<String, Integer> walletMap = new HashMap<>();
        for (wallet walletItem : walletList) {
            walletNames.add(walletItem.getName());
            walletMap.put(walletItem.getName(), walletItem.getIdWallet());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.style_spinner, walletNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWallet.setAdapter(arrayAdapter);

        spinnerWallet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedWalletName = walletNames.get(position);
                int idWalletChose = walletMap.get(selectedWalletName);
                showAllTransactionsForWeek(weekNumber, idWalletChose); // Truyền id ví được chọn vào phương thức
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (!walletNames.isEmpty()) {
                    String firstWalletName = walletNames.get(0);
                    int idWalletChose = walletMap.get(firstWalletName);
                    showAllTransactionsForWeek(weekNumber, idWalletChose); // Truyền id ví được chọn vào phương thức
                }
            }
        });

        return view;
    }

    private void showAllTransactionsForWeek(int weekNumber, int idWalletChose) {
        List<String> listDataHeader = new ArrayList<>();
        HashMap<String, List<GiaoDich>> listDataChild = new HashMap<>();
        HashMap<String, Integer> listIcons = new HashMap<>(); // Khởi tạo danh sách icon

        database.showAllTransactionsForWeek(listDataHeader, listDataChild, listIcons, weekNumber, idWalletChose); // Truyền listIcons vào

        if (!listDataHeader.isEmpty() && !listDataChild.isEmpty()) {
            CustomAdapterWallet adapter = new CustomAdapterWallet(getContext(), listDataHeader, listDataChild, listIcons);
            expandableListView.setAdapter(adapter);

            // Expand all groups
            for (int i = 0; i < adapter.getGroupCount(); i++) {
                expandableListView.expandGroup(i, false);
            }

            // Cập nhật các TextView với tổng số tiền
            int tongTienThu = 0;
            int tongTienChi = 0;
            for (List<GiaoDich> giaoDichs : listDataChild.values()) {
                for (GiaoDich giaoDich : giaoDichs) {
                    if (giaoDich.getLoai_giaoDich().equals("thu")) {
                        tongTienThu += giaoDich.getMoney();
                    } else if (giaoDich.getLoai_giaoDich().equals("chi")) {
                        tongTienChi += giaoDich.getMoney();
                    }
                }
            }

            tienThu.setText(String.valueOf(tongTienThu));
            tienChi.setText(String.valueOf(tongTienChi));
            tongTien.setText(String.valueOf(tongTienThu - tongTienChi));
        } else {
            tienThu.setText("0");
            tienChi.setText("0");
            tongTien.setText("0");
            expandableListView.setAdapter(new EmptyExpandableListAdapter(getContext()));
        }
    }


}
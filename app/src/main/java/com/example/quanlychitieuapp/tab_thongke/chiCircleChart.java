package com.example.quanlychitieuapp.tab_thongke;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.quanlychitieuapp.DatabaseHelper;
import com.example.quanlychitieuapp.R;
import com.example.quanlychitieuapp.UserHelper;
import com.example.quanlychitieuapp.walletHelper;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Color;


public class chiCircleChart extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database; // Khai báo biến database
    private com.example.quanlychitieuapp.walletHelper walletHelper;
    UserHelper userHelper;

    public chiCircleChart() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment chiCircleChart.
     */
    // TODO: Rename and change types and number of parameters
    public static chiCircleChart newInstance(String param1, String param2) {
        chiCircleChart fragment = new chiCircleChart();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chi_circle_chart, container, false);

        // Khởi tạo DatabaseHelper
        dbHelper = new DatabaseHelper(getActivity()); // Lấy context từ getActivity()

        // Khởi tạo database
        database = getActivity().openOrCreateDatabase("quanlychitieu.db", Context.MODE_PRIVATE, null); // Sử dụng Context.MODE_PRIVATE
        walletHelper = new walletHelper(getActivity());
        userHelper = new UserHelper(getContext());
        // Tạo biểu đồ tròn
        createPieChart(view);
        return view;
    }


    private void createPieChart(View view) {
        Map<String, Integer> thongKe = getThongKeChiTieu();

        PieChart pieChart = new PieChart(getActivity()); // Sử dụng getActivity()
        FrameLayout chartContainer = view.findViewById(R.id.chartContainerThongKe);

        if (chartContainer != null) {
            chartContainer.addView(pieChart);
        } else {
            // Handle the case when chartContainer is null, maybe log an error or show a message.
            Log.e("chiCircleChart", "chartContainer is null");
            return;
        }

        List<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : thongKe.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

        // Thiết lập PieDataSet
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueFormatter(new PercentFormatter(pieChart));
        dataSet.setValueTextSize(20f);

        // Thiết lập PieData
        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Thống kê chi tiêu");
        pieChart.getLegend().setTextColor(Color.WHITE); // Đặt màu chữ chú thích thành màu trắng
        pieChart.getLegend().setWordWrapEnabled(true); // xuong hang chu thich
        pieChart.getLegend().setTextSize(25f);
        pieChart.animateY(1000);
        pieChart.invalidate();
    }

    private Map<String, Integer> getThongKeChiTieu() {
        Map<String, Integer> thongKe = new HashMap<>();

        if (database != null) {
            Cursor cursor = database.query("giaodich", null, "loai_giaodich = 'chi'", null, null, null, null);

            if (cursor != null) {
                int indexIdWal = cursor.getColumnIndex("id_wal");
                int indexMoney = cursor.getColumnIndex("money");
                int indexGroupName = cursor.getColumnIndex("group_name");

                if (indexIdWal == -1 || indexMoney == -1 || indexGroupName == -1) {
                    // Log or handle the error appropriately
                    cursor.close();
                    throw new IllegalArgumentException("One or more column names do not exist in the table.");
                }

                while (cursor.moveToNext()) {
                    int id_w = cursor.getInt(indexIdWal);
                    double money = cursor.getDouble(indexMoney);

                    // Get userId associated with this id_w
                    int userId = walletHelper.getUserIdFromWalletId(id_w);

                    // Check if the userId matches current user's id
                    if (userId == userHelper.getUserIdFromPreferences()) {
                        String groupName = cursor.getString(indexGroupName);

                        if (thongKe.containsKey(groupName)) {
                            thongKe.put(groupName, thongKe.get(groupName) + (int) money);
                        } else {
                            thongKe.put(groupName, (int) money);
                        }
                    }
                }
                cursor.close();
            }
        }

        return thongKe;
    }


}
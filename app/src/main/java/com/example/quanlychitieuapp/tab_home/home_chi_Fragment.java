package com.example.quanlychitieuapp.tab_home;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.quanlychitieuapp.DatabaseHelper;
import com.example.quanlychitieuapp.R;
import com.example.quanlychitieuapp.UserHelper;
import com.example.quanlychitieuapp.walletHelper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class home_chi_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private walletHelper walletHelper;
    UserHelper userHelper;


    public home_chi_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment home_week_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static home_chi_Fragment newInstance(String param1, String param2) {
        home_chi_Fragment fragment = new home_chi_Fragment();
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
        View view = inflater.inflate(R.layout.fragment_home_chi_, container, false);
        walletHelper = new walletHelper(getActivity());
        userHelper = new UserHelper(getContext());
        dbHelper = new DatabaseHelper(getContext());
        database = dbHelper.getReadableDatabase();

        createBarChart(view);

        return view;
    }

    private void createBarChart(View view) {
        Map<String, Integer> thongKe = getThongKeChiTieu();
        int tongTienChi = 0;

        BarChart barChart = view.findViewById(R.id.barChart);

        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        int i = 0; // Biến đếm để thêm màu sắc
        for (Map.Entry<String, Integer> entry : thongKe.entrySet()) {
            entries.add(new BarEntry(i, entry.getValue()));
            labels.add(entry.getKey());
            tongTienChi += entry.getValue();
            i++;
        }

        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColors(getColors()); // Sử dụng danh sách màu sắc đã tạo
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(15f);
        dataSet.setDrawValues(false); // Xóa chữ trên cột

        BarData data = new BarData(dataSet);
        barChart.setData(data);
        barChart.getDescription().setEnabled(false);
        barChart.setFitBars(true);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        // Ẩn legend mặc định của BarChart
        barChart.getLegend().setEnabled(false);

        barChart.getXAxis().setTextColor(Color.WHITE);
        barChart.getAxisLeft().setTextColor(Color.WHITE);
        barChart.getAxisRight().setTextColor(Color.WHITE);

        barChart.invalidate();

        TextView tvTongTienChi = view.findViewById(R.id.tvTongTienChi);
        tvTongTienChi.setText("Tổng tiền chi: " + tongTienChi);
        tvTongTienChi.setTextColor(Color.WHITE);

        // Hiển thị chú thích chi tiết
        TextView tvChuThich = view.findViewById(R.id.tvChuThich);
        StringBuilder chuThichText = new StringBuilder("Chú thích:\n");
        for (i = 0; i < labels.size(); i++) {
            chuThichText.append(" - ")
                    .append(labels.get(i))
                    .append(": <font color=\"")
                    .append(String.format("#%06X", (0xFFFFFF & getColors().get(i)))) // Chuyển đổi màu sang mã hex
                    .append("\">■</font><br>");
        }
        tvChuThich.setText(Html.fromHtml(chuThichText.toString()));
    }

    // Danh sách màu sắc cho các cột
    private List<Integer> getColors() {
        List<Integer> colors = new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);
        colors.add(Color.CYAN);
        colors.add(Color.MAGENTA);
        return colors;
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
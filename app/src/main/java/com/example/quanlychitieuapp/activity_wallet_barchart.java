package com.example.quanlychitieuapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class activity_wallet_barchart extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private UserHelper userHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_barchart);

        dbHelper = new DatabaseHelper(this);
        database = openOrCreateDatabase("quanlychitieu.db", Context.MODE_PRIVATE, null);

        // Khởi tạo UserHelper
        userHelper = new UserHelper(this);

        createBarChart();
    }

    private void createBarChart() {
        Map<String, Integer> thongKe = getThongKeWallet();
        int tongTienThu = 0;

        BarChart barChart = new BarChart(this);
        FrameLayout chartContainer = findViewById(R.id.chartContainer);
        chartContainer.addView(barChart);

        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : thongKe.entrySet()) {
            entries.add(new BarEntry(entries.size(), entry.getValue()));
            labels.add(entry.getKey());
            tongTienThu += entry.getValue();
        }

        BarDataSet dataSet = new BarDataSet(entries, "Tổng tiền theo ví");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextColor(Color.WHITE);
        BarData data = new BarData(dataSet);

        barChart.setData(data);
        barChart.getDescription().setEnabled(false);
        barChart.setFitBars(true);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));

        // Đặt màu chữ thành màu trắng
        barChart.getXAxis().setTextColor(Color.WHITE);
        barChart.getAxisLeft().setTextColor(Color.WHITE);
        barChart.getAxisRight().setTextColor(Color.WHITE);

        barChart.invalidate();

        TextView tvTongTienChi = findViewById(R.id.tvTongTienChi);
        tvTongTienChi.setText("Tổng tiền: " + tongTienThu);
        tvTongTienChi.setTextColor(Color.WHITE);
    }

    private Map<String, Integer> getThongKeWallet() {
        Map<String, Integer> thongKe = new HashMap<>();

        // Lấy userId từ UserHelper
        int userId = userHelper.getUserIdFromPreferences();

        Cursor cursor = database.query("wallet", null, "user_id = ?", new String[]{String.valueOf(userId)}, null, null, null);

        while (cursor.moveToNext()) {
            String name_wallet = cursor.getString(cursor.getInt(2));
            int money = cursor.getInt(3); // Lấy tổng tiền từ cột "money" của bảng "wallet"

            thongKe.put(name_wallet, money);
        }
        cursor.close();

        return thongKe;
    }
}
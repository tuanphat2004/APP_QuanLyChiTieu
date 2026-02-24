package com.example.quanlychitieuapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
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

public class activity_barchart extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barchart);

        dbHelper = new DatabaseHelper(this);
        database = openOrCreateDatabase("quanlychitieu.db", Context.MODE_PRIVATE, null);

        createBarChart();
    }

    private void createBarChart() {
        Map<String, Integer> thongKe = getThongKeChiTieu();
        int tongTienChi = 0;

        BarChart barChart = new BarChart(this);
        FrameLayout chartContainer = findViewById(R.id.chartContainerBarChart);
        chartContainer.addView(barChart);

        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        int i = 0;
        for (Map.Entry<String, Integer> entry : thongKe.entrySet()) {
            entries.add(new BarEntry(i, entry.getValue()));
            labels.add(entry.getKey());
            tongTienChi += entry.getValue();
            i++;
        }

        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColors(getColors()); // Sử dụng danh sách màu sắc đã tạo
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(12f);
        dataSet.setDrawValues(false); // Xóa chữ trên cột

        BarData data = new BarData(dataSet);
        barChart.setData(data);
        barChart.getDescription().setEnabled(false);
        barChart.setFitBars(true);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));

        barChart.getXAxis().setTextColor(Color.WHITE);
        barChart.getAxisLeft().setTextColor(Color.WHITE);
        barChart.getAxisRight().setTextColor(Color.WHITE);
        barChart.getLegend().setTextColor(Color.WHITE);
        barChart.getLegend().setWordWrapEnabled(true);
        barChart.getLegend().setTextSize(25f);
        barChart.getLegend().setEnabled(false); //ẩn legend default

        barChart.invalidate();

        // Hiển thị tổng tiền chi
        TextView tvTongTienChi = findViewById(R.id.tvTongTienChi);
        tvTongTienChi.setText("Tổng tiền chi: " + tongTienChi);
        tvTongTienChi.setTextColor(Color.WHITE);

        // Hiển thị chú thích chi tiết
        TextView tvChuThich = findViewById(R.id.tvChuThich);
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

    private List<Integer> getColors() {
        List<Integer> colors = new ArrayList<>();
        //Thêm màu sắc cho các loại chi tiêu khác
        colors.add(Color.BLUE); // Màu xanh cho chi tiêu ăn uống (ví dụ)
        colors.add(Color.RED); // Màu đỏ cho chi tiêu đi lại (ví dụ)
        colors.add(Color.GREEN); // Màu xanh lá
        colors.add(Color.YELLOW); // Màu vàng
        colors.add(Color.CYAN); // Màu cam
        colors.add(Color.MAGENTA); // Màu tím

        return colors;
    }

    private Map<String, Integer> getThongKeChiTieu() {
        Map<String, Integer> thongKe = new HashMap<>();

        try (Cursor cursor = database.query("giaodich", null, "loai_giaodich = 'chi'", null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String groupName = cursor.getString(cursor.getColumnIndexOrThrow("group_name"));
                    double money = cursor.getDouble(cursor.getColumnIndexOrThrow("money"));

                    if (thongKe.containsKey(groupName)) {
                        thongKe.put(groupName, thongKe.get(groupName) + (int) money);
                    } else {
                        thongKe.put(groupName, (int) money);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            // Xử lý exception
            e.printStackTrace();
        }

        return thongKe;
    }
}
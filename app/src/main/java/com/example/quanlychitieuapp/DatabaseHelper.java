package com.example.quanlychitieuapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DatabaseHelper {
    private Context context;
    private String DB_PATH_SUFFIX = "/databases/";
    private String DATABASE_NAME = "quanlychitieu.db";
    private SQLiteDatabase database;

    int idWalletChose = 1;

    public DatabaseHelper(Context context) {
        this.context = context;
        xuLySaoChepCSDL();
    }

    public SQLiteDatabase getReadableDatabase() {
        return context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
    }

    private void xuLySaoChepCSDL() {
        File dbFile = context.getDatabasePath(DATABASE_NAME);

        if (!dbFile.exists()) {
            copyDatabase();
        }
//        else {
//            context.deleteDatabase(DATABASE_NAME);
//            copyDatabase();
//        }

    }

    private void copyDatabase() {
        try {
            InputStream myInput = context.getAssets().open(DATABASE_NAME);
            String outFileName = context.getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
            File f = new File(context.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists()) {
                f.mkdir();
            }

            OutputStream myOutput = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, len);
            }
            myOutput.flush();
            myInput.close();
            myOutput.close();

        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
    }

    public void addGiaoDich(int id_wal, Double money, String tenGiaoDich, String nhomGiaoDich, String day, String note) {
        // Mở hoặc tạo cơ sở dữ liệu
        database = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);

        // Chuyển đổi định dạng ngày từ "dd/MM/yyyy" sang "yyyy-MM-dd"
        SimpleDateFormat sdfInput = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());  // Chuyển chuỗi ngày "dd/MM/yyyy" sang đối tượng Date
        SimpleDateFormat sdfDatabase = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()); // Chuyển đối tượng Date thành chuỗi ngày "yyyy-MM-dd"
        try {
            Date dateFormatted = sdfInput.parse(day);
            day = sdfDatabase.format(dateFormatted);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ContentValues row = new ContentValues();
        row.put("id_wal", id_wal);
        row.put("money", money);
        row.put("loai_giaodich", tenGiaoDich);
        row.put("group_name", nhomGiaoDich);
        row.put("day", day);
        row.put("note", note);

        long r = database.insert("giaodich", null, row);
        if (r != -1) {
            Toast.makeText(context, "Thêm thành công", Toast.LENGTH_LONG).show();

            updateWalletBalance(id_wal, money, tenGiaoDich); // Cập nhật số tiền trong ví
        } else {
            Toast.makeText(context, "Thêm thất bại", Toast.LENGTH_LONG).show();
        }
    }

    private void updateWalletBalance(int id_wal, double money, String loai_giaoDich) {
        Cursor cursor = null;
        try {
            database = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);

            // Truy vấn số tiền hiện tại trong ví
            cursor = database.query("wallet", new String[]{"money"}, "id_w=?", new String[]{String.valueOf(id_wal)}, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                // Lấy chỉ số cột "money"
                int moneyColumnIndex = cursor.getColumnIndex("money");

                // Kiểm tra nếu chỉ số cột hợp lệ
                if (moneyColumnIndex != -1) {
                    // Lấy số dư hiện tại từ cột "money"
                    double currentBalance = cursor.getDouble(moneyColumnIndex);

                    // Tính toán số tiền mới dựa trên loại giao dịch
                    double newBalance;
                    if (loai_giaoDich.equals("thu")) {
                        newBalance = currentBalance + money;
                    } else if (loai_giaoDich.equals("chi")) {
                        newBalance = currentBalance - money;
                    } else {
                        // Nếu loại giao dịch không hợp lệ
                        Toast.makeText(context, "Loại giao dịch không hợp lệ", Toast.LENGTH_LONG).show();
                        return;
                    }

                    // Tạo ContentValues để cập nhật số dư mới
                    ContentValues row = new ContentValues();
                    row.put("money", newBalance);

                    // Cập nhật số dư mới vào cơ sở dữ liệu
                    int rowsAffected = database.update("wallet", row, "id_w=?", new String[]{String.valueOf(id_wal)});

                    if (rowsAffected > 0) {
                        Toast.makeText(context, "Cập nhật số tiền trong ví thành công", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "Cập nhật số tiền trong ví thất bại", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "Không tìm thấy cột 'money' trong bảng 'wallet'", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(context, "Không tìm thấy ví có ID " + id_wal, Toast.LENGTH_LONG).show();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    public void xoaGiaoDich(int id_giaodich) {
        // Mở cơ sở dữ liệu
        database = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);

        // Lấy thông tin giao dịch cần xóa để cập nhật lại ví
        Cursor cursor = database.query("giaodich", new String[]{"id_wal", "money", "loai_giaodich"}, "id=?", new String[]{String.valueOf(id_giaodich)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int idWalIndex = cursor.getColumnIndex("id_wal");
            int moneyIndex = cursor.getColumnIndex("money");
            int loaiGiaoDichIndex = cursor.getColumnIndex("loai_giaodich");

            // Kiểm tra xem các chỉ số cột có hợp lệ không
            if (idWalIndex != -1 && moneyIndex != -1 && loaiGiaoDichIndex != -1) {
                int id_wal = cursor.getInt(idWalIndex);
                double money = cursor.getDouble(moneyIndex);
                String loai_giaodich = cursor.getString(loaiGiaoDichIndex);

                // Xóa giao dịch theo id
                int rowsDeleted = database.delete("giaodich", "id=?", new String[]{String.valueOf(id_giaodich)});

                if (rowsDeleted > 0) {
                    Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    if (loai_giaodich.equals("chi")) {
                        updateWalletBalance(id_wal, money, "thu");
                    } else {
                        updateWalletBalance(id_wal, money, "chi");
                    }
                    // Cập nhật số tiền trong ví sau khi xóa giao dịch

                } else {
                    Toast.makeText(context, "Không tìm thấy giao dịch để xóa", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Xử lý trường hợp tên cột không tồn tại
                Log.e("CursorError", "Cột không tồn tại trong cursor");
            }
        } else {
            Toast.makeText(context, "Không tìm thấy giao dịch để xóa", Toast.LENGTH_SHORT).show();
        }

        if (cursor != null) {
            cursor.close();
        }
    }

    public void editGiaoDich(int id_giaodich, int id_wal, Double money, String tenGiaoDich, String nhomGiaoDich, String day, String note) {
        database = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);

        // Chuyển đổi định dạng ngày từ "dd/MM/yyyy" sang "yyyy-MM-dd"
        SimpleDateFormat sdfInput = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat sdfDatabase = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date dateFormatted = sdfInput.parse(day);
            day = sdfDatabase.format(dateFormatted);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ContentValues row = new ContentValues();
        row.put("id_wal", id_wal);
        row.put("money", money);
        row.put("loai_giaodich", tenGiaoDich);
        row.put("group_name", nhomGiaoDich);
        row.put("day", day);
        row.put("note", note);

        // Sửa giao dịch theo id
        int rowsEdit = database.update("giaodich", row, "id=?", new String[]{String.valueOf(id_giaodich)});
        if (rowsEdit > 0) {
            Toast.makeText(context, "Sửa giao dịch thành công", Toast.LENGTH_LONG).show();
            updateWalletBalance(id_wal, money, tenGiaoDich); // Cập nhật số tiền trong ví
        } else {
            Toast.makeText(context, "Không tìm thấy giao dịch để sửa", Toast.LENGTH_SHORT).show();
        }
    }

    public void showAllTransactionsForWeek(List<String> listDataHeader, HashMap<String, List<GiaoDich>> listDataChild, HashMap<String, Integer> listIcons, int weekNumber, int idWalletChose) {
        database = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);

        // Xác định ngày đầu tiên và ngày cuối cùng của tuần
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.WEEK_OF_YEAR, weekNumber);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date startDate = calendar.getTime();

        calendar.add(Calendar.DAY_OF_WEEK, 6);
        Date endDate = calendar.getTime();

        // Chuyển đổi startDate và endDate sang định dạng "yyyy-MM-dd" để truy vấn cơ sở dữ liệu
        SimpleDateFormat sdfDatabase = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String startDateDatabase = sdfDatabase.format(startDate);
        String endDateDatabase = sdfDatabase.format(endDate);

        // Truy vấn cơ sở dữ liệu để lấy các giao dịch trong khoảng thời gian xác định và theo ví đã chọn
        Cursor cursor = database.query(
                "giaodich",
                null,
                "id_wal = ? AND date(day) BETWEEN date(?) AND date(?)",
                new String[]{String.valueOf(idWalletChose), startDateDatabase, endDateDatabase},
                null,
                null,
                "day DESC"
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            int id_wal = cursor.getInt(1);
            int money = cursor.getInt(2);
            String loai_giaodich = cursor.getString(3);
            String group_name = cursor.getString(4);
            String date = cursor.getString(5);
            String note = cursor.getString(6);

            GiaoDich giaoDich = new GiaoDich(id, id_wal, money, loai_giaodich, group_name, date, note);

            // Chuyển đổi lại định dạng ngày từ "yyyy-MM-dd" sang "dd/MM/yyyy" để hiển thị
            SimpleDateFormat sdfDisplay = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            try {
                Date dateFormatted = sdfDatabase.parse(date);
                date = sdfDisplay.format(dateFormatted);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (!listDataHeader.contains(date)) {
                listDataHeader.add(date);
                listDataChild.put(date, new ArrayList<GiaoDich>());
            }
            listDataChild.get(date).add(giaoDich);

            // Thêm thông tin icon cho giao dịch vào listIcons
            int iconId = getIconForTransaction(loai_giaodich); // Phương thức này phải được cài đặt để trả về ID của icon
            listIcons.put(giaoDich.getGroup_name(), iconId);
        }
        cursor.close();
    }

    // Phương thức để lấy ID của icon dựa vào loại giao dịch (ví dụ)
    private int getIconForTransaction(String loai_giaodich) {
        // Thay thế bằng logic của bạn để lấy ID của icon dựa vào loại giao dịch
        if (loai_giaodich.equals("Ăn uống")) {
            return R.drawable.baseline_fastfood_24;
        } else if (loai_giaodich.equals("Bảo hiểm")) {
            return R.drawable.outline_medical_information_24;
        } else if (loai_giaodich.equals("Đầu tư")) {
            return R.drawable.baseline_monetization_on_24;
        } else if (loai_giaodich.equals("Di chuyển")) {
            return R.drawable.baseline_directions_car_filled_24;
        } else if (loai_giaodich.equals("Các chi phí khác")) {
            return R.drawable.baseline_monetization_on_24;
        } else if (loai_giaodich.equals("Lương")) {
            return R.drawable.baseline_monetization_on_24;
        } else if (loai_giaodich.equals("Thu nhập khác")) {
            return R.drawable.baseline_monetization_on_24;
        } else if (loai_giaodich.equals("Tiền chuyển đến")) {
            return R.drawable.baseline_monetization_on_24;
        } else {
            return R.drawable.outline_comment_24; // Biểu tượng mặc định nếu không có loại giao dịch phù hợp
        }
    }


}

package com.example.quanlychitieuapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class walletHelper {
    private Context context;
    private SQLiteDatabase database;
    private static final String DATABASE_NAME = "quanlychitieu.db";
    private int userId;

    public walletHelper(Context context) {
        this.context = context;
        UserHelper userHelper = new UserHelper(context);
        this.userId = userHelper.getUserIdFromPreferences();
    }

    private void openDatabase() {
        database = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
    }

    private void closeDatabase() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }

    public ArrayList<wallet> showWallet() {
        openDatabase();

        ArrayList<wallet> resultList = new ArrayList<>();
        if (userId == -1) {
            return resultList;
        }

        Cursor cursor = database.query("wallet",
                new String[]{"id_w", "user_id", "name_wallet", "money"},
                "user_id = ?",
                new String[]{String.valueOf(userId)},
                null,
                null,
                null);

        while (cursor.moveToNext()) {
            int id_w = cursor.getInt(0);
            int user_id = cursor.getInt(1);
            String name_wallet = cursor.getString(2);
            int money = cursor.getInt(3);

            wallet walletItem = new wallet(id_w, user_id, name_wallet, money);
            resultList.add(walletItem);
        }

        cursor.close();
        closeDatabase();
        return resultList;
    }

    public void addWallet(int userId, String nameWallet, int money) {
        openDatabase();

        ContentValues row = new ContentValues();
        row.put("user_id", userId);
        row.put("name_wallet", nameWallet);
        row.put("money", money);

        long result = database.insert("wallet", null, row);
        if (result != -1) {
            Toast.makeText(context, "Thêm thành công", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Thêm thất bại", Toast.LENGTH_LONG).show();
        }

        closeDatabase();
    }

    public void listenClick(ListView lv, ArrayList<wallet> data) {
        lv.setOnItemClickListener((parent, view, position, id) -> {
            wallet selectedItem = data.get(position);
            Toast.makeText(context, "Bạn đã chọn: " + selectedItem.getName(), Toast.LENGTH_SHORT).show();

            Intent myIntent = new Intent(context, calculator.class);
            Bundle myBundle = new Bundle();
            myBundle.putInt("idWallet", selectedItem.getIdWallet());
            myBundle.putInt("userId", selectedItem.getUserId());
            myBundle.putString("nameWallet", selectedItem.getName());
            myBundle.putInt("money", selectedItem.getMoney());

            myIntent.putExtra("selectedWallet", myBundle);
            ((Activity) context).setResult(Activity.RESULT_OK, myIntent);
            ((Activity) context).finish();
        });
    }

    // Thêm phương thức mới để lấy tên ví từ id_w
    public String getWalletNameById(int id_w) {
        openDatabase();
        String walletName = null;

        Cursor cursor = database.query("wallet",
                new String[]{"name_wallet"},
                "id_w = ?",
                new String[]{String.valueOf(id_w)},
                null,
                null,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex("name_wallet");
            if (columnIndex != -1) {
                walletName = cursor.getString(columnIndex);
            }
        } else {
            Log.e("WalletHelper", "Cursor is null or empty for id_w: " + id_w);
        }

        if (cursor != null) {
            cursor.close();
        }
        closeDatabase();
        return walletName;
    }

    public String getWalletNameByUserId(int userId) {
        openDatabase();
        String walletName = null;

        Cursor cursor = database.query("wallet",
                new String[]{"name_wallet"},
                "user_id = ?",
                new String[]{String.valueOf(userId)},
                null,
                null,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex("name_wallet");
            if (columnIndex != -1) {
                walletName = cursor.getString(columnIndex);
            }
        } else {
            Log.e("WalletHelper", "Cursor is null or empty for user_id: " + userId);
        }

        if (cursor != null) {
            cursor.close();
        }
        closeDatabase();
        return walletName;
    }

    public int getTotalMoney() {
        openDatabase();

        int totalMoney = 0;
        if (userId == -1) {
            return totalMoney;
        }

        Cursor cursor = database.rawQuery("SELECT SUM(money) FROM wallet WHERE user_id = ?", new String[]{String.valueOf(userId)});

        if (cursor != null && cursor.moveToFirst()) {
            totalMoney = cursor.getInt(0);
        }

        if (cursor != null) {
            cursor.close();
        }
        closeDatabase();

        return totalMoney;
    }

    public int getUserIdFromWalletId(int id_w) {
        openDatabase();
        int userId = -1;

        Cursor cursor = database.query("wallet",
                new String[]{"user_id"},
                "id_w = ?",
                new String[]{String.valueOf(id_w)},
                null,
                null,
                null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex("user_id");
                if (columnIndex != -1) {
                    userId = cursor.getInt(columnIndex);
                } else {

                }
            }
            cursor.close();
        }

        closeDatabase();
        return userId;
    }


}

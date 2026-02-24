package com.example.quanlychitieuapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserHelper {
    private Context context;
    private SQLiteDatabase database;
    private static final String DATABASE_NAME = "quanlychitieu.db";

    public UserHelper(Context context) {
        this.context = context;
    }

    public boolean registerUser(String email, String password, String confirmPassword) {
        if (confirmPassword(password, confirmPassword)) {
            database = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);

            if (isEmailExists(email)) {
                Toast.makeText(context, "Email đã tồn tại", Toast.LENGTH_SHORT).show();
                return false;
            }

            ContentValues values = new ContentValues();
            values.put("email", email);
            values.put("password", Util.hashPassword(password)); // Sử dụng hàm hashPassword

            try {
                long result = database.insert("user", null, values);
                if (result != -1) {
                    // Tao vi moi khi tao tai khoan
                    ContentValues valuesWallet = new ContentValues();
                    valuesWallet.put("user_id", result);
                    valuesWallet.put("name_wallet", "Tien mat");
                    valuesWallet.put("money", 0);

                    long insertWallet = database.insert("wallet", null, valuesWallet);
                    if (insertWallet != -1) {
                        Log.d("UserHelper", "Register result: " + insertWallet);
                        return true;
                    } else {
                        Log.e("UserHelper", "Error creating wallet");
                        return false;
                    }
                } else {
                    Log.e("UserHelper", "Error registering user");
                    return false;
                }
            } catch (Exception e) {
                Log.e("UserHelper", "Error occurred: " + e.getMessage());
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean loginUser(String email, String password) {
        database = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        try (Cursor cursor = database.rawQuery("SELECT * FROM user WHERE email = ?", new String[]{email})) {
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") String hashedPassword = cursor.getString(cursor.getColumnIndex("password"));
                String hashedInputPassword = Util.hashPassword(password);
                if (hashedPassword.equals(hashedInputPassword)) {
                    @SuppressLint("Range") int userId = cursor.getInt(cursor.getColumnIndex("id"));
                    saveUserIdToPreferences(userId);
                    Log.d("UserHelper", "User ID saved: " + userId);
                    return true;
                }
            }
        }
        return false;
    }


    private void saveUserIdToPreferences(int userId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("user_id", userId);
        editor.apply();
    }


    public int getUserIdFromPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("user_id", -1); // -1 nếu không tìm thấy
    }


    public boolean changePassword(int userId, String oldPassword, String newPassword, String confirmPassword) {
        database = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        String hashedOldPassword = Util.hashPassword(oldPassword);
        String hashedNewPassword = Util.hashPassword(newPassword);

        try (Cursor cursor = database.rawQuery("SELECT * FROM user WHERE id = ? AND password = ?", new String[]{String.valueOf(userId), hashedOldPassword})) {
            if (cursor.moveToFirst()) {
                ContentValues values = new ContentValues();
                values.put("password", hashedNewPassword);
                if (confirmPassword(newPassword, confirmPassword)) {
                    database.update("user", values, "id = ?", new String[]{String.valueOf(userId)});
                    return true;
                }
            }
        }
        return false;
    }

    @SuppressLint("Range")
    public int getUserIdByEmail(String email) {
        database = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        int userId = -1;
        try (Cursor cursor = database.rawQuery("SELECT id FROM user WHERE email = ?", new String[]{email})) {
            if (cursor.moveToFirst()) {
                userId = cursor.getInt(cursor.getColumnIndex("id"));
            }
        }
        return userId;
    }

    public boolean isEmailExists(String email) {
        database = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        try (Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM user WHERE email = ?", new String[]{email})) {
            if (cursor.moveToFirst()) {
                return cursor.getInt(0) > 0;
            }
        }
        return false;
    }

    boolean confirmPassword(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            return false; // Mật khẩu không khớp
        }

        // Kiểm tra độ dài mật khẩu
        if (password.length() < 8) {
            Toast.makeText(context, "Mật khẩu phải ít nhất 8 ký tự.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Kiểm tra chữ hoa
        Pattern pattern = Pattern.compile("[A-Z]");
        Matcher matcher = pattern.matcher(password);
        if (!matcher.find()) {
            Toast.makeText(context, "Mật khẩu phải có ít nhất 1 chữ hoa.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true; // Mật khẩu hợp lệ
    }

    public boolean checkEmail(String email) {
        database = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        Cursor cursor = database.query("user", new String[]{"email"}, "email = ?", new String[]{email}, null, null, null);
        boolean emailExists = (cursor.getCount() > 0);
        cursor.close();
        database.close();
        return emailExists;
    }

    public boolean deleteUser(int userId) {
        database = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        int rowsDeleted = database.delete("user", "id = ?", new String[]{String.valueOf(userId)});
        return rowsDeleted > 0;
    }
}
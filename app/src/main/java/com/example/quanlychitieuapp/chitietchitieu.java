package com.example.quanlychitieuapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class chitietchitieu extends AppCompatActivity {
    TextView tvName, tvMoney, tvDate, tvWallet;
    String name;
    int money;
    String date;
    String wallet;
    String note;
    private int idGiaodich;
    private DatabaseHelper dbHelper;
    int id_wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitietchitieu);

        // Khởi tạo DatabaseHelper tại đây
        dbHelper = new DatabaseHelper(this);

        Toolbar toolbar = findViewById(R.id.toolbarVi);
        setSupportActionBar(toolbar);

        tvName = findViewById(R.id.name);
        tvMoney = findViewById(R.id.money);
        tvDate = findViewById(R.id.date);
        tvWallet = findViewById(R.id.wallet);

        setContentText();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tool_bar_vi, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == R.id.edit) {

            Bundle myBundle = new Bundle();

            myBundle.putInt("id_giaodich", idGiaodich);  // Thêm id của giao dịch vào bundle
            myBundle.putString("name", name);
            myBundle.putInt("money", money);
            myBundle.putString("date", date);
            myBundle.putString("wallet", wallet);
            myBundle.putString("note", note);

            myBundle.putInt("id_wallet", id_wallet);
//            myBundle.putString("loai_giaodich", giaoDich.loai_giaoDich);
//            myBundle.putString("note", giaoDich.note);

            Intent intent = new Intent(chitietchitieu.this, editGiaoDich.class);
            intent.putExtra("myPackageEditChiTieu", myBundle);
            startActivity(intent);
        } else if (itemId == R.id.delete) {
            // Tạo dialog
            AlertDialog.Builder myDialog = new AlertDialog.Builder(chitietchitieu.this);
            myDialog.setTitle("Question");
            myDialog.setMessage("Bạn có chắc chắn muốn xóa giao dịch này không?");

            myDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dbHelper.xoaGiaoDich(idGiaodich);
                    Intent intent = new Intent(chitietchitieu.this, MainActivity.class);
                    startActivity(intent);
//                    finish(); // Đóng activity sau khi xóa
                }
            });

            myDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            myDialog.create().show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void getData() {
        Intent myIntent = getIntent();
        Bundle myBundle = myIntent.getBundleExtra("myPackageChiTietChiTieu");
        if (myBundle != null) {
            idGiaodich = myBundle.getInt("id_giaodich");
            id_wallet = myBundle.getInt("id_wallet");
            name = myBundle.getString("name");
            money = myBundle.getInt("money");
            date = myBundle.getString("date");
            wallet = myBundle.getString("wallet");
            note = myBundle.getString("note");
            // Kiểm tra giá trị id_giaodich
            Toast.makeText(this, "ID giao dịch: " + idGiaodich, Toast.LENGTH_SHORT).show();
        }
    }


    private void setContentText() {
        getData();
        tvName.setText(name);
        tvMoney.setText(String.valueOf(money));
        tvDate.setText(date);
        tvWallet.setText(wallet);
    }
}

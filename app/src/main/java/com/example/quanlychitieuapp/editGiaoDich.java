package com.example.quanlychitieuapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.mariuszgromada.math.mxparser.Expression;

import java.util.Calendar;

public class editGiaoDich extends AppCompatActivity {

    static final int REQUEST_CODE = 0;

    private EditText display, note_content;
    private Button btnSave, btnBack, btnDate;
    private String dateSelect;
    private TextView nhomGiaoDich;
    TextView txtWallet;

    String name;
    int money;
    String date;
    String wallet;
    String note;
    private int idGiaodich;
    private DatabaseHelper dbHelper;
    String selectedGD;
    int id_wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_giao_dich);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Chỉnh sửa giao dịch");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dbHelper = new DatabaseHelper(this);

        initViews();
        setupListeners();
        giaoDich();
        checkFieldsForEmptyValues();
    }

    private void getData() {
        Intent myIntent = getIntent();
        Bundle myBundle = myIntent.getBundleExtra("myPackageEditChiTieu");
        if (myBundle != null) {
            idGiaodich = myBundle.getInt("id_giaodich");
            id_wallet = myBundle.getInt("id_wallet");
            name = myBundle.getString("name");
            money = myBundle.getInt("money");
            date = myBundle.getString("date");
            wallet = myBundle.getString("wallet");
            note = myBundle.getString("note");
            selectedGD = myBundle.getString("loai_giaodich"); // Get selectedGD
            Toast.makeText(this, "ID giao dịch edit: " + idGiaodich, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Khong nhan duoc bundle", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveTransaction() {
        Double money = 0.0;
        try {
            money = Double.parseDouble(display.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(this, "Định dạng số không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        String lGiaoDich = nhomGiaoDich.getText().toString();

        if (lGiaoDich.isEmpty()) {
            Toast.makeText(this, "Lỗi: Nhóm giao dịch rỗng", Toast.LENGTH_SHORT).show();
            return;
        }

        String date = dateSelect;
        String note = note_content.getText().toString();

        dbHelper.editGiaoDich(idGiaodich, id_wallet, money, selectedGD, lGiaoDich, date, note);

        Intent myIntent = new Intent(editGiaoDich.this, MainActivity.class);
        startActivity(myIntent);
    }


    private void giaoDich() {
        Intent intent = getIntent();
        Bundle myBundle = intent.getBundleExtra("myPackageNhom");

        if (myBundle != null) {
            String nGiaoDich = myBundle.getString("loaiChiTieu");
            nhomGiaoDich.setText(nGiaoDich);
        } else {
            // Xử lý khi myBundle là null
        }
    }

    private void initViews() {
        display = findViewById(R.id.display);
        note_content = findViewById(R.id.noteEdit);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);
        btnDate = findViewById(R.id.btnDate);
        nhomGiaoDich = findViewById(R.id.textGroup);
        txtWallet = findViewById(R.id.wallet);


        getData();

        display.setText(String.valueOf(money));
        note_content.setText(note);
        nhomGiaoDich.setText(name);
        btnDate.setText(date);
        txtWallet.setText(wallet);
    }

    private void setupListeners() {
//        display.setOnClickListener(this::clearDisplayOnFirstClick);
        display.addTextChangedListener(textWatcher);

        nhomGiaoDich.setOnClickListener(v -> startActivityForResult(new Intent(editGiaoDich.this, nhom.class), REQUEST_CODE));
        btnSave.setOnClickListener(v -> saveTransaction());
        btnDate.setOnClickListener(v -> showDatePickerDialog());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Bundle myBundleNhom = data.getBundleExtra("myPackageNhom");
                if (myBundleNhom != null) {
                    String selectedNhom = myBundleNhom.getString("loaiChiTieu");
                    selectedGD = myBundleNhom.getString("nhomChiTieu");
                    nhomGiaoDich.setText(selectedNhom);
                }
            } else {
                Toast.makeText(this, "Không nhận được dữ liệu từ Activity nhom", Toast.LENGTH_SHORT).show();
            }
        }
    }


//    private void clearDisplayOnFirstClick(View v) {
//        if (getString(R.string.calculator).equals(display.getText().toString())) {
//            display.setText("");
//        }
//    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkFieldsForEmptyValues();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            dateSelect = String.format("%d/%d/%d", dayOfMonth, month1 + 1, year1);
            btnDate.setText(dateSelect);
            checkFieldsForEmptyValues();
        }, year, month, day);
        datePickerDialog.show();
    }


    private void checkFieldsForEmptyValues() {
        String money = display.getText().toString();
        String nGiaoDich = nhomGiaoDich.getText().toString();
        if (money.isEmpty() || dateSelect == null || dateSelect.isEmpty() || nGiaoDich.isEmpty()) {
            btnSave.setEnabled(false);
            btnSave.setBackgroundResource(R.drawable.disabled_button_background);
        } else {
            btnSave.setEnabled(true);
            btnSave.setBackgroundResource(R.drawable.enabled_button_background);
        }
    }

//    private void updateText(String strToAdd) {
//        String oldStr = display.getText().toString();
//        int cursorPos = display.getSelectionStart();
//        String leftStr = oldStr.substring(0, cursorPos);
//        String rightStr = oldStr.substring(cursorPos);
//
//        display.setText(String.format("%s%s%s", leftStr, strToAdd, rightStr));
//        display.setSelection(cursorPos + 1);
//    }
//
//    private boolean isThreeZeroBtnPressed(View view) {
//        return view.getId() == R.id.btn000; // Kiểm tra xem id của view có phải là btnThreeZero không
//    }
//
//    // Các phương thức xử lý các nút số và toán học
//    public void zeroBtn(View view) {
//        updateText("0");
//    }
//
//    public void threeZeroBtn(View view) {
//        updateText("000");
//    }
//
//    public void dotBtn(View view) {
//        updateText(".");
//    }
//
//    public void oneBtn(View view) {
//        updateText("1");
//    }
//
//    public void twoBtn(View view) {
//        updateText("2");
//    }
//
//    public void threeBtn(View view) {
//        updateText("3");
//    }
//
//    public void fourBtn(View view) {
//        updateText("4");
//    }
//
//    public void fiveBtn(View view) {
//        updateText("5");
//    }
//
//    public void sixBtn(View view) {
//        updateText("6");
//    }
//
//    public void sevenBtn(View view) {
//        updateText("7");
//    }
//
//    public void eightBtn(View view) {
//        updateText("8");
//    }
//
//    public void nineBtn(View view) {
//        updateText("9");
//    }
//
//    public void addBtn(View view) {
//        updateText("+");
//    }
//
//    public void subtractBtn(View view) {
//        updateText("-");
//    }
//
//    public void multiplyBtn(View view) {
//        updateText("×");
//    }
//
//    public void divideBtn(View view) {
//        updateText("/");
//    }
//
//    public void deleteBtn(View view) {
//        int cursorPos = display.getSelectionStart();
//        int textLen = display.getText().length();
//
//        if (cursorPos != 0 && textLen != 0) {
//            SpannableStringBuilder selection = (SpannableStringBuilder) display.getText();
//            selection.replace(cursorPos - 1, cursorPos, "");
//            display.setText(selection);
//            display.setSelection(cursorPos - 1);
//        }
//    }
//
//    public void restartBtn(View view) {
//        display.setText("");
//    }

//    public void equalBtn(View view) {
//        String userExp = display.getText().toString();
//        userExp = userExp.replaceAll("÷", "/");
//        userExp = userExp.replaceAll("×", "×");
//
//        Expression exp = new Expression(userExp);
//        String result = String.valueOf(exp.calculate());
//
//        display.setText(result);
//        display.setSelection(result.length());
//    }

}
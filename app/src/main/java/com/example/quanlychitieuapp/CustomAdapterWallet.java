package com.example.quanlychitieuapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

/**
 * CustomAdapterWallet: Adapter tùy chỉnh cho ExpandableListView
 * Hiển thị danh sách các giao dịch theo nhóm
 */
public class CustomAdapterWallet extends BaseExpandableListAdapter {
    private Context context;  // Ngữ cảnh của ứng dụng
    private List<String> listDataHeader;  // Danh sách tiêu đề nhóm
    private HashMap<String, List<GiaoDich>> listDataChild;  // Danh sách các giao dịch theo nhóm
    private HashMap<String, Integer> listIcons;  // Danh sách các biểu tượng theo nhóm

    // Constructor để khởi tạo Adapter
    public CustomAdapterWallet(Context context, List<String> listDataHeader, HashMap<String, List<GiaoDich>> listDataChild, HashMap<String, Integer> listIcons) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listDataChild;
        this.listIcons = listIcons;  // Lưu danh sách biểu tượng
    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();  // Trả về số lượng nhóm
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listDataChild.get(listDataHeader.get(groupPosition)).size();  // Trả về số lượng phần tử con trong một nhóm
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listDataHeader.get(groupPosition);  // Trả về đối tượng nhóm
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);  // Trả về đối tượng con trong một nhóm
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;  // Trả về ID của nhóm
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;  // Trả về ID của phần tử con
    }

    @Override
    public boolean hasStableIds() {
        return false;  // Trả về false vì ID của các phần tử không ổn định
    }

    // Tạo và trả về View cho nhóm (group) trong danh sách
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);  // Lấy tiêu đề nhóm
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.head_listview_vi, null);  // Nạp layout cho nhóm
        }

        TextView lblListHeader = convertView.findViewById(R.id.groupTitle);  // Lấy TextView để hiển thị tiêu đề nhóm
        lblListHeader.setText(headerTitle);  // Đặt tiêu đề nhóm

        return convertView;
    }

    // Tạo và trả về View cho phần tử con (child) trong nhóm
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final GiaoDich giaoDich = (GiaoDich) getChild(groupPosition, childPosition);  // Lấy đối tượng giao dịch
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_with_icon, null);  // Nạp layout cho phần tử con
        }

        TextView txtListChild = convertView.findViewById(R.id.text_view);  // Lấy TextView để hiển thị thông tin giao dịch
        txtListChild.setText(giaoDich.toString());  // Đặt thông tin giao dịch

        ImageView iconImageView = convertView.findViewById(R.id.icon);  // Lấy ImageView để hiển thị biểu tượng
        int iconResource = getIconResourceForTransaction(giaoDich);  // Lấy resource biểu tượng tương ứng
        if (iconResource != 0) {
            iconImageView.setImageResource(iconResource);  // Đặt biểu tượng tương ứng
        } else {
            iconImageView.setImageResource(R.drawable.outline_comment_24);  // Nếu không có biểu tượng thì sử dụng biểu tượng mặc định
        }

        // Đặt sự kiện click cho phần tử con
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị Toast khi người dùng chọn phần tử
                Toast.makeText(context, "Bạn đã chọn: " + giaoDich.toString(), Toast.LENGTH_SHORT).show();

                // Chuyển sang activity chi tiết chi tiêu khi người dùng click vào phần tử con
                Intent myIntent = new Intent(context, chitietchitieu.class);
                Bundle myBundle = new Bundle();
                myBundle.putInt("id_giaodich", giaoDich.getId());  // Thêm id của giao dịch vào bundle
                myBundle.putInt("id_wallet", giaoDich.getIdWallet());
                myBundle.putString("name", giaoDich.group_name);
                myBundle.putInt("money", giaoDich.money);
                myBundle.putString("loai_giaodich", giaoDich.loai_giaoDich);
                myBundle.putString("date", giaoDich.date);
                myBundle.putString("note", giaoDich.note);

                myIntent.putExtra("myPackageChiTietChiTieu", myBundle);  // Đặt bundle vào intent
                context.startActivity(myIntent);  // Bắt đầu activity chi tiết chi tiêu
            }
        });

        return convertView;  // Trả về view của phần tử con
    }

    private int getIconResourceForTransaction(GiaoDich giaoDich) {
        // Các biểu tượng được chọn dựa trên tên nhóm của giao dịch
        switch (giaoDich.getGroup_name()) {
            case "Ăn uống":
                return R.drawable.baseline_fastfood_24;
            case "Bảo hiểm":
                return R.drawable.outline_medical_information_24;
            case "Đầu tư":
                return R.drawable.baseline_monetization_on_24;
            case "Di chuyển":
                return R.drawable.baseline_directions_car_filled_24;
            case "Các chi phí khác":
                return R.drawable.baseline_monetization_on_24;
            case "Lương":
                return R.drawable.baseline_monetization_on_24;
            case "Thu nhập khác":
                return R.drawable.baseline_monetization_on_24;
            case "Tiền chuyển đến":
                return R.drawable.baseline_monetization_on_24;
            default:
                return R.drawable.outline_comment_24;  // Biểu tượng mặc định nếu không có loại giao dịch phù hợp
        }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;  // Các phần tử con có thể chọn được
    }
}

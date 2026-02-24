package com.example.quanlychitieuapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class walletAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final List<String> values;
    private final int icon; // Sử dụng int duy nhất thay vì int[] icons

    public walletAdapter(Context context, List<String> values, int icon) {
        super(context, R.layout.list_item_with_icon, values);
        this.context = context;
        this.values = values;
        this.icon = icon; // Lưu icon duy nhất
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_item_with_icon, parent, false);
        }

        TextView textView = rowView.findViewById(R.id.text_view);
        ImageView imageView = rowView.findViewById(R.id.icon);

        textView.setText(values.get(position));

        // Sử dụng icon duy nhất cho tất cả các mục trong ListView
        imageView.setImageResource(icon);

        return rowView;
    }
}

package com.example.quanlychitieuapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<String> {

        private final Context context;
        private final List<String> values;
        private final int[] icons;

        public CustomAdapter(Context context, List<String> values, int[] icons) {
            super(context, R.layout.list_item_with_icon, values);
            this.context = context;
            this.values = values;
            this.icons = icons;
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

            // Kiểm tra nếu vị trí của biểu tượng hợp lệ trước khi thiết lập nó cho ImageView
            if (position < icons.length) {
                imageView.setImageResource(icons[position]);
            } else {
                // Thiết lập biểu tượng mặc định hoặc ẩn ImageView nếu không có biểu tượng phù hợp
                imageView.setImageResource(R.drawable.ic_launcher_foreground); // Biểu tượng mặc định
                // hoặc imageView.setVisibility(View.GONE); để ẩn ImageView
            }

            return rowView;
        }
    }



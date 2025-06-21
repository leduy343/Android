package com.example.tlunavigator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CourseAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final List<String> courses;

    public CourseAdapter(Context context, List<String> courses) {
        super(context, 0, courses);
        this.context = context;
        this.courses = courses;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String course = courses.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_course, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.courseName);
        ImageView iconView = convertView.findViewById(R.id.courseIcon);

        textView.setText(course);
        // iconView.setImageResource(R.drawable.ic_book); // nếu bạn có icon riêng
        iconView.setImageResource(android.R.drawable.ic_menu_info_details); // tạm dùng icon hệ thống

        return convertView;
    }
}

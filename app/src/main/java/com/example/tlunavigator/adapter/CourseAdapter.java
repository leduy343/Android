package com.example.tlunavigator.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tlunavigator.R;
import com.example.tlunavigator.model.Course;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    private List<Course> courses;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Course course);
    }

    public CourseAdapter(List<Course> courses, OnItemClickListener listener) {
        this.courses = courses;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCourse;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCourse = itemView.findViewById(R.id.tvCourse);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Course course = courses.get(position);
        holder.tvCourse.setText(course.getName());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(course));
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }
}

package com.example.tlunavigator;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tlunavigator.adapter.CourseAdapter;
import com.example.tlunavigator.model.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Course> courseList;
    private CourseAdapter adapter;
    private String majorCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        recyclerView = findViewById(R.id.recyclerCourses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        majorCode = getIntent().getStringExtra("majorCode");
        courseList = new ArrayList<>();

        // Môn học cứng cho ngành CNTT
        if ("7480201".equals(majorCode)) {
            courseList.add(new Course("CongNgheWeb", "Công nghệ Web"));
            courseList.add(new Course("CoSoDuLieu", "Cơ sở dữ liệu"));
        }

        adapter = new CourseAdapter(courseList, course -> {
            Intent intent = new Intent(CourseListActivity.this, UserAllDocumentsActivity.class);
            intent.putExtra("majorCode", majorCode);
            intent.putExtra("subjectCode", course.getCode()); // truyền mã môn học
            startActivity(intent);

        });

        recyclerView.setAdapter(adapter);
    }
}


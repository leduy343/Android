package com.example.tlunavigator;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tlunavigator.model.Major;

import java.util.ArrayList;
import java.util.List;

public class MajorListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Major> majorList = new ArrayList<>();
    private MajorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_major_list);

        recyclerView = findViewById(R.id.recyclerMajors);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Danh sách ngành cố định
        majorList.add(new Major("7480201", "Công nghệ thông tin"));
        majorList.add(new Major("7340101", "Quản trị kinh doanh"));
        majorList.add(new Major("7580201", "Kỹ thuật xây dựng"));
        majorList.add(new Major("7520216", "Kỹ thuật cấp thoát nước"));
        majorList.add(new Major("7580210", "Kỹ thuật cơ sở hạ tầng"));
        majorList.add(new Major("7520320", "Kỹ thuật môi trường"));

        adapter = new MajorAdapter(majorList, major -> {
            if ("Công nghệ thông tin".equals(major.getName())) {
                // 👉 Chỉ xử lý khi là ngành CNTT
                Intent intent = new Intent(MajorListActivity.this, SubjectActivity.class);
                startActivity(intent);
            }
            // 👉 Các ngành khác sẽ không làm gì cả
        });

        recyclerView.setAdapter(adapter);
    }
}

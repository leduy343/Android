package com.example.tlunavigator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

public class SubjectActivity extends AppCompatActivity {

    List<String> subjects = Arrays.asList("Công nghệ Web", "Cơ sở dữ liệu", "Lập trình Java");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        ListView listView = findViewById(R.id.subjectListView);

        CourseAdapter adapter = new CourseAdapter(this, subjects);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, view, position, id) -> {
            Intent intent = new Intent(SubjectActivity.this, UserAllDocumentsActivity.class);
            intent.putExtra("subject", subjects.get(position));
            startActivity(intent);
        });
    }
}

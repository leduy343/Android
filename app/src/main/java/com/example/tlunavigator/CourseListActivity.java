package com.example.tlunavigator;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tlunavigator.adapter.CourseAdapter;
import com.example.tlunavigator.model.Course;
import com.example.tlunavigator.model.Subject;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Course> courseList;
    private CourseAdapter adapter;
    private String majorCode;
    private DatabaseReference Ref;
    private Map<String, Subject> subjectMap = new HashMap<>();

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
            intent.putExtra("subjectid", course.getCode()); // truyền mã môn học
            startActivity(intent);

        });

        recyclerView.setAdapter(adapter);
        View();
        loadAllSubjects();
    }

    private void View() {
        DatabaseReference viewRef = FirebaseDatabase.getInstance()
                .getReference("Program")
                .child(majorCode)
                .child("views");
        viewRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                Integer integer = currentData.getValue(Integer.class);
                if(integer == null){
                    currentData.setValue(1);
                }else {
                    currentData.setValue(integer+1);
                }
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {

            }
        });
    }

    private void loadAllSubjects() {
        DatabaseReference subjectRef = FirebaseDatabase.getInstance().getReference("Subjects");
        subjectRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subjectMap.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Subject subject = snap.getValue(Subject.class);
                    if (subject != null) {
                        subjectMap.put(subject.id, subject);
                    }
                }
                loadProgram();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CourseListActivity.this, "Lỗi tải môn học", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadProgram() {
        Ref = FirebaseDatabase.getInstance().getReference("Program").child(majorCode).child("subjects");
        Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                courseList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    String subjectId = snap.getKey();
                    String type = snap.getValue(String.class);

                    Subject subject = subjectMap.get(subjectId);
                    Course course =new Course(subjectId,subject.name);
                    if (subject != null) {
                        courseList.add(course);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CourseListActivity.this, "Lỗi tải dữ liệu chương trình", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


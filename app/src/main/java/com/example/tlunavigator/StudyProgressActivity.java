package com.example.tlunavigator;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tlunavigator.adapter.ProgressAdapter;
import com.example.tlunavigator.model.StudyProgressItem;
import com.example.tlunavigator.model.Subject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudyProgressActivity extends AppCompatActivity {

    private RecyclerView recyclerCompleted, recyclerUncompleted;
    private List<String> completedList = new ArrayList<>();
    private List<String> uncompletedList = new ArrayList<>();
    private ProgressAdapter completedAdapter, uncompletedAdapter;

    private DatabaseReference userRef, subjectsRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_study_progress);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerCompleted = findViewById(R.id.recyclerCompleted);
        recyclerUncompleted = findViewById(R.id.recyclerUncompleted);

        recyclerCompleted.setLayoutManager(new LinearLayoutManager(this));
        recyclerUncompleted.setLayoutManager(new LinearLayoutManager(this));

        completedAdapter = new ProgressAdapter(this, completedList);
        uncompletedAdapter = new ProgressAdapter(this, uncompletedList);

        recyclerCompleted.setAdapter(completedAdapter);
        recyclerUncompleted.setAdapter(uncompletedAdapter);

        loadStudyProgress();
    }
    private void loadStudyProgress() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                completedList.clear();
                uncompletedList.clear();

                for (DataSnapshot markSnap : snapshot.child("marks").getChildren()) {
                    String item = markSnap.getValue(String.class);
                    if (item != null && item.contains("-")) {
                        int colonIndex = item.lastIndexOf("-");
                        String subjectname = item.substring(0, colonIndex).trim();
                        Log.d("TAG", subjectname);
                        completedList.add(subjectname);
                    }
                }
                completedAdapter.notifyDataSetChanged();

                for (DataSnapshot ucSnap : snapshot.child("uncompletedSubjects").getChildren()) {
                    String value = ucSnap.getValue(String.class);
                    if (value != null && value.contains(" - ")) {
                        String[] parts = value.split(" - ", 2);
                        String code = parts[0];
                        String name = parts[1];
                        Log.d("TAG", name+"("+code+")");
                        uncompletedList.add(name+"("+code+")");
                    }
                }
                uncompletedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StudyProgressActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
package com.example.tlunavigator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tlunavigator.adapter.SubInProgramAdapter;
import com.example.tlunavigator.model.Subject;
import com.example.tlunavigator.model.SubjectWithType;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubjectinProgram extends AppCompatActivity {
    private FloatingActionButton btnAdd;
    private RecyclerView recyclerView;
    private SubInProgramAdapter adapter;
    private List<SubjectWithType> subList;
    private Map<String, Subject> subjectMap = new HashMap<>();
    private DatabaseReference Ref;
    private String programID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_subjectin_program);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        programID = getIntent().getStringExtra("ProgramId");
        btnAdd = findViewById(R.id.btnAdd);
        recyclerView = findViewById(R.id.recyclerSub);
        subList = new ArrayList<>();
        adapter = new SubInProgramAdapter(this, subList,programID);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);



        Ref = FirebaseDatabase.getInstance().getReference("Program").child(programID).child("subjects");

        loadAllSubjects();
        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(SubjectinProgram.this, AddSubjectToProgramActivity.class);
            intent.putExtra("ProgramId", programID);
            startActivity(intent);
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
                Toast.makeText(SubjectinProgram.this, "Lỗi tải môn học", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadProgram() {
        Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    String subjectId = snap.getKey();
                    String type = snap.getValue(String.class);

                    Subject subject = subjectMap.get(subjectId);

                    if (subject != null) {
                        subList.add(new SubjectWithType(subject, type));
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SubjectinProgram.this, "Lỗi tải dữ liệu chương trình", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

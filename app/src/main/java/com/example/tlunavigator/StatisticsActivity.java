package com.example.tlunavigator;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tlunavigator.adapter.ImprovementAdapter;
import com.example.tlunavigator.adapter.ProgramAdapter;
import com.example.tlunavigator.adapter.SubjectAdapter;
import com.example.tlunavigator.model.Subject;
import com.example.tlunavigator.model.TrainingProgram;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSubject,recyclerViewProgram;
    private ImprovementAdapter subjectAdapter ,programAdapter;

    private List<String> subjectList, ProgramList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_statistics);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerViewProgram = findViewById(R.id.recyclerviewProgram);
        recyclerViewProgram.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSubject = findViewById(R.id.recyclerviewSubject);
        recyclerViewSubject.setLayoutManager(new LinearLayoutManager(this));
        subjectList = new ArrayList<>();
        ProgramList = new ArrayList<>();
        subjectAdapter = new ImprovementAdapter(StatisticsActivity.this,subjectList);
        programAdapter = new ImprovementAdapter(StatisticsActivity.this,ProgramList);
        recyclerViewSubject.setAdapter(subjectAdapter);
        recyclerViewProgram.setAdapter(programAdapter);
        LoadSubjects();
        LoadProgram();
    }

    private void LoadSubjects() {
        DatabaseReference rf = FirebaseDatabase.getInstance().getReference("Subjects");
        rf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Subject> subject = new ArrayList<>();
                for(DataSnapshot snap : snapshot.getChildren() ){
                        subject.add(snap.getValue(Subject.class));
                }
                subject.sort((a, b) -> Integer.compare(b.views, a.views));

                for (int i = 0; i < Math.min(3, subject.size()); i++) {
                    Subject top = subject.get(i);
                    subjectList.add(top.name+":"+top.views);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void LoadProgram() {
        DatabaseReference programRef = FirebaseDatabase.getInstance().getReference("Program");

        programRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<TrainingProgram> program = new ArrayList<>();

                for (DataSnapshot snap : snapshot.getChildren()) {
                    TrainingProgram item = snap.getValue(TrainingProgram.class);
                    if (item != null) {
                        program.add(item);
                    }
                }

                program.sort((a, b) -> Integer.compare(b.views, a.views));

                for (int i = 0; i < Math.min(3, program.size()); i++) {
                    TrainingProgram top = program.get(i);
                    ProgramList.add(top.name+":"+top.views);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}
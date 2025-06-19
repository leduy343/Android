package com.example.tlunavigator;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tlunavigator.model.Subject;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddSubjectToProgramActivity extends AppCompatActivity {
    private AutoCompleteTextView autoCompleteSubject;
    private RadioGroup rgType;
    private Button btnAdd;

    private String programId;
    private List<Subject> subjectList = new ArrayList<>();
    private ArrayAdapter<String> subjectAdapter;
    private Map<String, String> subjectIdMap = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_subject_to_program);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        autoCompleteSubject = findViewById(R.id.autoCompleteSubject);
        rgType = findViewById(R.id.rgType);
        btnAdd = findViewById(R.id.btnAddSubject);

        programId = getIntent().getStringExtra("ProgramId");
        loadSubjects();

        btnAdd.setOnClickListener(v -> {
            addSubjectToProgram();
            finish();
        });
    }
    private void loadSubjects() {
        FirebaseDatabase.getInstance().getReference("Subjects")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<String> names = new ArrayList<>();
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            Subject s = snap.getValue(Subject.class);
                            names.add(s.name);
                            subjectIdMap.put(s.name, s.id);
                        }
                        subjectAdapter = new ArrayAdapter<>(AddSubjectToProgramActivity.this,
                                android.R.layout.simple_spinner_item, names);
                        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        autoCompleteSubject.setAdapter(subjectAdapter);
                        autoCompleteSubject.setThreshold(1);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
    }

    private void addSubjectToProgram() {
        String selectedName = autoCompleteSubject.getText().toString();
        String subjectId = subjectIdMap.get(selectedName);

        int checkedId = rgType.getCheckedRadioButtonId();
        String type = "required";
        if (checkedId == R.id.rbElective) {
            type = "elective";
        }

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("Program")
                .child(programId)
                .child("subjects");

        ref.child(subjectId).setValue(type)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
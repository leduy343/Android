package com.example.tlunavigator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tlunavigator.adapter.SubjectAdapter;
import com.example.tlunavigator.model.Subject;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageSubjectsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SubjectAdapter adapter;
    private List<Subject> subjectList;
    private DatabaseReference subjectsRef;
    private FloatingActionButton btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_subjects);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerView = findViewById(R.id.recyclerSubjects);
        btnAdd = findViewById(R.id.btnAddSubject);

        subjectList = new ArrayList<>();
        adapter = new SubjectAdapter(this, subjectList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        subjectsRef = FirebaseDatabase.getInstance().getReference("Subjects");

        loadSubjects();

        btnAdd.setOnClickListener(v -> showAddDialog());
    }

    private void loadSubjects() {
        subjectsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subjectList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Subject sub = snap.getValue(Subject.class);
                    subjectList.add(sub);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ManageSubjectsActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_subject, null);

        EditText etName = view.findViewById(R.id.etSubjectName);
        EditText etCode = view.findViewById(R.id.etSubjectCode);
        EditText etCredit = view.findViewById(R.id.etSubjectCredit);

        builder.setView(view)
                .setTitle("Thêm môn học")
                .setPositiveButton("Thêm", (dialog, which) -> {
                    String name = etName.getText().toString();
                    String code = etCode.getText().toString();
                    int credit = Integer.parseInt(etCredit.getText().toString());

                    String id = subjectsRef.push().getKey();
                    Subject subject = new Subject(id, name, code, credit);
                    subjectsRef.child(id).setValue(subject);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
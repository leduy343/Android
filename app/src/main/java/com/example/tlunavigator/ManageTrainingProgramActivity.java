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

import com.example.tlunavigator.adapter.ProgramAdapter;
import com.example.tlunavigator.model.Subject;
import com.example.tlunavigator.model.TrainingProgram;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageTrainingProgramActivity extends AppCompatActivity {

    private FloatingActionButton btnAdd;
    private RecyclerView recyclerView;
    private ProgramAdapter adapter;
    private List<TrainingProgram> ProgramList;
    private DatabaseReference ProgramRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_training_program);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerProgram);
        btnAdd = findViewById(R.id.btnProgram);
        ProgramRef = FirebaseDatabase.getInstance().getReference("Program");

        ProgramList = new ArrayList<>();
        adapter = new ProgramAdapter(this,ProgramList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        loadProgram();

        btnAdd.setOnClickListener( v-> showAddDialog());
    }
    private void loadProgram(){
        ProgramRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ProgramList.clear();
                for(DataSnapshot snap : snapshot.getChildren()){
                    TrainingProgram TP = snap.getValue(TrainingProgram.class);
                    ProgramList.add(TP);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ManageTrainingProgramActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showAddDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_program, null);

        EditText etName = view.findViewById(R.id.etProgramName);
        EditText etCode = view.findViewById(R.id.etProgramCode);
        EditText etCredit = view.findViewById(R.id.etProgramCredit);

        builder.setView(view)
                .setTitle("Thêm chương trình đào tạo")
                .setPositiveButton("Thêm", (dialog, which) -> {
                    String name = etName.getText().toString();
                    String code = etCode.getText().toString();
                    int credit = Integer.parseInt(etCredit.getText().toString());

                    String id = ProgramRef.push().getKey();
                    Subject subject = new Subject(id, name, code, credit);
                    ProgramRef.child(id).setValue(subject);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
package com.example.tlunavigator;

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

import com.example.tlunavigator.adapter.ImprovementAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ImprovementSubjectsActivity extends AppCompatActivity {

    DatabaseReference rf ;
    ArrayList<String> improvementSubjects = new ArrayList<>();

    RecyclerView recyclerView;
    ImprovementAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_improvement_subjects);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerView =findViewById(R.id.recyclerImprov);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getmark();

    }

    private void getmark(){
        rf = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("marks");
        rf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap: snapshot.getChildren()){
                    String item = snap.getValue(String.class);
                    if (item != null && item.contains(":")) {
                        int lastColon = item.lastIndexOf(":");
                        String name = item.substring(0, lastColon).trim();
                        String gradeStr = item.substring(lastColon + 1).trim();
                            if (gradeStr.equals("D") || gradeStr.equals("C") || gradeStr.equals("F")) {
                                improvementSubjects.add(name + ": " + gradeStr);
                            }
                    }
                }
                adapter = new ImprovementAdapter(ImprovementSubjectsActivity.this,improvementSubjects);

                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ImprovementSubjectsActivity.this, "Lỗi tải điểm", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
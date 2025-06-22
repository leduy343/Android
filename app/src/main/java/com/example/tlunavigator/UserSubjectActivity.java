package com.example.tlunavigator;

import android.os.Bundle;
import android.provider.ContactsContract;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tlunavigator.adapter.UsersubjectAdapter;
import com.example.tlunavigator.model.Subject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserSubjectActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference subrf;
    private UsersubjectAdapter adapter;
    private List<Subject> subjects;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_subject);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        subjects = new ArrayList<>();
        adapter = new UsersubjectAdapter(UserSubjectActivity.this,subjects);
        recyclerView.setAdapter(adapter);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        subrf = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("speciality");
        subrf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getValue() != null){
                    String speciality = snapshot.getValue().toString();
                    getsubjectID(speciality);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getsubjectID(String speciality) {
        DatabaseReference rf  = FirebaseDatabase.getInstance().getReference("Program");
        rf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    List<String> list = new ArrayList<>();
                    for (DataSnapshot snap : snapshot.getChildren()){
                        String name  = snap.child("name").getValue().toString();

                        if(name!= null && name.equals(speciality)){
                            DataSnapshot subjectsSnap = snap.child("subjects");
                           for (DataSnapshot item : subjectsSnap.getChildren()){
                               list.add(item.getKey());

                           }
                        }
                    }
                    loadSub(list);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadSub(List<String> subjectIds) {
        DatabaseReference rf  = FirebaseDatabase.getInstance().getReference("Subjects");
        rf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subjects.clear();
                for (String id : subjectIds) {
                    if (snapshot.hasChild(id)) {
                        Subject subject = snapshot.child(id).getValue(Subject.class);
                        if (subject != null) {
                            subjects.add(subject);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
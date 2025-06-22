package com.example.tlunavigator;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tlunavigator.adapter.GPAresultAdapter;
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

public class GPAResultActivity extends AppCompatActivity {

    private TextView txtTotalCreditsPassed,txtGPA;
    private RecyclerView recyclerView;
    private DatabaseReference gpaRef;
    private GPAresultAdapter adapter;
    private List<Map<String,String>> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gparesult);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txtGPA = findViewById(R.id.txtGPA);
        txtTotalCreditsPassed = findViewById(R.id.txtTotalCreditsPassed);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new GPAresultAdapter(GPAResultActivity.this,list);
        recyclerView.setAdapter(adapter);
        getGPA();

    }

    private void getGPA() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        gpaRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        gpaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String creditsPassed =  snapshot.child("totalCreditsPassed").getValue().toString();
                String AvgGPA = snapshot.child("avgMark4").getValue().toString();
                txtGPA.setText(AvgGPA);
                txtTotalCreditsPassed.setText(creditsPassed);
                DataSnapshot SchoolYears = snapshot.child("schoolYears");
                int index = 0;
                for (DataSnapshot yearSnap : SchoolYears.getChildren()){
                    DataSnapshot semestersSnap = yearSnap.child("semesters");
                    for (DataSnapshot item : semestersSnap.getChildren()){
                        String i = index+"";
                        String gpa = item.child("avg4").getValue().toString();
                        String CreditStudied =item.child("creditsStudied").getValue().toString();
                        index+=1;
                        Map<String, String> Result = new HashMap<>();
                        Result.put("Học kì " + index, "Tín chỉ: " + CreditStudied + "- GPA: " + gpa);
                        list.add(Result);
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
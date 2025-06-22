package com.example.tlunavigator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDashboardActivity extends AppCompatActivity {

    private ImageButton btnDocs,btnInfo,btnRetake,btnsubject,btnProgress,btnResult;
    private TextView txtGPA;
    DatabaseReference dataref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        btnResult = findViewById(R.id.btnResult);
        btnDocs = findViewById(R.id.btnDocs);
        btnInfo = findViewById(R.id.btnInfo);
        btnRetake = findViewById(R.id.btnRetake);
        btnsubject = findViewById(R.id.btnSubject);
        btnProgress = findViewById(R.id.btnProgress);
        txtGPA = findViewById(R.id.txtGPA);
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDashboardActivity.this, UserinfoActivity.class);
                startActivity(intent);
            }
        });
//        btnDocs.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(UserDashboardActivity.this, UserinfoActivity.class);
//                startActivity(intent);
//            }
//        });

        btnRetake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDashboardActivity.this, ImprovementSubjectsActivity.class);
                startActivity(intent);
            }
        });
        btnsubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDashboardActivity.this,UserSubjectActivity.class);
                startActivity(intent);
            }
        });
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dataref = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("avgMark4");
        dataref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getValue() != null) {
                    String GPA = snapshot.getValue().toString();
                    txtGPA.setText("ẠN ĐÃ ĐẠT ĐƯỢC "+ GPA+"/4 GPA");
                } else {
                    txtGPA.setText("Chưa có dữ liệu GPA");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserDashboardActivity.this, "Không thể tải điểm GPA", Toast.LENGTH_SHORT).show();
            }
        });

        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDashboardActivity.this, GPAResultActivity.class);
                startActivity(intent);
            }
        });
        btnProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDashboardActivity.this, StudyProgressActivity.class);
                startActivity(intent);
            }
        });
        btnDocs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDashboardActivity.this, MajorListActivity.class);
                startActivity(intent);
            }
        });
    }
}

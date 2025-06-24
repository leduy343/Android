package com.example.tlunavigator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminMainActivity extends AppCompatActivity {

    private ImageView btnManageUsers, btnManageSubjects, btnManageDocuments, btnProgram, btnStatistic;
    private TextView txtsubject,txtprogram,txtdoc;

    private DatabaseReference subdb,prodb,docdb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnStatistic =findViewById(R.id.btnStatistics);
        btnProgram = findViewById(R.id.btnChuongTrinh);
        btnManageUsers = findViewById(R.id.btnManageUsers);
        btnManageSubjects = findViewById(R.id.btnManageSubjects);
        btnManageDocuments = findViewById(R.id.btnManageDocuments);

        txtdoc = findViewById(R.id.txtdoc);
        txtprogram = findViewById(R.id.txtProgram);
        txtsubject = findViewById(R.id.txtsubject);

        btnProgram.setOnClickListener( v-> {
            Intent intent =new Intent(AdminMainActivity.this,ManageTrainingProgramActivity.class);
            startActivity(intent);
        });
        btnManageUsers.setOnClickListener(v -> {
            Intent intent = new Intent(AdminMainActivity.this, ManageUsersActivity.class);
            startActivity(intent);
        });

        btnManageSubjects.setOnClickListener(v ->{
                Intent intent = new Intent(AdminMainActivity.this,ManageSubjectsActivity.class);
                startActivity(intent);
        });
        btnManageDocuments = findViewById(R.id.btnManageDocuments);
        btnManageDocuments.setOnClickListener(v -> {
            Intent intent = new Intent(this, ManageDocumentsActivity.class);
            startActivity(intent);
        });

        btnStatistic.setOnClickListener(v->{
                Intent intent = new Intent(AdminMainActivity.this, StatisticsActivity.class);
                startActivity(intent);
        });

        subdb = FirebaseDatabase.getInstance().getReference("Subjects");
        subdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long count  = snapshot.getChildrenCount();
                txtsubject.setText(count+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        prodb = FirebaseDatabase.getInstance().getReference("Program");
        prodb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long count  = snapshot.getChildrenCount();
                txtprogram.setText(count+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        docdb = FirebaseDatabase.getInstance().getReference("Documents");
        docdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long count  = snapshot.getChildrenCount();
                txtdoc.setText(count+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
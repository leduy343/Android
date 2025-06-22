package com.example.tlunavigator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminMainActivity extends AppCompatActivity {

    private ImageView btnManageUsers, btnManageSubjects, btnManageDocuments, btnProgram;
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
        btnProgram = findViewById(R.id.btnChuongTrinh);
        btnManageUsers = findViewById(R.id.btnManageUsers);
        btnManageSubjects = findViewById(R.id.btnManageSubjects);
        btnManageDocuments = findViewById(R.id.btnManageDocuments);
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

    }
}
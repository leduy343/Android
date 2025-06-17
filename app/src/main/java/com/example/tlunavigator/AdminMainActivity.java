package com.example.tlunavigator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminMainActivity extends AppCompatActivity {

    private ImageButton btnManageUsers, btnManageSubjects, btnManageDocuments;
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
        btnManageUsers = findViewById(R.id.btnManageUsers);
        btnManageSubjects = findViewById(R.id.btnManageSubjects);
        btnManageUsers.setOnClickListener(v -> {
            Intent intent = new Intent(AdminMainActivity.this, ManageUsersActivity.class);
            startActivity(intent);
        });

        btnManageSubjects.setOnClickListener(v ->{
                Intent intent = new Intent(AdminMainActivity.this,ManageSubjectsActivity.class);
                startActivity(intent);
        });
    }
}
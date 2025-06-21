package com.example.tlunavigator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class UserDashboardActivity extends AppCompatActivity {

    private ImageButton btnDocs,btnInfo,btnRetake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        btnDocs = findViewById(R.id.btnDocs);
        btnInfo = findViewById(R.id.btnInfo);
        btnRetake = findViewById(R.id.btnRetake);
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDashboardActivity.this, UserinfoActivity.class);
                startActivity(intent);
            }
        });
        btnDocs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDashboardActivity.this, UserAllDocumentsActivity.class);
                startActivity(intent);
            }
        });
        btnRetake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDashboardActivity.this, ImprovementSubjectsActivity.class);
                startActivity(intent);
            }
        });
    }
}

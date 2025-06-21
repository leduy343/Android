package com.example.tlunavigator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class UserDashboardActivity extends AppCompatActivity {

    private LinearLayout btnDocs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        // Ánh xạ nút "Tài liệu"
        btnDocs = findViewById(R.id.btnDocs);

        // Xử lý khi bấm vào "Tài liệu"
        btnDocs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang UserAllDocumentActivity
                Intent intent = new Intent(UserDashboardActivity.this, MajorListActivity.class);
                startActivity(intent);
            }
        });
    }
}

package com.example.tlunavigator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tlunavigator.adapter.DocumentAdapter;
import com.example.tlunavigator.model.Document;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserAllDocumentsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DocumentAdapter adapter;
    private List<Document> documentList;
    private DatabaseReference dbRef;

    private String subjectFilter = null; // dùng để lọc theo subject nếu có

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_all_documents);

        recyclerView = findViewById(R.id.recyclerAllDocuments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        documentList = new ArrayList<>();

        // 🔍 Đọc role từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserRole", MODE_PRIVATE);
        String role = prefs.getString("role", "user");
        boolean isAdmin = "admin".equals(role);

        // ✅ Truyền isAdmin vào Adapter
        adapter = new DocumentAdapter(this, documentList, isAdmin);
        recyclerView.setAdapter(adapter);

        // 👉 Nhận subject từ intent (nếu có)
        subjectFilter = getIntent().getStringExtra("subject");

        dbRef = FirebaseDatabase.getInstance().getReference("Documents");

        loadFilteredDocuments();
    }

    private void loadFilteredDocuments() {
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                documentList.clear();
                for (DataSnapshot docSnap : snapshot.getChildren()) {
                    Document doc = docSnap.getValue(Document.class);
                    if (doc != null) {
                        if (subjectFilter == null || subjectFilter.equalsIgnoreCase(doc.getSubjectName())) {
                            documentList.add(doc);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserAllDocumentsActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

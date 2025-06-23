package com.example.tlunavigator;

import android.content.SharedPreferences;
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

import com.example.tlunavigator.adapter.DocumentAdapter;
import com.example.tlunavigator.model.Document;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DocumentOfSubjectActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DocumentAdapter adapter;
    private List<Document> documentList;
    private DatabaseReference documentsRef;
    private String subjectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_document_of_subject);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerViewDocuments);
        documentList = new ArrayList<>();

        // ✅ Lấy role từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserRole", MODE_PRIVATE);
        String role = prefs.getString("role", "user");
        boolean isAdmin = "admin".equals(role);

        // ✅ Truyền vào adapter
        adapter = new DocumentAdapter(this, documentList, isAdmin);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Lấy subjectId từ Intent
        subjectId = getIntent().getStringExtra("subjectid");

        documentsRef = FirebaseDatabase.getInstance().getReference("Documents");

        loadDocumentsForSubject();
    }

    private void loadDocumentsForSubject() {
        documentsRef.orderByChild("subjectId").equalTo(subjectId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        documentList.clear();
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            Document doc = snap.getValue(Document.class);
                            documentList.add(doc);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(DocumentOfSubjectActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

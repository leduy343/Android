package com.example.tlunavigator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tlunavigator.model.Document;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageDocumentsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DocumentAdapter adapter;
    private List<Document> documentList;
    private DatabaseReference documentsRef;
    private FloatingActionButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_documents);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerDocument);
        btnAdd = findViewById(R.id.btnAddDocument);

        documentList = new ArrayList<>();
        adapter = new DocumentAdapter(this, documentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        documentsRef = FirebaseDatabase.getInstance().getReference("Documents");

        loadDocuments();

        btnAdd.setOnClickListener(v -> showAddDialog());
    }

    private void loadDocuments() {
        documentsRef.addValueEventListener(new ValueEventListener() {
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
                Toast.makeText(ManageDocumentsActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_document, null);

        EditText etSubjectsName = view.findViewById(R.id.etSubjectsName);
        EditText etDocumentName = view.findViewById(R.id.etDocumentName);
        EditText etType = view.findViewById(R.id.etType);

        builder.setView(view)
                .setTitle("Thêm tài liệu")
                .setPositiveButton("Thêm", (dialog, which) -> {
                    String subjectName = etSubjectsName.getText().toString();
                    String documentName = etDocumentName.getText().toString();
                    String type = etType.getText().toString();

                    String id = documentsRef.push().getKey();
                    Document document = new Document(id, subjectName, documentName, type);
                    documentsRef.child(id).setValue(document);
                })
                .setNegativeButton("Hủy", null);
    }
}
    

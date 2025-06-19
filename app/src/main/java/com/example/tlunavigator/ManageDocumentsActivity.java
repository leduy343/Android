package com.example.tlunavigator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tlunavigator.model.Document;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageDocumentsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DocumentAdapter adapter;
    private List<Document> documentList;
    private DatabaseReference documentsRef;
    private FloatingActionButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_documents);

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
    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_document, null);
        builder.setView(view);

        AutoCompleteTextView autoSubject = view.findViewById(R.id.autoCompleteSubject);
        EditText etDocumentName = view.findViewById(R.id.etDocumentName);
        EditText etType = view.findViewById(R.id.etType);

        List<String> subjectNames = new ArrayList<>();
        Map<String, String> subjectIdMap = new HashMap<>();

        DatabaseReference subjectsRef = FirebaseDatabase.getInstance().getReference("Subjects");
        subjectsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    String name = data.child("name").getValue(String.class);
                    String id = data.getKey();
                    subjectNames.add(name);
                    subjectIdMap.put(name, id);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(ManageDocumentsActivity.this, android.R.layout.simple_dropdown_item_1line, subjectNames);
                autoSubject.setAdapter(adapter);
                autoSubject.setThreshold(1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ManageDocumentsActivity.this, "Lỗi tải môn học", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setTitle("Thêm tài liệu mới")
                .setPositiveButton("Thêm", (dialog, which) -> {
                    String subject = autoSubject.getText().toString().trim();
                    String name = etDocumentName.getText().toString().trim();
                    String type = etType.getText().toString().trim();
                    String subjectId = subjectIdMap.get(subject);


                    if (subject.isEmpty() || name.isEmpty() || type.isEmpty()) {
                        Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String id = documentsRef.push().getKey(); // tạo ID tự động
                    Document document = new Document(id,subjectId, subject, name, type);
                    documentsRef.child(id).setValue(document)
                            .addOnSuccessListener(aVoid -> Toast.makeText(this, "Đã thêm", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(this, "Thêm thất bại", Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void loadDocuments() {
        documentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                documentList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Document doc = snap.getValue(Document.class);
                    documentList.add(doc);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ManageDocumentsActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }


}

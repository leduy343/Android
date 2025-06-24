package com.example.tlunavigator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tlunavigator.adapter.DocumentAdapter;
import com.example.tlunavigator.model.Document;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserAllDocumentsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DocumentAdapter adapter;
    private List<Document> documentList;
    private DatabaseReference dbRef;

    private String subjectid = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_all_documents);

        recyclerView = findViewById(R.id.recyclerAllDocuments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        documentList = new ArrayList<>();

        SharedPreferences prefs = getSharedPreferences("UserRole", MODE_PRIVATE);
        String role = prefs.getString("role", "user");
        boolean isAdmin = "admin".equals(role);

        adapter = new DocumentAdapter(this, documentList, isAdmin);
        recyclerView.setAdapter(adapter);

        subjectid = getIntent().getStringExtra("subjectid");

        dbRef = FirebaseDatabase.getInstance().getReference("Documents");
        View();
        loadFilteredDocuments();
    }

    private void View() {
        DatabaseReference viewRef = FirebaseDatabase.getInstance()
                .getReference("Subjects")
                .child(subjectid)
                .child("views");
        viewRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                Integer integer = currentData.getValue(Integer.class);
                if(integer == null){
                    currentData.setValue(1);
                }else {
                    currentData.setValue(integer+1);
                }
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {

            }
        });
    }

    private void loadFilteredDocuments() {
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                documentList.clear();
                for (DataSnapshot docSnap : snapshot.getChildren()) {
                    Document doc = docSnap.getValue(Document.class);
                    if (doc != null) {
                        if (subjectid == null || subjectid.equalsIgnoreCase(doc.subjectId)){
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

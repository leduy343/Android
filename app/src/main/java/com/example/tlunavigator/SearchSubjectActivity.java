package com.example.tlunavigator;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tlunavigator.adapter.UsersubjectAdapter;
import com.example.tlunavigator.model.Subject;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class SearchSubjectActivity extends AppCompatActivity {
    private EditText edtSearch;
    private RecyclerView recyclerView;
    private Button btnHome;
    private List<Subject> subjectList;
    private List<Subject> subjects;
    private UsersubjectAdapter adapter;
    private DatabaseReference subjectsRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_subject);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtSearch = findViewById(R.id.edtSearch);
        recyclerView = findViewById(R.id.recyclerSubjects);
        btnHome = findViewById(R.id.btnHome);

        subjectList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UsersubjectAdapter(SearchSubjectActivity.this,subjectList);
        recyclerView.setAdapter(adapter);
        subjects = new ArrayList<>();
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                subjectList.clear();
                String key = removeDiacritics(s.toString());
                if(key.isEmpty()){
                    subjectList.addAll(subjects);
                }else {
                    for(Subject item : subjects){
                        String name = removeDiacritics(item.name);
                        if(name.toLowerCase().contains(key.toLowerCase())){
                            subjectList.add(item);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        subjectsRef = FirebaseDatabase.getInstance().getReference("Subjects");
        loadSubjects();

        btnHome.setOnClickListener(v -> finish() );
    }
    public static String removeDiacritics(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", ""); // Loại bỏ dấu (ký tự kết hợp)
    }
    private void loadSubjects() {
        subjectsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subjectList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Subject sub = snap.getValue(Subject.class);
                    if (sub != null && sub.name != null) {  // Kiểm tra tránh null
                        subjectList.add(sub);
                        subjects.add(sub);

                    }
                }
               adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchSubjectActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
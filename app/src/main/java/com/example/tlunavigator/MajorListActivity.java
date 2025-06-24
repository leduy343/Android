package com.example.tlunavigator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tlunavigator.adapter.MajorAdapter;
import com.example.tlunavigator.model.Major;
import com.example.tlunavigator.model.TrainingProgram;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MajorListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Major> majorList = new ArrayList<>();
    private MajorAdapter adapter;
    private DatabaseReference ProgramRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_major_list);

        recyclerView = findViewById(R.id.recyclerMajors);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Danh sách ngành cố định
//        majorList.add(new Major("7480201", "Công nghệ thông tin"));
//        majorList.add(new Major("7340101", "Quản trị kinh doanh"));
//        majorList.add(new Major("7580201", "Kỹ thuật xây dựng"));
//        majorList.add(new Major("7520216", "Kỹ thuật cấp thoát nước"));
//        majorList.add(new Major("7580210", "Kỹ thuật cơ sở hạ tầng"));
//        majorList.add(new Major("7520320", "Kỹ thuật môi trường"));

        adapter = new MajorAdapter(majorList, major -> {
            Intent intent = new Intent(MajorListActivity.this, CourseListActivity.class);
            intent.putExtra("majorCode", major.getId());
            startActivity(intent);
        });

        ProgramRef = FirebaseDatabase.getInstance().getReference("Program");
        recyclerView.setAdapter(adapter);
        loadProgram();
    }

    private void loadProgram(){

        ProgramRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                majorList.clear();
                for(DataSnapshot snap : snapshot.getChildren()){
                    Major TP = snap.getValue(Major.class);
                    majorList.add(TP);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MajorListActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

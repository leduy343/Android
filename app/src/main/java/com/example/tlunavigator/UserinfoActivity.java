package com.example.tlunavigator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserinfoActivity extends AppCompatActivity {

    TextView  tvemail,tvname,tvbitrhday,tvStudentCode, tvClass, txtNganh, tvGetInfo;
    String uid;
    DatabaseReference userRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_userinfo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tvname = findViewById(R.id.tvname);
        tvemail = findViewById(R.id.tvemail);
        tvbitrhday = findViewById(R.id.tvBirthday);
        tvStudentCode = findViewById(R.id.tvStudentCode);
        tvClass = findViewById(R.id.tvClass);
        txtNganh = findViewById(R.id.txtnganh);
        tvGetInfo = findViewById(R.id.tvGetinfo);

        tvGetInfo.setOnClickListener(v ->{
            Intent intent = new Intent(UserinfoActivity.this, GetstudentinfoActivity.class);
            startActivity(intent);
        });
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String birthday = snapshot.child("birthday").getValue(String.class);
                    String studentId = snapshot.child("studentId").getValue(String.class);
                    String className = snapshot.child("className").getValue(String.class);
                    String major = snapshot.child("major").getValue(String.class); // nếu có
                    tvname.setText(name);
                    tvemail.setText(email);
                    tvbitrhday.setText(birthday != null? birthday : "Chưa cập nhật");
                    tvStudentCode.setText(studentId != null ? studentId : "Chưa cập nhật");
                    tvClass.setText(className != null ? className : "Chưa cập nhật");
                    txtNganh.setText(major != null ? major : "Chưa cập nhật");
                } else {
                    Toast.makeText(UserinfoActivity.this, "Không tìm thấy thông tin", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserinfoActivity.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
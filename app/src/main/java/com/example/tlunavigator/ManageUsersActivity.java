package com.example.tlunavigator;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tlunavigator.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageUsersActivity extends AppCompatActivity {
    private ListView listViewUsers;
    private ArrayAdapter<String> adapter;
    private List<String> userList = new ArrayList<>();
    private DatabaseReference usersRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_users);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        listViewUsers = findViewById(R.id.listViewUsers);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);
        listViewUsers.setAdapter(adapter);

        usersRef = FirebaseDatabase.getInstance().getReference("users");

        checkIsAdminAndLoadUsers();
    }

    private void checkIsAdminAndLoadUsers() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Chưa đăng nhập", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String uid = currentUser.getUid();
        usersRef.child(uid).child("role").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String role = snapshot.getValue(String.class);
                if ("admin".equals(role)) {
                    loadUsers();
                } else {
                    Toast.makeText(ManageUsersActivity.this, "Bạn không phải admin", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ManageUsersActivity.this, "Lỗi truy cập DB", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUsers() {
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot userSnap : snapshot.getChildren()) {
                    User user = userSnap.getValue(User.class);
                    String info = user.name + "\n" + user.email + "\nRole: " + user.role;
                    userList.add(info);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ManageUsersActivity.this, "Không thể tải danh sách", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
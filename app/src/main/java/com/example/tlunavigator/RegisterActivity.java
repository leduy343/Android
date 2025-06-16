package com.example.tlunavigator;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText etEmail, etPassword, etConfirm,etName;
    private Button btnRegister;
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirm = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);

        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("Users");

        btnRegister.setOnClickListener(v -> registerUser());
    }
    private void registerUser() {
        String name = etName.getText().toString();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirm = etConfirm.getText().toString().trim();

        if (name.isEmpty()||email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            Toast.makeText(this, "Điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirm)) {
            Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid();

                        // Tạo user object
                        Map<String, String> user = new HashMap<>();
                        user.put("name",name);
                        user.put("email", email);
                        user.put("role", "user");

                        dbRef.child(uid).setValue(user)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                    finish(); // quay lại login
                                })
                                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi lưu DB", Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(this, "Lỗi: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
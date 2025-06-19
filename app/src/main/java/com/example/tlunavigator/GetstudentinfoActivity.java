package com.example.tlunavigator;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GetstudentinfoActivity extends AppCompatActivity {


    Button btnLogin;
    EditText edtUsername, edtPassword;
    OkHttpClient client = new OkHttpClient.Builder()
            .hostnameVerifier((hostname, session) -> true)
            .sslSocketFactory(getUnsafeSslSocketFactory(), getTrustAllCertsManager())
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_getstudentinfo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnLogin = findViewById(R.id.btnLogin);
        edtUsername = findViewById(R.id.etAccount);
        edtPassword = findViewById(R.id.edtPassword);

        btnLogin.setOnClickListener(v -> {
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();


            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            // Gửi yêu cầu đăng nhập
            JSONObject json = new JSONObject();
            try {
                json.put("username", username);
                json.put("password", password);
                json.put("client_id", "education_client");
                json.put("grant_type", "password");
                json.put("client_secret", "password");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestBody body = new FormBody.Builder()
                    .add("username", username)
                    .add("password", password)
                    .add("client_id", "education_client")
                    .add("grant_type", "password")
                    .add("client_secret", "password")
                    .build();

            Request request = new Request.Builder()
                    .url("https://sinhvien1.tlu.edu.vn/education/oauth/token")
                    .addHeader("Content-Type", "application/json")  // THÊM DÒNG NÀY
                    .post(body)
                    .build();


            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    runOnUiThread(() ->
                            Toast.makeText(GetstudentinfoActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try {
                            String responseBody = response.body().string();
                            JSONObject resJson = new JSONObject(responseBody);
                            String accessToken = resJson.getString("access_token");

                            runOnUiThread(() ->
                                    Toast.makeText(GetstudentinfoActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show());

                            // Gọi hàm lấy thông tin sinh viên
                            getStudentInfo(accessToken);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        runOnUiThread(() ->
                                Toast.makeText(GetstudentinfoActivity.this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show());
                    }
                }
            });
        });
    }

    // TODO: Viết tiếp hàm lấy thông tin sinh viên
    private void getStudentInfo(String accessToken) {
        Request request = new Request.Builder()
                .url("https://sinhvien1.tlu.edu.vn/education/api/studentsubjectmark/getListMarkDetailStudent\n")
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                Log.d("API_RESPONSE", responseBody);
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(responseBody);

                        String name = "";
                        String studentId = "";
                        String className = "";
                        ArrayList<String> marks = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject item = jsonArray.getJSONObject(i);
                            JSONObject student = item.getJSONObject("student");

                            // Chỉ lấy một lần
                            if (i == 0) {
                                name = student.getString("displayName");
                                studentId = student.getString("studentCode");
                                className =  student.getString("className");
                            }

                            JSONObject subject = item.getJSONObject("subject");
                            String subjectName = subject.getString("subjectName");
                            String subjectCode = subject.getString("subjectCode");

                            JSONObject semester = item.getJSONObject("semester");
                            String semesterName = semester.getString("semesterName");

                            double mark = item.has("mark") && !item.isNull("mark") ? item.getDouble("mark") : -1;

                            String markText = subjectName + " (" + subjectCode + ") - " + semesterName + ": " + (mark == -1 ? "Chưa có điểm" : mark);
                            marks.add(markText);
                        }

                        String finalName = name;
                        String finalStudentId = studentId;
                        String finalClassName = className;

                        DatabaseReference userRef = FirebaseDatabase.getInstance()
                                .getReference("Users")
                                .child(finalStudentId); // dùng studentId làm key

                        Map<String, Object> userData = new HashMap<>();
                        userData.put("studentId", finalStudentId);
                        userData.put("className", finalClassName);
                        userData.put("marks", marks);

                        userRef.setValue(userData)
                                .addOnSuccessListener(aVoid -> Log.d("FIREBASE", "Lưu thành công"))
                                .addOnFailureListener(e -> Log.e("FIREBASE", "Lỗi lưu: " + e.getMessage()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(GetstudentinfoActivity.this, "Không lấy được thông tin sinh viên", Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(GetstudentinfoActivity.this, "Lỗi kết nối khi lấy thông tin sinh viên", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private SSLSocketFactory getUnsafeSslSocketFactory() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[]{};
                        }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private X509TrustManager getTrustAllCertsManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }
        };
    }
}

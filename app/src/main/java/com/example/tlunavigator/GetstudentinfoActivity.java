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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    String finalStudentId ;
    String finalClassName ;
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

    private void getStudentInfo(String accessToken) {
        Request request = new Request.Builder()
                .url("https://sinhvien1.tlu.edu.vn/education/api/studentsubjectmark/findByStudentAndSubjectByLoginUser/113/0/1/1000")
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                Log.d("API_RESPONSE", responseBody);
                if (response.isSuccessful()) {
                    try {
                        JSONObject json = new JSONObject(responseBody);
                        JSONArray jsonArray = json.getJSONArray("content");
                        String name = "";
                        String studentId = "";
                        ArrayList<String> marks = new ArrayList<>();
                        ArrayList<String> passedSubjectCodes = new ArrayList<>();
                        marks.clear();
                        passedSubjectCodes.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject item = jsonArray.getJSONObject(i);
                            JSONObject student = item.getJSONObject("student");

                            if (i == 0) {
                                name = student.getString("displayName");
                                studentId = student.getString("studentCode");
                            }

                            JSONObject subject = item.getJSONObject("subject");
                            String subjectName = subject.getString("subjectName");
                            String subjectCode = subject.getString("subjectCode");

                            passedSubjectCodes.add(subjectCode);


                            String mark = item.has("charMark") && !item.isNull("charMark") ? item.getString("charMark") : null;

                            String markText = subjectName + " (" + subjectCode + ") - " + ": " + (mark == null ? "Chưa có điểm" : mark);
                            marks.add(markText);
                        }

                        String finalName = name;
                        finalStudentId = studentId;

                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference userRef = FirebaseDatabase.getInstance()
                                .getReference("Users")
                                .child(uid);

                        Map<String, Object> userData = new HashMap<>();
                        userData.put("studentId", finalStudentId);
                        userData.put("name", finalName);
                        userData.put("marks", marks);

                        userRef.updateChildren(userData)
                                .addOnSuccessListener(aVoid -> Log.d("FIREBASE", "Lưu thành công"))
                                .addOnFailureListener(e -> Log.e("FIREBASE", "Lỗi lưu: " + e.getMessage()));
                        Getprogram(accessToken,passedSubjectCodes);
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
    private void Getprogram(String accessToken, ArrayList<String> passedSubjectCodes) {
        Request request = new Request.Builder()
                .url("https://sinhvien1.tlu.edu.vn/education/api/student/getstudentbylogin")
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                Log.d("API_RESPONSE", responseBody);

                if (response.isSuccessful()) {
                    try {
                        JSONObject json = new JSONObject(responseBody);

                        String className = "";
                        if (json.has("enrollmentClass") && !json.isNull("enrollmentClass")) {
                            JSONObject enrollmentClass = json.getJSONObject("enrollmentClass");
                            className = enrollmentClass.optString("className", "");
                        }

                        String specialityName = "";
                        JSONArray programsArray = json.getJSONArray("programs");

                        if (programsArray.length() > 0) {
                            JSONObject programWrapper = programsArray.getJSONObject(0);
                            JSONObject program = programWrapper.getJSONObject("program");

                            if (program.has("speciality") && !program.isNull("speciality")) {
                                JSONObject speciality = program.getJSONObject("speciality");
                                specialityName = speciality.optString("name", "");
                            }

                            // So sánh môn học
                            JSONArray subjectsArray = program.getJSONArray("subjects");
                            ArrayList<String> uncompletedSubjects = new ArrayList<>();
                            uncompletedSubjects.clear();

                            for (int i = 0; i < subjectsArray.length(); i++) {
                                JSONObject subjectItem = subjectsArray.getJSONObject(i);
                                JSONObject subject = subjectItem.getJSONObject("subject");

                                String subjectCode = subject.optString("subjectCode", "");
                                String subjectName = subject.optString("subjectName", "");

                                if (!passedSubjectCodes.contains(subjectCode)) {
                                    String uncompleted = subjectCode + " - " + subjectName;
                                    uncompletedSubjects.add(uncompleted);
                                }
                            }
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DatabaseReference userRef = FirebaseDatabase.getInstance()
                                    .getReference("Users")
                                    .child(uid);

                            Map<String, Object> userData = new HashMap<>();
                            userData.put("className",className);
                            userData.put("speciality",specialityName);
                            userData.put("uncompletedSubjects", uncompletedSubjects);

                            userRef.updateChildren(userData)
                                    .addOnSuccessListener(aVoid -> Log.d("FIREBASE", "Lưu thành công"))
                                    .addOnFailureListener(e -> Log.e("FIREBASE", "Lỗi lưu: " + e.getMessage()));

                            getStudentSummaryMark(accessToken);
                        } else {
                            Log.e("PROGRAMS", "Không có chương trình đào tạo nào.");
                        }

                    } catch (JSONException e) {
                        Log.e("JSON_ERROR", "Lỗi phân tích JSON: " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    Log.e("GET_PROGRAM_ERROR", "Không lấy được thông tin chương trình đào tạo");
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e("CONNECTION_ERROR", "Lỗi kết nối khi gọi API chương trình đào tạo");
            }
        });
    }

    private void getStudentSummaryMark(String accessToken) {
        Request request = new Request.Builder()
                .url("https://sinhvien1.tlu.edu.vn/education/api/studentsummarymark/getbystudent")
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                Log.d("SUMMARY_MARK_RESPONSE", responseBody);

                if (response.isSuccessful()) {
                    try {
                        JSONObject json = new JSONObject(responseBody);

                        double avgMark4 = json.getDouble("mark4");
                        int totalCreditsPassed = json.getInt("firstTotalCredit"); // ví dụ: 118

                        Map<String, Object> summary = new HashMap<>();
                        summary.put("avgMark4", avgMark4);
                        summary.put("totalCreditsPassed", totalCreditsPassed);

                        List<Map<String, Object>> schoolYears = new ArrayList<>();
                        JSONArray schoolYearSummaryMarks = json.getJSONArray("schoolYearSummaryMarks");
                        for (int i = 0; i < schoolYearSummaryMarks.length(); i++) {
                            JSONObject yearMark = schoolYearSummaryMarks.getJSONObject(i);

                            String yearName = yearMark.getJSONObject("schoolYear").getString("name");
                            double yearAvg10 = yearMark.getDouble("mark");
                            double yearAvg4 = yearMark.getDouble("mark4");
                            Log.d("YEAR_AVG", yearName + ": " + yearAvg10 + " (10), " + yearAvg4 + " (4)");

                            JSONArray semesterMarks = yearMark.getJSONArray("semesterMarks");
                            List<Map<String, Object>> semesters = new ArrayList<>();
                            for (int j = 0; j < semesterMarks.length(); j++) {
                                JSONObject sem = semesterMarks.getJSONObject(j);
                               // String semName = sem.getJSONObject("semester").optString("name", "");
                                double semMark10 = sem.getDouble("mark");
                                double semMark4 = sem.getDouble("mark4");
                                int totalCredit = sem.getInt("totalCredit");
                                int learnedCredit = sem.getInt("learningTotalCredit");

                                Map<String, Object> semData = new HashMap<>();
                                //semData.put("name", semName);
                                semData.put("avg10", semMark10);
                                semData.put("avg4", semMark4);
                                semData.put("creditsPassed", totalCredit);
                                semData.put("creditsStudied", learnedCredit);

                                semesters.add(semData);

                            }
                            Map<String, Object> yearData = new HashMap<>();
                            yearData.put("year", yearName);
                            yearData.put("avg10", yearAvg10);
                            yearData.put("avg4", yearAvg4);
                            yearData.put("semesters", semesters);

                            schoolYears.add(yearData);
                        }
                        summary.put("schoolYears", schoolYears);
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference userRef = FirebaseDatabase.getInstance()
                                .getReference("Users")
                                .child(uid);
                        userRef.updateChildren(summary)
                                .addOnSuccessListener(aVoid -> Log.d("FIREBASE", "Lưu thành công"))
                                .addOnFailureListener(e -> Log.e("FIREBASE", "Lỗi lưu: " + e.getMessage()));


                        finish();
                    } catch (JSONException e) {
                        Log.e("JSON_PARSE", "Lỗi JSON: " + e.getMessage());
                    }
                } else {
                    Log.e("API_ERROR", "Không lấy được dữ liệu điểm trung bình");
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e("API_FAIL", "Lỗi kết nối khi lấy điểm trung bình");
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

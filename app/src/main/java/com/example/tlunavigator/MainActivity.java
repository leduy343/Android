package com.example.tlunavigator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    LinearLayout itemChuongTrinh, itemMonHoc, itemNguoiDung, itemTaiNguyen, itemThongKe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ các item trong layout
        itemChuongTrinh = findViewById(R.id.item_chuong_trinh);
        itemMonHoc = findViewById(R.id.item_mon_hoc);
        itemNguoiDung = findViewById(R.id.item_nguoi_dung);
        itemTaiNguyen = findViewById(R.id.item_tai_nguyen);
        itemThongKe = findViewById(R.id.item_thong_ke);

        // Gắn sự kiện click chuyển trang
        itemChuongTrinh.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ChuongTrinhActivity.class)));
        itemMonHoc.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MonHocActivity.class)));
        itemNguoiDung.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, NguoiDungActivity.class)));
        itemTaiNguyen.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, TaiNguyenActivity.class)));
        itemThongKe.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ThongKeActivity.class)));
    }
}

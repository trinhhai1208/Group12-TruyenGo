package com.example.truyengo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AccountActivity extends AppCompatActivity {
    private TextView tvUsername, btnDeleteAccount, btnLogout;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mapping();

        // Gán tên người dùng (có thể load từ SharedPreferences)
        tvUsername.setText("Viết Doanh");

        // Xử lý nút “Xóa tài khoản”
        btnDeleteAccount.setOnClickListener(v -> {
            Toast.makeText(this, "Xóa tài khoản", Toast.LENGTH_SHORT).show();
            // Thực hiện API hoặc xác nhận xóa
        });

        // Xử lý nút “Đăng xuất”
        btnLogout.setOnClickListener(v -> {
            Toast.makeText(this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
            // Ví dụ: quay lại màn hình đăng nhập
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        // Thanh điều hướng dưới
//        bottomNavigationView.setSelectedItemId(R.id.menu_account);
//        bottomNavigationView.setOnItemSelectedListener(item -> {
//            int id = item.getItemId();
//            if (id == R.id.menu_home) {
//                startActivity(new Intent(this, HomeActivity.class));
//                overridePendingTransition(0, 0);
//                return true;
//            } else if (id == R.id.menu_favorite) {
//                startActivity(new Intent(this, FavoriteActivity.class));
//                overridePendingTransition(0, 0);
//                return true;
//            } else if (id == R.id.menu_history) {
//                startActivity(new Intent(this, HistoryActivity.class));
//                overridePendingTransition(0, 0);
//                return true;
//            } else if (id == R.id.menu_chatbot) {
//                startActivity(new Intent(this, ChatbotActivity.class));
//                overridePendingTransition(0, 0);
//                return true;
//            }
//            return true;
//        });

    }

    public void mapping(){
        tvUsername = findViewById(R.id.tvUsername);
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount);
        btnLogout = findViewById(R.id.btnLogout);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
    }
}
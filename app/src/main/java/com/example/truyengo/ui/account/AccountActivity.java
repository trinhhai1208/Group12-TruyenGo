package com.example.truyengo.ui.account;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.truyengo.ui.info.AppInfoActivity;
import com.example.truyengo.R;
import com.example.truyengo.ui.auth.ForgotPasswordActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AccountActivity extends AppCompatActivity {
    private TextView tvUsername, btnDeleteAccount, btnLogout;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.account_account_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mapping();

        // Gán tên người dùng (có thể load từ SharedPreferences)
        tvUsername.setText("Viết Doanh");


        btnLogout = findViewById(R.id.btnLogout);
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount);

        // Sự kiện nút "Đăng xuất"
        btnLogout.setOnClickListener(v -> showLogoutDialog());

        // Sự kiện nút "Xóa tài khoản"
        btnDeleteAccount.setOnClickListener(v -> showDeleteAccountDialog());


        findViewById(R.id.itemAccountManagement).setOnClickListener(v ->
                startActivity(new Intent(this, AccountManagementActivity.class)));

        findViewById(R.id.itemChangePassword).setOnClickListener(v ->
                startActivity(new Intent(this, ChangePasswordActivity.class)));

        findViewById(R.id.itemForgotPassword).setOnClickListener(v ->
                startActivity(new Intent(this, ForgotPasswordActivity.class)));

        findViewById(R.id.itemAboutApp).setOnClickListener(v ->
                startActivity(new Intent(this, AppInfoActivity.class)));


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

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.common_dialog_logout_activity, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        TextView btnCancel = view.findViewById(R.id.btnCancelLogout);
        TextView btnConfirm = view.findViewById(R.id.btnConfirmLogout);

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnConfirm.setOnClickListener(v -> {
            dialog.dismiss();
            // TODO: Xử lý đăng xuất (xóa token, chuyển màn hình Login, ...)
        });

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    private void showDeleteAccountDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.common_dialog_delete_account_activity, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        TextView btnCancel = view.findViewById(R.id.btnCancelDelete);
        TextView btnConfirm = view.findViewById(R.id.btnConfirmDelete);

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnConfirm.setOnClickListener(v -> {
            dialog.dismiss();
            // TODO: Xử lý xóa tài khoản (gọi API xóa, xóa local data, ...)
        });

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }
}
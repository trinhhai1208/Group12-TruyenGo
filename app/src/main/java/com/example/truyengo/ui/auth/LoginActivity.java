package com.example.truyengo.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.truyengo.R;
import com.example.truyengo.dto.LoginRequest;
import com.example.truyengo.dto.LoginResponse;
import com.example.truyengo.ui.main.MainActivity;
import com.example.truyengo.utils.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    // 1. Khai báo biến giao diện
    private EditText etUsername, etPassword;
    private TextView btnLogin, tvRegisterNow, tvForgotPassword;
    private CheckBox cbRemember;
    private TextView tvMessage;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_login_activity);

        initViews();
        setListeners();
    }

    private void initViews() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegisterNow = findViewById(R.id.tvRegisterNow);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        cbRemember = findViewById(R.id.cbRemember);
        tvMessage = findViewById(R.id.tvMessage);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setListeners() {
        btnLogin.setOnClickListener(v -> handleLoginApi());

        tvRegisterNow.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        tvForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }

    private void handleLoginApi() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty()) {
            showMessage("Xin mời nhập tên tài khoản");
            return;
        }
        if (password.isEmpty()) {
            showMessage("Xin mời nhập mật khẩu");
            return;
        }

        // 2. Hiển thị Loading
        setLoadingState(true);
        if (tvMessage != null) tvMessage.setText("");

        // 3. Gọi API thông qua Retrofit
        LoginRequest request = new LoginRequest(username, password);

        ApiClient.getApiService().login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                setLoadingState(false);

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();

                    if (loginResponse.getData().getStatus().equals("OK")) {
                        if (cbRemember.isChecked()) {
                            saveLoginInfoLocal(username, password);
                        }

                        navigateToHome();

                    } else {
                        showMessage(loginResponse.getData().getMessage());
                    }
                } else {
                    showMessage("Lỗi hệ thống: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                setLoadingState(false);
                showMessage("Lỗi kết nối mạng: " + t.getMessage());
            }
        });
    }


    private void setLoadingState(boolean isLoading) {
        if (isLoading) {
            if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
            btnLogin.setEnabled(false);
            etUsername.setEnabled(false);
            etPassword.setEnabled(false);
        } else {
            if (progressBar != null) progressBar.setVisibility(View.GONE);
            btnLogin.setEnabled(true);
            etUsername.setEnabled(true);
            etPassword.setEnabled(true);
        }
    }

    private void showMessage(String msg) {
        if (tvMessage != null) {
            tvMessage.setText(msg);
        } else {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToHome() {
        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void saveLoginInfoLocal(String u, String p) {
        // Code lưu SharedPreferences ở đây (nếu cần)
        // ...
    }
}
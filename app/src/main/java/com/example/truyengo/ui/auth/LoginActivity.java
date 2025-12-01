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
import com.example.truyengo.dto.request.auth.AuthData;
import com.example.truyengo.dto.request.auth.LoginRequestDto;
import com.example.truyengo.dto.response.BaseResponse;
import com.example.truyengo.dto.response.LoginResponseDto;
import com.example.truyengo.ui.main.MainActivity;
import com.example.truyengo.utils.ApiClient;
import com.example.truyengo.utils.TokenManager;

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
        btnLogin.setOnClickListener(v -> handleLogin());

        tvRegisterNow.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        tvForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }

    private void handleLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        LoginRequestDto request = new LoginRequestDto(username, password);

        ApiClient.getApiService().login(request).enqueue(new Callback<BaseResponse<AuthData>>() {
            @Override
            public void onResponse(Call<BaseResponse<AuthData>> call, Response<BaseResponse<AuthData>> response) {
                // Check HTTP Code 200
                if (response.isSuccessful() && response.body() != null) {
                    BaseResponse<AuthData> body = response.body();

                    // Check "status": "SUCCESS" trong JSON body
                    if (body.isSuccess() && body.getData() != null) {
                        AuthData data = body.getData();

                        // --- QUAN TRỌNG: LƯU TOKEN & USER ID ---
                        TokenManager tokenManager = new TokenManager(LoginActivity.this);
                        tokenManager.saveAuthInfo(
                                data.getAccessToken(),
                                data.getRefreshToken(),
                                data.getId() // userId lấy từ trường "id"
                        );

                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                        // Chuyển màn hình
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        // Trường hợp server trả về 200 nhưng status != SUCCESS (nếu có)
                        // Hoặc lấy message từ bên trong data
                        String msg = (body.getData() != null) ? body.getData().getMessage() : "Đăng nhập thất bại";
                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Lỗi 400, 401, 500...
                    Toast.makeText(LoginActivity.this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<AuthData>> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
package com.example.truyengo.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.truyengo.R;
import com.example.truyengo.dto.request.auth.RegisterRequestDto;
import com.example.truyengo.dto.response.BaseResponse;
import com.example.truyengo.utils.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    // Khai báo biến giao diện
    private ImageButton btnBack;
    private EditText etUsername, etPassword, etConfirmPassword, etName, etEmail;
    private CheckBox cbTerms;
    private TextView btnRegister, tvLoginNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_register_activity);

        initViews();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        cbTerms = findViewById(R.id.cbTerms);
        btnRegister = findViewById(R.id.btnRegister);
        tvLoginNow = findViewById(R.id.tvLoginNow);
    }

    private void setupListeners() {
        // Nút Back
        btnBack.setOnClickListener(v -> finish());

        // Chuyển sang đăng nhập
        tvLoginNow.setOnClickListener(v -> finish());

        // Nút Đăng ký
        btnRegister.setOnClickListener(v -> handleRegister());
    }

    private void handleRegister() {
        // 1. Lấy dữ liệu
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String fullName = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        // 2. Validate dữ liệu
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || fullName.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!cbTerms.isChecked()) {
            Toast.makeText(this, "Bạn cần đồng ý với điều khoản sử dụng!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. Xử lý tách Họ và Tên (API yêu cầu firstName, lastName)
        // Logic: Lấy từ cuối cùng làm Tên (LastName), phần còn lại là Họ đệm (FirstName)
        String firstName = "";
        String lastName = "";

        if (fullName.contains(" ")) {
            int lastSpaceIndex = fullName.lastIndexOf(" ");
            firstName = fullName.substring(0, lastSpaceIndex);
            lastName = fullName.substring(lastSpaceIndex + 1);
        } else {
            // Nếu chỉ nhập 1 từ
            firstName = fullName;
            lastName = ""; // Hoặc để lastName = fullName tùy quy ước backend
        }

        // 4. Tạo Request DTO
        RegisterRequestDto requestDto = new RegisterRequestDto(
                username,
                password,
                firstName,
                lastName,
                email
        );

        // 5. Gọi API
        callRegisterApi(requestDto, email);
    }

    private void callRegisterApi(RegisterRequestDto request, String email) {
        // Khóa nút để tránh bấm nhiều lần
        btnRegister.setEnabled(false);
        btnRegister.setText("Đang xử lý...");

        ApiClient.getApiService().register(request).enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                btnRegister.setEnabled(true);
                btnRegister.setText("Đăng ký ngay");

                if (response.isSuccessful() && response.body() != null) {
                    BaseResponse<String> body = response.body();
                    if (body.isSuccess()) {
                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công! Vui lòng kiểm tra email.", Toast.LENGTH_LONG).show();

                        // Chuyển sang màn hình xác thực OTP
                        Intent intent = new Intent(RegisterActivity.this, VerifyOTPActivity.class);
                        intent.putExtra("EMAIL", email);
                        startActivity(intent);
                        finish();
                    } else {
                        String msg = body.getData() != null ? body.getData() : "Đăng ký thất bại";
                        Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Lỗi server: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                btnRegister.setEnabled(true);
                btnRegister.setText("Đăng ký ngay");
                Toast.makeText(RegisterActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
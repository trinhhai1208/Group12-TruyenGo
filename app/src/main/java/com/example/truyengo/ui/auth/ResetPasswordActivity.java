package com.example.truyengo.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.truyengo.R;
import com.example.truyengo.dto.request.auth.ResetPasswordRequestDto;
import com.example.truyengo.dto.response.BaseResponse;
import com.example.truyengo.dto.response.UserResponseDto;
import com.example.truyengo.utils.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText edtNewPassword, edtRePassword;
    private TextView btnConfirm;
    private ImageButton btnBack;

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_reset_password_activity);

        initViews();

        // Nhận email từ màn hình VerifyOtpActivity
        if (getIntent() != null) {
            email = getIntent().getStringExtra("EMAIL");
        }

        // Kiểm tra nếu không có email thì báo lỗi và đóng màn hình
        if (email == null) {
            Toast.makeText(this, "Lỗi: Không tìm thấy thông tin email", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtRePassword = findViewById(R.id.edtConfirmPassword);
        btnConfirm = findViewById(R.id.btnChangePassword);
    }

    private void setupListeners() {
        // Sự kiện nút Back
        btnBack.setOnClickListener(v -> finish());

        // Sự kiện nút Xác nhận đổi mật khẩu
        btnConfirm.setOnClickListener(v -> handleResetPassword());
    }

    private void handleResetPassword() {
        String newPass = edtNewPassword.getText().toString().trim();
        String rePass = edtRePassword.getText().toString().trim();

        // 1. Kiểm tra dữ liệu đầu vào (Validate)
        if (newPass.isEmpty() || rePass.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPass.equals(rePass)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newPass.length() < 6) {
            Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }

        // Khóa nút để tránh bấm nhiều lần
        btnConfirm.setEnabled(false);
        btnConfirm.setText("Đang xử lý...");

        // 2. Tạo Request gửi lên Server
        ResetPasswordRequestDto request = new ResetPasswordRequestDto(email, newPass, rePass);

        // 3. Gọi API Reset Password
        ApiClient.getApiService().resetPassword(request).enqueue(new Callback<BaseResponse<UserResponseDto>>() {
            @Override
            public void onResponse(Call<BaseResponse<UserResponseDto>> call, Response<BaseResponse<UserResponseDto>> response) {
                // Mở khóa nút
                btnConfirm.setEnabled(true);
                btnConfirm.setText("Xác nhận");

                if (response.isSuccessful() && response.body() != null) {
                    BaseResponse<UserResponseDto> body = response.body();

                    if (body.isSuccess()) {
                        // Thành công -> Thông báo và chuyển về màn hình Đăng nhập
                        Toast.makeText(ResetPasswordActivity.this, "Đặt lại mật khẩu thành công!", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                        // Xóa lịch sử activity để người dùng không back lại được màn hình này
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        // Thất bại do lỗi nghiệp vụ (Backend trả về message lỗi)
                        String msg = body.getData() != null ? body.getMessage() : "Thất bại";
                        Toast.makeText(ResetPasswordActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Thất bại do lỗi Server (404, 500...)
                    Toast.makeText(ResetPasswordActivity.this, "Lỗi server: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<UserResponseDto>> call, Throwable t) {
                // Thất bại do lỗi mạng
                btnConfirm.setEnabled(true);
                btnConfirm.setText("Xác nhận");
                Toast.makeText(ResetPasswordActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
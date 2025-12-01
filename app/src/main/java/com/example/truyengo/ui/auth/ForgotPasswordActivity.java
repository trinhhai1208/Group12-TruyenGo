package com.example.truyengo.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.truyengo.R;
import com.example.truyengo.dto.request.auth.ForgotPasswordRequestDto;
import com.example.truyengo.dto.response.BaseResponse;
import com.example.truyengo.utils.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private EditText edtEmail; // Lưu ý: Layout bạn gửi có cả edtUsername, nhưng API mẫu chỉ cần email
    private TextView btnGetCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_forgot_password_activity); // Đảm bảo tên layout đúng

        initViews();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        // Trong layout bạn gửi có edtUsername, nhưng API mẫu chỉ cần email.
        // Tôi sẽ focus vào edtEmail. Bạn có thể ẩn/hiện edtUsername tùy logic backend thực tế.
        edtEmail = findViewById(R.id.edtEmail);
        btnGetCode = findViewById(R.id.btnGetCode);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnGetCode.setOnClickListener(v -> handleForgotPassword());
    }

    private void handleForgotPassword() {
        String email = edtEmail.getText().toString().trim();

        // Validate Email
        if (email.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Khóa nút để tránh spam
        btnGetCode.setEnabled(false);
        btnGetCode.setText("Đang gửi...");

        // Tạo Request
        ForgotPasswordRequestDto request = new ForgotPasswordRequestDto(email);

        // Gọi API
        ApiClient.getApiService().forgotPassword(request).enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                btnGetCode.setEnabled(true);
                btnGetCode.setText("Lấy mã xác nhận");

                if (response.isSuccessful() && response.body() != null) {
                    BaseResponse<String> body = response.body();

                    if (body.isSuccess()) {
                        // Thành công: Backend trả về "OTP has sent..."
                        Toast.makeText(ForgotPasswordActivity.this, "Mã OTP đã được gửi đến email của bạn!", Toast.LENGTH_LONG).show();

                        // Chuyển sang màn hình VerifyOtpActivity để nhập mã
                        // Lưu ý: Có thể bạn cần dùng 1 cờ (flag) để VerifyOtpActivity biết
                        // đây là xác thực cho "Quên mật khẩu" chứ không phải "Đăng ký"
                        Intent intent = new Intent(ForgotPasswordActivity.this, VerifyOTPActivity.class);
                        intent.putExtra("EMAIL", email);
                        intent.putExtra("IS_FORGOT_PASSWORD", true); // Đánh dấu đây là luồng quên pass
                        startActivity(intent);
                        finish();
                    } else {
                        String msg = body.getData() != null ? body.getData() : "Yêu cầu thất bại";
                        Toast.makeText(ForgotPasswordActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Lỗi server: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                btnGetCode.setEnabled(true);
                btnGetCode.setText("Lấy mã xác nhận");
                Toast.makeText(ForgotPasswordActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
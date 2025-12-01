package com.example.truyengo.ui.account;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.truyengo.R;
import com.example.truyengo.dto.request.auth.ResetPasswordRequestDto;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private TextInputEditText edtOldPassword, edtNewPassword, edtConfirmPassword;
    private TextView btnChangePassword;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_change_password_activity);

        // 1. Ánh xạ View
        initViews();

        // 2. Xử lý sự kiện click
        btnBack.setOnClickListener(v -> finish()); // Quay lại màn hình trước

        btnChangePassword.setOnClickListener(v -> handleChangePassword());
    }

    private void initViews() {
        edtOldPassword = findViewById(R.id.edtOldPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnBack = findViewById(R.id.btnBack);
    }

    private void handleChangePassword() {
        String oldPass = edtOldPassword.getText().toString().trim();
        String newPass = edtNewPassword.getText().toString().trim();
        String confirmPass = edtConfirmPassword.getText().toString().trim();

        // --- VALIDATE DỮ LIỆU ---
        if (TextUtils.isEmpty(oldPass) || TextUtils.isEmpty(newPass) || TextUtils.isEmpty(confirmPass)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPass.equals(confirmPass)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newPass.length() < 6) { // Ví dụ: yêu cầu độ dài tối thiểu
            Toast.makeText(this, "Mật khẩu mới phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- GỌI API ---
        performChangePassword(oldPass, newPass, confirmPass);
    }

    private void performChangePassword(String oldPass, String newPass, String confirmPass) {
        // Tạo request body
        ResetPasswordRequestDto requestDto = new ResetPasswordRequestDto(oldPass, newPass, confirmPass);

        // Lấy instance của Retrofit (Bạn cần thay thế bằng cách gọi Singleton/Client của bạn)
        // Ví dụ: ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);

        // Giả sử bạn đã có biến apiService:
        /* apiService.resetPassword(requestDto).enqueue(new Callback<BaseResponse<UserResponseDto>>() {
            @Override
            public void onResponse(Call<BaseResponse<UserResponseDto>> call, Response<BaseResponse<UserResponseDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Kiểm tra status code từ server (nếu BaseResponse có trường status/code)
                    Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();

                    // Đổi xong thì finish activity hoặc yêu cầu đăng nhập lại tùy logic
                    finish();
                } else {
                    // Xử lý lỗi từ server (ví dụ: mật khẩu cũ sai)
                    Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu thất bại. Vui lòng kiểm tra lại mật khẩu cũ.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<UserResponseDto>> call, Throwable t) {
                Toast.makeText(ChangePasswordActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        */
    }
}
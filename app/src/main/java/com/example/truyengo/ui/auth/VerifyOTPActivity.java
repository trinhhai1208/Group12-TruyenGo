package com.example.truyengo.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.truyengo.R;
import com.example.truyengo.dto.request.auth.VerifyOtpRequestDto;
import com.example.truyengo.dto.response.BaseResponse;
import com.example.truyengo.dto.response.UserResponseDto;
import com.example.truyengo.utils.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyOTPActivity extends AppCompatActivity {

    private EditText otp1, otp2, otp3, otp4, otp5, otp6;
    private TextView tvEmail, textTimer, textResend, btnContinue;
    private ImageButton btnBack;

    private String email;
    private CountDownTimer countDownTimer;
    private boolean isResendEnabled = false;
    private boolean isForgotPassword = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_verify_otp_activity);

        initViews();
        setupOtpInputs();

        // Lấy email từ Intent
        if (getIntent() != null) {
            email = getIntent().getStringExtra("EMAIL");
            if (email != null) {
                tvEmail.setText(email);
            }
            // Kiểm tra xem có phải đang ở luồng Quên mật khẩu không
            isForgotPassword = getIntent().getBooleanExtra("IS_FORGOT_PASSWORD", false);
        }

        startTimer(60000); // 60 giây

        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        otp5 = findViewById(R.id.otp5);
        otp6 = findViewById(R.id.otp6);

        tvEmail = findViewById(R.id.tvEmail);
        textTimer = findViewById(R.id.textTimer);
        textResend = findViewById(R.id.textResend);
        btnContinue = findViewById(R.id.btnContinue);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnContinue.setOnClickListener(v -> handleVerify());

        textResend.setOnClickListener(v -> {
            if (isResendEnabled) {
                resendOtp();
            }
        });
    }

    // Logic tự động chuyển ô nhập
    private void setupOtpInputs() {
        EditText[] edits = {otp1, otp2, otp3, otp4, otp5, otp6};

        for (int i = 0; i < edits.length; i++) {
            final int index = i;
            edits[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1 && index < edits.length - 1) {
                        edits[index + 1].requestFocus(); // Chuyển sang ô kế tiếp
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            // Xử lý nút Xóa (Backspace) để lùi ô
            edits[i].setOnKeyListener((v, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (edits[index].getText().toString().isEmpty() && index > 0) {
                        edits[index - 1].requestFocus();
                        return true;
                    }
                }
                return false;
            });
        }
    }

    private void startTimer(long duration) {
        isResendEnabled = false;
        textResend.setTextColor(getResources().getColor(R.color.selector_nav_item));

        countDownTimer = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textTimer.setText((millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
                textTimer.setText("0s");
                isResendEnabled = true;
                textResend.setTextColor(getResources().getColor(R.color.chat_accent)); // Màu xanh (bấm được)
                Toast.makeText(VerifyOTPActivity.this, "Bạn có thể gửi lại mã", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    private void handleVerify() {
        String code = otp1.getText().toString() + otp2.getText().toString() +
                otp3.getText().toString() + otp4.getText().toString() +
                otp5.getText().toString() + otp6.getText().toString();

        if (code.length() < 6) {
            Toast.makeText(this, "Vui lòng nhập đủ 6 số", Toast.LENGTH_SHORT).show();
            return;
        }

        btnContinue.setEnabled(false);
        btnContinue.setText("Đang xử lý...");

        if (isForgotPassword) {
            verifyOtpForResetPassword(code);
        } else {
            verifyOtpForRegister(code);
        }
    }

    // 1. API Xác thực cho Quên Mật Khẩu
    private void verifyOtpForResetPassword(String otpCode) {
        VerifyOtpRequestDto request = new VerifyOtpRequestDto(email, otpCode);

        ApiClient.getApiService().verifyOtpResetPassword(request).enqueue(new Callback<BaseResponse<Boolean>>() {
            @Override
            public void onResponse(Call<BaseResponse<Boolean>> call, Response<BaseResponse<Boolean>> response) {
                btnContinue.setEnabled(true);
                btnContinue.setText("Tiếp tục");

                if (response.isSuccessful() && response.body() != null) {
                    BaseResponse<Boolean> body = response.body();

                    // Backend trả về data: true nghĩa là thành công
                    if (body.isSuccess() && Boolean.TRUE.equals(body.getData())) {
                        Toast.makeText(VerifyOTPActivity.this, "Xác thực thành công!", Toast.LENGTH_SHORT).show();

                        // Chuyển sang màn hình Đặt lại mật khẩu (ResetPasswordActivity)
                        Intent intent = new Intent(VerifyOTPActivity.this, ResetPasswordActivity.class);
                        intent.putExtra("EMAIL", email);
                        intent.putExtra("OTP", otpCode); // Cần truyền OTP sang để gọi API Reset Password
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(VerifyOTPActivity.this, body.getStatus(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(VerifyOTPActivity.this, "Mã OTP không đúng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Boolean>> call, Throwable t) {
                btnContinue.setEnabled(true);
                btnContinue.setText("Tiếp tục");
                Toast.makeText(VerifyOTPActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 2. API Xác thực cho Đăng Ký (Code cũ)
    private void verifyOtpForRegister(String otpCode) {
        VerifyOtpRequestDto request = new VerifyOtpRequestDto(email, otpCode);

        ApiClient.getApiService().verifyOtp(request).enqueue(new Callback<BaseResponse<UserResponseDto>>() {
            @Override
            public void onResponse(Call<BaseResponse<UserResponseDto>> call, Response<BaseResponse<UserResponseDto>> response) {
                btnContinue.setEnabled(true);
                btnContinue.setText("Tiếp tục");

                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        Toast.makeText(VerifyOTPActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(VerifyOTPActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(VerifyOTPActivity.this, response.body().getStatus(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(VerifyOTPActivity.this, "Mã OTP không đúng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<UserResponseDto>> call, Throwable t) {
                btnContinue.setEnabled(true);
                btnContinue.setText("Tiếp tục");
                Toast.makeText(VerifyOTPActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resendOtp() {
        // Gọi API gửi lại OTP (Cần thêm API này vào Interface nếu backend hỗ trợ)
        // Tạm thời chỉ reset timer giả lập
        startTimer(60000);
        Toast.makeText(this, "Đã gửi lại mã (Giả lập)", Toast.LENGTH_SHORT).show();

        // TODO: Gọi API resendOtp(email)
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
    }
}
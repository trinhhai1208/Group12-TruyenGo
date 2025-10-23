package com.example.truyengo;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class VerifyOTP extends AppCompatActivity {
    private EditText otp1, otp2, otp3, otp4;
    private Button btnContinue;
    private TextView resendText, timerText;
    private CountDownTimer countDownTimer;
    private boolean isCounting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_otp);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mapping();
        // Focus ô đầu tiên
        otp1.requestFocus();

        // Thiết lập logic OTP
        setupOTPInput(otp1, otp2, null);
        setupOTPInput(otp2, otp3, otp1);
        setupOTPInput(otp3, otp4, otp2);
        setupOTPInput(otp4, null, otp3);

        // Disable button ban đầu
        btnContinue.setEnabled(false);

        // Bắt đầu đếm ngược
        startCountDown();

        // Click gửi lại mã
        resendText.setOnClickListener(v -> {
            if (!isCounting) {
                startCountDown();
            }
        });
    }

    public void mapping(){
        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        btnContinue = findViewById(R.id.btnContinue);
        resendText = findViewById(R.id.textResend); // lát thêm id này
        timerText = findViewById(R.id.textTimer);   // lát thêm id này

    }

    private void setupOTPInput(EditText current, EditText next, EditText previous) {
        current.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Nếu nhập ký tự → qua ô kế tiếp
                if (s.length() == 1 && next != null) {
                    next.requestFocus();
                }
                // Nếu xóa ký tự → quay lại ô trước
                else if (s.length() == 0 && previous != null) {
                    previous.requestFocus();
                }

                // Cập nhật trạng thái nút "Tiếp tục"
                checkOTPComplete();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void checkOTPComplete() {
        String otpCode = otp1.getText().toString().trim()
                + otp2.getText().toString().trim()
                + otp3.getText().toString().trim()
                + otp4.getText().toString().trim();

        if (otpCode.length() == 4) {
            btnContinue.setEnabled(true);
            btnContinue.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light)); // #A4C18B
            btnContinue.setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_green_light));
        } else {
            btnContinue.setEnabled(false);
            btnContinue.setBackgroundResource(R.drawable.btn_continue); // màu xám mặc định
        }
    }

    private void startCountDown() {
        if (countDownTimer != null) countDownTimer.cancel();

        resendText.setAlpha(0.5f);
        resendText.setEnabled(false);
        isCounting = true;

        countDownTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                timerText.setText(seconds + "s");
            }

            public void onFinish() {
                isCounting = false;
                timerText.setText("0s");
                resendText.setAlpha(1f);
                resendText.setEnabled(true);
            }
        }.start();
    }
}
package com.example.truyengo.ui.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.truyengo.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private EditText edtUsername, edtEmail;
    private TextView btnGetCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password); // tên XML bạn dùng

        // Ánh xạ view
        btnBack = findViewById(R.id.btnBack);
        edtUsername = findViewById(R.id.edtUsername);
        edtEmail = findViewById(R.id.edtEmail);
        btnGetCode = findViewById(R.id.btnGetCode);

        // Nút quay lại
        btnBack.setOnClickListener(v -> onBackPressed());

        // Nút lấy mã xác nhận
        btnGetCode.setOnClickListener(v -> {
            String username = edtUsername.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            // TODO: Gọi API gửi mã xác nhận đến email
            Toast.makeText(this, "Mã xác nhận đã được gửi đến email của bạn", Toast.LENGTH_SHORT).show();
        });
    }
}

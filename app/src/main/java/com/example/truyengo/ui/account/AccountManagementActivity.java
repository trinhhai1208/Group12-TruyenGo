package com.example.truyengo.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.truyengo.R;
import com.example.truyengo.dto.response.BaseResponse;
import com.example.truyengo.dto.response.UserResponseDto;
import com.example.truyengo.ui.auth.LoginActivity;
import com.example.truyengo.utils.ApiClient;
import com.example.truyengo.utils.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountManagementActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private ImageView imgAvatar;
    private TextView tvFullNameMain, tvFullName, tvUsername, tvEmail, tvPhone, tvStatus, btnChangeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_account_management_activity);

        initViews();
        setupListeners();

        // Gọi API ngay khi mở màn hình
        loadUserProfile();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        imgAvatar = findViewById(R.id.imgAvatar);

        tvFullNameMain = findViewById(R.id.tvFullName); // Tên to ở dưới avatar
        tvFullName = findViewById(R.id.tvName);             // Tên ở list
        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvStatus = findViewById(R.id.tvStatus);

        btnChangeName = findViewById(R.id.btnChangeName);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnChangeName.setOnClickListener(v -> {
            // Xử lý mở Dialog đổi tên hoặc chuyển màn hình
            Toast.makeText(this, "Chức năng đổi tên đang phát triển", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadUserProfile() {
        TokenManager tokenManager = new TokenManager(this);
        String token = tokenManager.getAccessToken();

        if (token == null) {
            Toast.makeText(this, "Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        String authHeader = "Bearer " + token;

        // Gọi lại API getProfile (tái sử dụng DTO UserResponseDto)
        ApiClient.getApiService().getUserProfile(authHeader).enqueue(new Callback<BaseResponse<UserResponseDto>>() {
            @Override
            public void onResponse(Call<BaseResponse<UserResponseDto>> call, Response<BaseResponse<UserResponseDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BaseResponse<UserResponseDto> body = response.body();

                    if (body.isSuccess() && body.getData() != null) {
                        UserResponseDto user = body.getData();
                        updateUI(user);
                    }
                } else {
                    if (response.code() == 401) {
                        Toast.makeText(AccountManagementActivity.this, "Phiên đăng nhập hết hạn", Toast.LENGTH_SHORT).show();
                        tokenManager.clear();
                        startActivity(new Intent(AccountManagementActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(AccountManagementActivity.this, "Lỗi tải thông tin", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<UserResponseDto>> call, Throwable t) {
                Toast.makeText(AccountManagementActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(UserResponseDto user) {
        String fullName = user.getFullName();
        String username = user.getUsername();
        String displayname = fullName.isEmpty() ? username : fullName;

        // Cập nhật Text
        tvFullNameMain.setText(displayname);
        tvFullName.setText(displayname);
        tvUsername.setText(username);
        tvEmail.setText(user.getEmail());

        String phone = user.getPhone();
        tvPhone.setText((phone != null && !phone.isEmpty()) ? phone : "Chưa cập nhật");

        // Cập nhật Avatar
        if (user.getLinkAvatar() != null && !user.getLinkAvatar().isEmpty()) {
            Glide.with(this)
                    .load(user.getLinkAvatar())
                    .placeholder(R.drawable.circle_bg)
                    .error(R.drawable.ic_person)
                    .circleCrop()
                    .into(imgAvatar);
        }
    }
}
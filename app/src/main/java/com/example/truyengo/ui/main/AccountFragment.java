package com.example.truyengo.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.truyengo.R;
import com.example.truyengo.dto.response.BaseResponse;
import com.example.truyengo.dto.response.UserResponseDto;
import com.example.truyengo.ui.account.AccountManagementActivity;
import com.example.truyengo.ui.account.AppInfoActivity;
import com.example.truyengo.ui.account.ChangePasswordActivity;
import com.example.truyengo.ui.auth.ForgotPasswordActivity;
import com.example.truyengo.ui.auth.LoginActivity;
import com.example.truyengo.utils.ApiClient;
import com.example.truyengo.utils.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {

    // 1. Khai báo View khớp với XML
    private ImageView imgAvatar;
    private TextView tvUsername;

    // Các mục menu
    private LinearLayout itemAccountManagement, itemChangePassword, itemForgotPassword, itemAboutApp;

    // Nút chức năng
    private TextView btnDeleteAccount, btnLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        initViews(view);
        setupListeners();

        // 2. Gọi API lấy thông tin ngay khi mở màn hình
        loadUserProfile();

        return view;
    }

    private void initViews(View view) {
        imgAvatar = view.findViewById(R.id.imgAvatar);
        tvUsername = view.findViewById(R.id.tvUsername);

        itemAccountManagement = view.findViewById(R.id.itemAccountManagement);
        itemChangePassword = view.findViewById(R.id.itemChangePassword);
        itemForgotPassword = view.findViewById(R.id.itemForgotPassword);
        itemAboutApp = view.findViewById(R.id.itemAboutApp);

        btnDeleteAccount = view.findViewById(R.id.btnDeleteAccount);
        btnLogout = view.findViewById(R.id.btnLogout);
    }

    private void setupListeners() {
        // Chuyển màn hình (Giữ lịch sử)
        itemAccountManagement.setOnClickListener(v -> navigateToActivity(AccountManagementActivity.class, false));
        itemChangePassword.setOnClickListener(v -> navigateToActivity(ChangePasswordActivity.class, false));
        itemForgotPassword.setOnClickListener(v -> navigateToActivity(ForgotPasswordActivity.class, false));
        itemAboutApp.setOnClickListener(v -> navigateToActivity(AppInfoActivity.class, false));

        // Xóa tài khoản (Logic xử lý sau)
        btnDeleteAccount.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Chức năng đang phát triển", Toast.LENGTH_SHORT).show();
        });

        // Đăng xuất (Xóa token + Xóa lịch sử)
        btnLogout.setOnClickListener(v -> {
            TokenManager tokenManager = new TokenManager(getContext());
            tokenManager.clear();
            Toast.makeText(getContext(), "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
            navigateToActivity(LoginActivity.class, true);
        });
    }

    // --- HÀM GỌI API LẤY PROFILE ---
    private void loadUserProfile() {
        TokenManager tokenManager = new TokenManager(getContext());
        String accessToken = tokenManager.getAccessToken();

        // Nếu chưa đăng nhập thì thôi không gọi API
        if (accessToken == null) return;

        // Header Authorization: Bearer <token>
        String authHeader = "Bearer " + accessToken;

        ApiClient.getApiService().getUserProfile(authHeader).enqueue(new Callback<BaseResponse<UserResponseDto>>() {
            @Override
            public void onResponse(Call<BaseResponse<UserResponseDto>> call, Response<BaseResponse<UserResponseDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BaseResponse<UserResponseDto> body = response.body();

                    if (body.isSuccess() && body.getData() != null) {
                        // API trả về thành công -> Cập nhật giao diện
                        updateUI(body.getData());
                    }
                } else {
                    if (response.code() == 401) {
                        // Token hết hạn -> Bắt đăng nhập lại
                        Toast.makeText(getContext(), "Phiên đăng nhập hết hạn", Toast.LENGTH_SHORT).show();
                        navigateToActivity(LoginActivity.class, true);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<UserResponseDto>> call, Throwable t) {
                // Lỗi mạng, có thể log ra hoặc kệ (giữ nguyên UI mặc định)
            }
        });
    }

    // --- HÀM CẬP NHẬT GIAO DIỆN ---
    private void updateUI(UserResponseDto user) {
        if (user == null) return;

        // 1. Cập nhật Tên hiển thị
        // Ưu tiên hiển thị FullName (FirstName + LastName), nếu rỗng thì hiện Username
        String displayName = user.getFullName();
        if (displayName == null || displayName.trim().isEmpty()) {
            displayName = user.getUsername();
        }
        tvUsername.setText(displayName);

        // 2. Cập nhật Avatar (Dùng Glide)
        // Kiểm tra nếu linkAvatar có dữ liệu thì load, không thì giữ nguyên ảnh mặc định
        if (user.getLinkAvatar() != null && !user.getLinkAvatar().isEmpty()) {
            Glide.with(this)
                    .load(user.getLinkAvatar())
                    .placeholder(R.drawable.circle_bg) // Ảnh chờ
                    .error(R.drawable.ic_person)       // Ảnh lỗi
                    .circleCrop()                      // Bo tròn ảnh
                    .into(imgAvatar);
        }
    }

    private void navigateToActivity(Class<?> targetClass, boolean isClearHistory) {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), targetClass);
            if (isClearHistory) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish(); // Đóng Activity cha
            } else {
                startActivity(intent);
            }
        }
    }
}
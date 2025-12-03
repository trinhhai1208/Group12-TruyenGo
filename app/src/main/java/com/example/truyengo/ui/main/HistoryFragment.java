package com.example.truyengo.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.truyengo.R;
import com.example.truyengo.commons.BookGridAdapter;
import com.example.truyengo.dto.response.BaseResponse;
import com.example.truyengo.models.book.Book;
import com.example.truyengo.ui.auth.LoginActivity;
import com.example.truyengo.utils.ApiClient;
import com.example.truyengo.utils.TokenManager;
// Import các class cần thiết khác...

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment {

    private RecyclerView rvHistory;
    private BookGridAdapter historyAdapter; // Tái sử dụng Adapter của bạn

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false); // Đảm bảo tên file xml đúng

        // 1. Ánh xạ
        rvHistory = view.findViewById(R.id.rvHistory);

        // 2. Setup RecyclerView
        setupRecyclerView();

        // 3. Load dữ liệu lịch sử (Giả lập hoặc gọi API/Room Database)
        loadUserHistory();

        return view;
    }

    private void setupRecyclerView() {
        // TÙY CHỌN 1: Hiển thị dạng LƯỚI (Giống rvNewGrid ở Home) - 3 cột
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        rvHistory.setLayoutManager(gridLayoutManager);

        /* // TÙY CHỌN 2: Hiển thị dạng DANH SÁCH DỌC (Phổ biến cho Lịch sử)
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvHistory.setLayoutManager(linearLayoutManager);
        */

        historyAdapter = new BookGridAdapter(getContext());
        rvHistory.setAdapter(historyAdapter);
    }

    private void loadUserHistory() {
        // 1. Lấy Token (Giống code mẫu)
        TokenManager tokenManager = new TokenManager(getContext());
        String token = tokenManager.getAccessToken();
        String userId = tokenManager.getUserId();

        if (token == null) {
            Toast.makeText(getContext(), "Phiên đăng nhập hết hạn", Toast.LENGTH_SHORT).show();
            navigateToActivity(LoginActivity.class, true);
            return;
        }

        String authHeader = "Bearer " + token;

        // 2. Gọi API getHistory
        ApiClient.getApiService().getHistory(authHeader, userId).enqueue(new Callback<BaseResponse<List<Book>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Book>>> call, Response<BaseResponse<List<Book>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BaseResponse<List<Book>> body = response.body();

                    // Kiểm tra success và data có tồn tại không
                    if (body.isSuccess() && !body.getData().isEmpty()) {
                        List<Book> books = body.getData();

                        // Cập nhật RecyclerView
                        if (historyAdapter != null) {
                            historyAdapter.setBooks(books);
                        }
                    } else {
                        // Xử lý trường hợp không thành công hoặc list rỗng
                        Toast.makeText(getContext(), "Không có dữ liệu lịch sử", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Lỗi server: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<Book>>> call, Throwable t) {
                android.util.Log.e("HistoryError", "Lỗi: " + t.getMessage());
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
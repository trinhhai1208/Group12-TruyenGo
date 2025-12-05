package com.example.truyengo.ui.main;

import static com.example.truyengo.api.ApiGet.getApi;

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
import com.example.truyengo.api.book.ApiOneBookResponse;
import com.example.truyengo.commons.BookGridAdapter;
import com.example.truyengo.data.GetAllBook;
import com.example.truyengo.dto.response.BaseResponse;
import com.example.truyengo.dto.response.HistoryAndFavoriteResponseDto;
import com.example.truyengo.models.book.Book;
import com.example.truyengo.ui.auth.LoginActivity;
import com.example.truyengo.utils.ApiClient;
import com.example.truyengo.utils.TokenManager;
import com.google.gson.Gson;
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
        ApiClient.getApiService().getHistory("Bearer " + token, userId).enqueue(new Callback<BaseResponse<List<HistoryAndFavoriteResponseDto>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<HistoryAndFavoriteResponseDto>>> call, Response<BaseResponse<List<HistoryAndFavoriteResponseDto>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BaseResponse<List<HistoryAndFavoriteResponseDto>> body = response.body();

                    if (body.isSuccess() && body.getData() != null && !body.getData().isEmpty()) {
                        List<HistoryAndFavoriteResponseDto> bookSlugs = body.getData();

                        // --- SỬA TẠI ĐÂY: Tạo Thread mới để xử lý lấy dữ liệu chi tiết ---
                        new Thread(() -> {
                            List<Book> books = new ArrayList<>();

                            // 1. Chạy vòng lặp lấy dữ liệu (Chạy ngầm, không gây lag UI)
                            for (HistoryAndFavoriteResponseDto h : bookSlugs) {
                                // Gọi hàm getBooksTheoTen (Có chứa HttpURLConnection) ở đây là an toàn
                                GetAllBook getAllBook = new GetAllBook();
                                Book b = getAllBook.getBooksTheoTen(h.getSlug());

                                // Kiểm tra nếu lấy được dữ liệu thì mới add
                                if (b != null && b.getName() != null) {
                                    books.add(b);
                                }
                            }

                            // 2. Sau khi lấy xong hết, quay về Main Thread để hiển thị lên RecyclerView
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(() -> {
                                    if (historyAdapter != null) {
                                        historyAdapter.setBooks(books);
                                        // Ẩn loading nếu có
                                    } else {
                                        // Log cảnh báo nếu adapter chưa khởi tạo
                                        android.util.Log.w("HistoryFragment", "Adapter is null");
                                    }
                                });
                            }
                        }).start();
                        // ------------------------------------------------------------------

                    } else {
                        Toast.makeText(getContext(), "Không có dữ liệu lịch sử", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Lỗi server: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<HistoryAndFavoriteResponseDto>>> call, Throwable t) {
                android.util.Log.e("HistoryError", "Lỗi: " + t.getMessage());
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
package com.example.truyengo.ui.book;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.truyengo.R;
import com.example.truyengo.api.book.ApiOneBookResponse; // Đảm bảo import đúng package
import com.example.truyengo.commons.ChapterAdapter;
import com.example.truyengo.data.GetChapters; // Import class GetChapters của bạn
import com.example.truyengo.dto.response.BaseResponse;
import com.example.truyengo.dto.response.LastReadHistoryResponseDto;
import com.example.truyengo.models.book.Book;
import com.example.truyengo.models.chapter.AllChapters;
import com.example.truyengo.ui.auth.LoginActivity;
import com.example.truyengo.utils.ApiClient;
import com.example.truyengo.utils.TokenManager;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDetailActivity extends AppCompatActivity {

    // Views
    private ImageView btnBack, btnFavorite, imgDetailCover;
    private TextView btnContinue, tvDetailTitle, tvDetailAuthor, tvDetailStatus, tvDetailContent;

    // Tabs
    private TextView tabOverview, tabChapter;
    private View indicatorOverview, indicatorChapter;
    private LinearLayout layoutOverview, layoutChapter;

    private boolean isFavorite = false;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    // RecyclerView cho Chapter
    private RecyclerView rvChapterList;
    private ChapterAdapter chapterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_book_detail_activity);

        initViews();
        setupListeners();

        String bookSlug = getIntent().getStringExtra("BOOK_SLUG");
        if (bookSlug != null) {
            loadBookDetails(bookSlug);
        } else {
            Toast.makeText(this, "Lỗi: Không tìm thấy truyện", Toast.LENGTH_SHORT).show();
        }
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnContinue = findViewById(R.id.btnContinue);

        imgDetailCover = findViewById(R.id.imgDetailCover);
        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailAuthor = findViewById(R.id.tvDetailAuthor);
        tvDetailStatus = findViewById(R.id.tvDetailStatus);
        tvDetailContent = findViewById(R.id.tvDetailContent);

        tabOverview = findViewById(R.id.tabOverview);
        tabChapter = findViewById(R.id.tabChapter);
        indicatorOverview = findViewById(R.id.indicatorOverview);
        indicatorChapter = findViewById(R.id.indicatorChapter);

        layoutOverview = findViewById(R.id.layoutOverview);
        layoutChapter = findViewById(R.id.layoutChapter);

        // Ánh xạ RecyclerView cho Chapter
        rvChapterList = findViewById(R.id.rvChapterList);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnFavorite.setOnClickListener(v -> {
            isFavorite = !isFavorite;
            if (isFavorite) {
                btnFavorite.setImageResource(R.drawable.ic_heart_red);
                Toast.makeText(this, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
            } else {
                btnFavorite.setImageResource(R.drawable.ic_heart_black);
            }
        });

        tabOverview.setOnClickListener(v -> showOverview());
        tabChapter.setOnClickListener(v -> showChapters());

        showOverview();
    }

    private void loadBookDetails(String slug) {
        executorService.execute(() -> {
            Book book = getBooksTheoTen(slug);

            runOnUiThread(() -> {
                if (book != null && book.getName() != null) {
                    displayBookData(book);
                } else {
                    Toast.makeText(BookDetailActivity.this, "Không tải được dữ liệu truyện", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void displayBookData(Book book) {
        // 1. Hiển thị thông tin cơ bản
        if (tvDetailTitle != null) tvDetailTitle.setText(book.getName());

        // Tác giả và Trạng thái (Nếu model Book có trường này)
        if (tvDetailStatus != null) tvDetailStatus.setText(book.getStatus());
        // if (tvDetailAuthor != null) tvDetailAuthor.setText(book.getAuthor());

        if (tvDetailContent != null && book.getContent() != null) {
            tvDetailContent.setText(android.text.Html.fromHtml(book.getContent(), android.text.Html.FROM_HTML_MODE_LEGACY));
        }

        if (book.getId() != null) {
            loadBookByHistoryAndFavorite(book.getId());
        }

        // 2. Load ảnh bìa
        String baseUrl = "https://img.otruyenapi.com/uploads/comics/";
        String thumbUrl = book.getThumbnail(); // Hoặc getThumbUrl() tùy model
        String fullUrl = baseUrl + thumbUrl;

        if (imgDetailCover != null) {
            Glide.with(this).load(fullUrl).into(imgDetailCover);
        }

        // 3. XỬ LÝ DANH SÁCH CHƯƠNG (Dùng RecyclerView + GetChapters)
        if (book.getChapters() != null && !book.getChapters().isEmpty()) {

            // Setup RecyclerView
            rvChapterList.setLayoutManager(new LinearLayoutManager(this));
            chapterAdapter = new ChapterAdapter(this, thumbUrl, book.getName());
            rvChapterList.setAdapter(chapterAdapter);

            // Dùng helper class của bạn để parse list chapter
            GetChapters getChaptersHelper = new GetChapters();
            ArrayList<AllChapters> listChapters = getChaptersHelper.getListChapters(book);

            System.out.println(listChapters.size());

            // Đổ dữ liệu vào Adapter
            chapterAdapter.setChapters(listChapters);
        }
    }

    // --- Logic API (Giữ nguyên từ code của bạn) ---
    public Book getBooksTheoTen(String tenTruyen) {
        Book book = new Book();
        try {
            String apiUrl = "https://otruyenapi.com/v1/api/truyen-tranh/" + tenTruyen;
            String jsonData = getApi(apiUrl);

            if (jsonData != null && !jsonData.isEmpty()) {
                Gson gson = new Gson();
                ApiOneBookResponse apiResponse = gson.fromJson(jsonData, ApiOneBookResponse.class);

                if (apiResponse != null && apiResponse.getData() != null) {
                    var b = apiResponse.getData().getItem();
                    // Map dữ liệu
                    book.setName(b.getName());
                    book.setSlug(b.getSlug());
                    book.setContent(b.getContent());
                    book.setStatus(b.getStatus());
                    book.setThumbnail(b.getThumbUrl()); // Set thumbUrl
                    book.setChapters(b.getChapters());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return book;
    }

    private String getApi(String urlString) {
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result.toString();
    }

    private void loadBookByHistoryAndFavorite(String bookId) {
        // 1. Lấy Token (Giống code mẫu)
        TokenManager tokenManager = new TokenManager(this);
        String token = tokenManager.getAccessToken();
        String userId = tokenManager.getUserId();

        if (token == null) {
            Toast.makeText(this, "Phiên đăng nhập hết hạn", Toast.LENGTH_SHORT).show();
            navigateToActivity(LoginActivity.class, true);
            return;
        }

        String authHeader = "Bearer " + token;

        // 2. Gọi API getHistory
        ApiClient.getApiService().getLastReadChapter(authHeader, userId, bookId).enqueue(new Callback<BaseResponse<LastReadHistoryResponseDto>>() {
            @Override
            public void onResponse(Call<BaseResponse<LastReadHistoryResponseDto>> call, Response<BaseResponse<LastReadHistoryResponseDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BaseResponse<LastReadHistoryResponseDto> body = response.body();

                    // Kiểm tra success và data có tồn tại không
                    if (body.isSuccess() && body.getData() != null) {
                        LastReadHistoryResponseDto books = body.getData();

                        String chapterNum = String.valueOf(books.getLastReadChapter());
                        if (books.getLastReadChapter() != 1) {
                            btnContinue.setText("Tiếp tục đọc chapter " + chapterNum);
                        } else {
                            btnContinue.setText("Đọc ngay");
                        }
                    } else {
                        btnContinue.setText("Đọc ngay");
                    }
                } else {
                    android.util.Log.e("API_ERROR", "Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<LastReadHistoryResponseDto>> call, Throwable t) {
                android.util.Log.e("Check history error", "Lỗi: " + t.getMessage());
                Toast.makeText(BookDetailActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        ApiClient.getApiService().checkIsFavorite(authHeader, userId, bookId).enqueue(new Callback<BaseResponse<Boolean>>() {
            @Override
            public void onResponse(Call<BaseResponse<Boolean>> call, Response<BaseResponse<Boolean>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BaseResponse<Boolean> body = response.body();

                    // Kiểm tra success và data có tồn tại không
                    if (body.isSuccess() && body.getData() != null) {
                        Boolean books = body.getData();

                        if (books.TRUE) {
                            btnFavorite.setImageResource(R.drawable.ic_heart_red);
                        } else {
                            btnFavorite.setImageResource(R.drawable.ic_heart_black);
                        }
                    } else {
                        btnFavorite.setImageResource(R.drawable.ic_heart_black);
                    }
                } else {
                    android.util.Log.e("API_ERROR", "Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Boolean>> call, Throwable t) {
                android.util.Log.e("Check favorite error", "Lỗi: " + t.getMessage());
                Toast.makeText(BookDetailActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToActivity(Class<?> targetClass, boolean isClearHistory) {
        Intent intent = new Intent(this, targetClass);
        if (isClearHistory) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish(); // Đóng Activity hiện tại
        } else {
            startActivity(intent);
        }
    }


    private void showOverview() {
        layoutOverview.setVisibility(View.VISIBLE);
        layoutChapter.setVisibility(View.GONE);
        tabOverview.setTextColor(getColor(R.color.nav_selected_color));
        indicatorOverview.setBackgroundColor(getColor(R.color.nav_selected_color));
        tabChapter.setTextColor(getColor(R.color.nav_unselected_color));
        indicatorChapter.setBackgroundColor(Color.TRANSPARENT);
    }

    private void showChapters() {
        layoutOverview.setVisibility(View.GONE);
        layoutChapter.setVisibility(View.VISIBLE);
        tabChapter.setTextColor(getColor(R.color.nav_selected_color));
        indicatorChapter.setBackgroundColor(getColor(R.color.nav_selected_color));
        tabOverview.setTextColor(getColor(R.color.nav_unselected_color));
        indicatorOverview.setBackgroundColor(Color.TRANSPARENT);
    }
}
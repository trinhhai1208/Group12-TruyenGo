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
import com.example.truyengo.api.book.ApiOneBookResponse;
import com.example.truyengo.commons.ChapterAdapter;
import com.example.truyengo.data.GetChapters;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDetailActivity extends AppCompatActivity {

    // Views
    private ImageView btnBack, btnFavorite, imgDetailCover;
    private TextView btnContinue, tvDetailTitle, tvDetailAuthor, tvDetailStatus, tvDetailContent;
    private TextView tabOverview, tabChapter;
    private View indicatorOverview, indicatorChapter;
    private LinearLayout layoutOverview, layoutChapter;
    private RecyclerView rvChapterList;

    private ChapterAdapter chapterAdapter;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    // Biến toàn cục để lưu trạng thái
    private boolean isFavorite = false;
    private int curChapter = 1;
    private Book currentBook; // QUAN TRỌNG: Lưu book để dùng lại, tránh gọi API lại

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
        rvChapterList = findViewById(R.id.rvChapterList);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        // FIX: Gọi API toggleFavoriteApi thay vì chỉ đổi icon local
        btnFavorite.setOnClickListener(v -> {
            if (currentBook != null && currentBook.getId() != null) {
                toggleFavoriteApi(currentBook.getId());
            } else {
                Toast.makeText(this, "Đang tải dữ liệu...", Toast.LENGTH_SHORT).show();
            }
        });

        tabOverview.setOnClickListener(v -> showOverview());
        tabChapter.setOnClickListener(v -> showChapters());

        // FIX: Dùng biến currentBook đã lưu, KHÔNG gọi lại getBooksTheoTen (tránh crash MainThread)
        btnContinue.setOnClickListener(v -> {
            GetChapters getChaptersHelper = new GetChapters();
            ArrayList<AllChapters> chapters = getChaptersHelper.getListChapters(currentBook);


            if (currentBook != null && currentBook.getId() != null && chapters != null) { // chapters là list AllChapters của bạn

                // 1. Tìm index của chương đang đọc dở (curChapter) trong danh sách
                int index = -1;
                // Giả sử curChapter là String lưu tên chương (ví dụ: "10")
                for (int i = 0; i < chapters.size(); i++) {
                    if (chapters.get(i).getChapter_name().equals(curChapter)) {
                        index = i;
                        break;
                    }
                }

                // Nếu tìm thấy hoặc mặc định mở chương đầu tiên nếu không tìm thấy
                if (index == -1) index = 0;

                // 2. Chuyển màn hình đọc
                Intent intent = new Intent(this, ReadActivity.class);
                intent.putExtra("ALL_CHAPTERS", (ArrayList<AllChapters>) chapters); // List chương
                intent.putExtra("CURRENT_INDEX", index);            // Vị trí chương hiện tại
                intent.putExtra("BOOK_NAME", currentBook.getName()); // Tên truyện
                intent.putExtra("BOOK_ID", currentBook.getId());     // QUAN TRỌNG: Truyền ID truyện để lưu lịch sử
                startActivity(intent);

                // Lưu ý: Bạn không cần gọi addToHistoryApi ở đây nữa,
                // vì khi ReadActivity mở lên nó sẽ tự gọi API lưu lịch sử cho chương đó.
            }
        });

        showOverview();
    }

    private void loadBookDetails(String slug) {
        executorService.execute(() -> {
            // Hàm này chạy background thread
            Book book = getBooksTheoTen(slug);

            runOnUiThread(() -> {
                if (book != null && book.getName() != null) {
                    this.currentBook = book; // LƯU LẠI ĐỂ DÙNG SAU
                    displayBookData(book);
                } else {
                    Toast.makeText(BookDetailActivity.this, "Không tải được dữ liệu truyện", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void displayBookData(Book book) {
        if (tvDetailTitle != null) tvDetailTitle.setText(book.getName());
        if (tvDetailStatus != null) tvDetailStatus.setText(book.getStatus());
        if (tvDetailContent != null && book.getContent() != null) {
            tvDetailContent.setText(android.text.Html.fromHtml(book.getContent(), android.text.Html.FROM_HTML_MODE_LEGACY));
        }

        // Gọi API check lịch sử và yêu thích
        if (book.getId() != null) {
            loadBookByHistoryAndFavorite(book.getId());
        }

        String baseUrl = "https://img.otruyenapi.com/uploads/comics/";
        String thumbUrl = book.getThumbnail();
        String fullUrl = baseUrl + thumbUrl;

        if (imgDetailCover != null) {
            Glide.with(this).load(fullUrl).into(imgDetailCover);
        }

        if (book.getChapters() != null && !book.getChapters().isEmpty()) {
            rvChapterList.setLayoutManager(new LinearLayoutManager(this));

            // BƯỚC 1: Lấy danh sách chapter trước để dùng bên trong Listener
            GetChapters getChaptersHelper = new GetChapters();
            ArrayList<AllChapters> listChapters = getChaptersHelper.getListChapters(book);

            // BƯỚC 2: Khởi tạo Adapter với Listener xử lý chuyển màn hình
            chapterAdapter = new ChapterAdapter(this, thumbUrl, book.getName(), (clickedChapter) -> {

                // Tìm vị trí (index) của chương vừa bấm trong danh sách tổng
                int index = -1;
                if (listChapters != null) {
                    for (int i = 0; i < listChapters.size(); i++) {
                        // So sánh tên chương (hoặc ID nếu có) để tìm vị trí
                        if (listChapters.get(i).getChapter_name().equals(clickedChapter.getChapter_name())) {
                            index = i;
                            break;
                        }
                    }
                }

                // Nếu tìm thấy, chuyển sang ReadActivity
                if (index != -1) {
                    Intent intent = new Intent(BookDetailActivity.this, ReadActivity.class);
                    intent.putExtra("ALL_CHAPTERS", listChapters);       // Truyền danh sách chương
                    intent.putExtra("CURRENT_INDEX", index);             // Truyền vị trí hiện tại
                    intent.putExtra("BOOK_NAME", book.getName());        // Truyền tên truyện
                    intent.putExtra("BOOK_ID", book.getId());            // Truyền ID để bên kia lưu lịch sử
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Lỗi: Không tìm thấy nội dung chương", Toast.LENGTH_SHORT).show();
                }
            });

            rvChapterList.setAdapter(chapterAdapter);
            // Set dữ liệu cho adapter
            chapterAdapter.setChapters(listChapters);
        }
    }

    // ... (Giữ nguyên các hàm getBooksTheoTen và getApi) ...
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
                    book.setId(b.getId()); // Đảm bảo mapping ID đúng
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
        TokenManager tokenManager = new TokenManager(this);
        String token = tokenManager.getAccessToken();
        String userId = tokenManager.getUserId();

        if (token == null) return;

        String authHeader = "Bearer " + token;

        ApiClient.getApiService().getLastReadChapter(authHeader, userId, bookId).enqueue(new Callback<BaseResponse<LastReadHistoryResponseDto>>() {
            @Override
            public void onResponse(Call<BaseResponse<LastReadHistoryResponseDto>> call, Response<BaseResponse<LastReadHistoryResponseDto>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    LastReadHistoryResponseDto data = response.body().getData();

                    // FIX logic hiển thị nút
                    curChapter = data.getLastReadChapter(); // Lưu lại chapter hiện tại
                    if (curChapter > 1) {
                        btnContinue.setText("Đọc tiếp chap " + curChapter);
                    } else {
                        btnContinue.setText("Đọc ngay");
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<LastReadHistoryResponseDto>> call, Throwable t) {
            }
        });

        ApiClient.getApiService().checkIsFavorite(authHeader, userId, bookId).enqueue(new Callback<BaseResponse<Boolean>>() {
            @Override
            public void onResponse(Call<BaseResponse<Boolean>> call, Response<BaseResponse<Boolean>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    // FIX: books.TRUE là sai cú pháp. Sửa thành Boolean.TRUE.equals(...)
                    Boolean isFav = response.body().getData();
                    isFavorite = Boolean.TRUE.equals(isFav);
                    updateFavoriteUI();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Boolean>> call, Throwable t) {
            }
        });
    }

    private void addToHistoryApi(String bookId, int chapterNumber) {
        TokenManager tokenManager = new TokenManager(this);
        String token = tokenManager.getAccessToken();
        String userId = tokenManager.getUserId();
        if (token == null) return;

        // Lưu ý: Đảm bảo thread an toàn hoặc call trong background nếu cần thiết (Retrofit tự lo background)
        ApiClient.getApiService().addToHistory("Bearer " + token, userId, bookId, chapterNumber).enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                // Log thành công
                android.util.Log.d("HISTORY", "Saved chapter " + chapterNumber);
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                // Log lỗi
            }
        });
    }

    private void toggleFavoriteApi(String bookId) {
        TokenManager tokenManager = new TokenManager(this);
        String token = tokenManager.getAccessToken();
        String userId = tokenManager.getUserId();
        if (token == null) {
            Toast.makeText(this, "Bạn cần đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiClient.getApiService().toggleFavorite("Bearer " + token, userId, bookId).enqueue(new Callback<BaseResponse<Boolean>>() {
            @Override
            public void onResponse(Call<BaseResponse<Boolean>> call, Response<BaseResponse<Boolean>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    isFavorite = response.body().getData();
                    updateFavoriteUI();
                    Toast.makeText(BookDetailActivity.this, isFavorite ? "Đã thích" : "Đã bỏ thích", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Boolean>> call, Throwable t) {
                Toast.makeText(BookDetailActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateFavoriteUI() {
        if (isFavorite) {
            btnFavorite.setImageResource(R.drawable.ic_heart_red);
        } else {
            btnFavorite.setImageResource(R.drawable.ic_heart_black);
        }
    }

    // ... (Giữ nguyên navigateToActivity và showOverview/showChapters) ...
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
package com.example.truyengo.ui.book;

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
import com.example.truyengo.models.book.Book;
import com.example.truyengo.models.chapter.AllChapters;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
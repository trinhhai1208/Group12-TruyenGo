package com.example.truyengo.ui.book;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.truyengo.R;
import com.example.truyengo.commons.ReadAdapter;
import com.example.truyengo.data.GetChapters; // Import GetChapters
import com.example.truyengo.dto.response.BaseResponse;
import com.example.truyengo.models.chapter.AllChapters;
import com.example.truyengo.models.chapter.Chapter;
import com.example.truyengo.models.chapter.ChapterImage;
import com.example.truyengo.utils.ApiClient;
import com.example.truyengo.utils.TokenManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReadActivity extends AppCompatActivity {

    private ImageButton btnBack, btnPrevChapter, btnNextChapter;
    private TextView tvChapterTitle, tvCurrentChapterName;
    private RecyclerView rvComicImages;
    private ReadAdapter readAdapter;

    private ArrayList<AllChapters> allChaptersList;
    private int currentChapterIndex;
    private String bookName;
    private String slug;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_read_chapter_activity);

        initViews();
        setupRecyclerView();
        setupListeners();

        if (getIntent() != null) {
            allChaptersList = (ArrayList<AllChapters>) getIntent().getSerializableExtra("ALL_CHAPTERS");
            currentChapterIndex = getIntent().getIntExtra("CURRENT_INDEX", -1);
            bookName = getIntent().getStringExtra("BOOK_NAME");
            slug = getIntent().getStringExtra("SLUG");

            if (allChaptersList != null && currentChapterIndex != -1) {
                loadChapter(currentChapterIndex);
            } else {
                Toast.makeText(this, "Lỗi dữ liệu chương", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnPrevChapter = findViewById(R.id.btnPrevChapter);
        btnNextChapter = findViewById(R.id.btnNextChapter);
        tvChapterTitle = findViewById(R.id.tvChapterTitle);
        tvCurrentChapterName = findViewById(R.id.tvCurrentChapterName);
        rvComicImages = findViewById(R.id.rvComicImages);
    }

    private void setupRecyclerView() {
        rvComicImages.setLayoutManager(new LinearLayoutManager(this));
        readAdapter = new ReadAdapter(this);
        rvComicImages.setAdapter(readAdapter);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnPrevChapter.setOnClickListener(v -> {
            if (currentChapterIndex > 0) {
                loadChapter(currentChapterIndex - 1);
            } else {
                Toast.makeText(this, "Đây là chương đầu tiên", Toast.LENGTH_SHORT).show();
            }
        });

        btnNextChapter.setOnClickListener(v -> {
            if (currentChapterIndex < allChaptersList.size() - 1) {
                loadChapter(currentChapterIndex + 1);
            } else {
                Toast.makeText(this, "Đây là chương cuối cùng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadChapter(int index) {
        currentChapterIndex = index;
        AllChapters currentChapter = allChaptersList.get(index);

        // Cập nhật UI Header và Bottom Bar
        tvChapterTitle.setText(bookName + " - Chapter " + currentChapter.getChapter_name());
        tvCurrentChapterName.setText("Chapter " + currentChapter.getChapter_name());

        // Cập nhật trạng thái nút Prev/Next (Làm mờ nếu không bấm được)
        btnPrevChapter.setAlpha(currentChapterIndex > 0 ? 1.0f : 0.5f);
        btnNextChapter.setAlpha(currentChapterIndex < allChaptersList.size() - 1 ? 1.0f : 0.5f);

        // Xóa ảnh cũ và cuộn lên đầu
        readAdapter.setImages(new ArrayList<>(), "");
        rvComicImages.scrollToPosition(0);

        // Gọi API lấy dữ liệu chương bằng GetChapters
        fetchChapterData(currentChapter);

        if (slug != null) {
            try {
                int chapNum = Integer.parseInt(currentChapter.getChapter_name());
                System.out.println("CHECK 2: " + chapNum);
                addToHistoryApi(slug, chapNum);
            } catch (NumberFormatException e) {
                android.util.Log.e("ReadActivity", "Không thể lưu lịch sử cho chương: " + currentChapter.getChapter_name());
            }
        }
    }

    // SỬ DỤNG GetChapters CỦA BẠN TẠI ĐÂY
    private void fetchChapterData(AllChapters currentChapter) {
        executorService.execute(() -> {
            try {
                // 1. Dùng class GetChapters để lấy dữ liệu chi tiết
                GetChapters getChaptersHelper = new GetChapters();
                Chapter chapterDetail = getChaptersHelper.getChapter(currentChapter);

                // 2. Xử lý dữ liệu trả về
                if (chapterDetail != null && chapterDetail.getChapter_image() != null) {

                    String chapterPath = chapterDetail.getChapter_path();
                    List<ChapterImage> images = chapterDetail.getChapter_image();

                    // Chuyển đổi List<ChapterImage> -> List<String> (tên file ảnh)
                    List<String> imageUrls = new ArrayList<>();
                    for (ChapterImage img : images) {
                        imageUrls.add(img.getImage_file());
                    }

                    // 3. Cập nhật UI trên Main Thread
                    runOnUiThread(() -> {
                        if (!imageUrls.isEmpty()) {
                            readAdapter.setImages(imageUrls, chapterPath);
                        } else {
                            Toast.makeText(ReadActivity.this, "Chương này chưa có ảnh", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(ReadActivity.this, "Lỗi tải dữ liệu chương", Toast.LENGTH_SHORT).show());
                }

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(ReadActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void addToHistoryApi(String slug, int chapterNumber) {
        TokenManager tokenManager = new TokenManager(this);
        String token = tokenManager.getAccessToken();
        String userId = tokenManager.getUserId();
        if (token == null) return;

        // Lưu ý: Đảm bảo thread an toàn hoặc call trong background nếu cần thiết (Retrofit tự lo background)
        ApiClient.getApiService().addToHistory("Bearer " + token, userId, slug, chapterNumber).enqueue(new Callback<BaseResponse<String>>() {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}
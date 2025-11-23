package com.example.truyengo.ui.book;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.truyengo.R;
import com.example.truyengo.commons.BookGridAdapter;
import com.example.truyengo.data.GetAllBook;
import com.example.truyengo.models.book.Book;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BookListActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextView tvPageTitle;
    private TabLayout tabLayout;
    private RecyclerView rvBookList;
    private BookGridAdapter adapter;
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    private int currentPage = 1;
    private boolean isLoading = false;
    private int currentTabIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_book_list_activity);

        initViews();
        setupRecyclerView();
        setupTabs();

        // Lấy dữ liệu được truyền từ màn Home (nếu có)
        String title = getIntent().getStringExtra("TITLE");
        if (title != null) {
            tvPageTitle.setText(title);
        }

        // Mặc định chọn tab đầu tiên và load dữ liệu
        loadBooks(0, 1);

        // Xử lý nút Back
        btnBack.setOnClickListener(v -> finish());
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tabLayout = findViewById(R.id.tabLayout);
        rvBookList = findViewById(R.id.rvBookList);
    }

    private void setupRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rvBookList.setLayoutManager(gridLayoutManager);
        adapter = new BookGridAdapter(this);
        rvBookList.setAdapter(adapter);

        // --- LẮNG NGHE SỰ KIỆN CUỘN ĐỂ LOAD TIẾP ---
        rvBookList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // dy > 0 nghĩa là đang lướt xuống dưới
                if (dy > 0) {
                    int visibleItemCount = gridLayoutManager.getChildCount();
                    int totalItemCount = gridLayoutManager.getItemCount();
                    int pastVisibleItems = gridLayoutManager.findFirstVisibleItemPosition();

                    // Nếu không phải đang tải VÀ đã lướt đến gần cuối danh sách
                    if (!isLoading) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            // Tăng số trang lên và gọi API
                            currentPage++;
                            loadBooks(currentTabIndex, currentPage);
                        }
                    }
                }
            }
        });
    }

    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("New Series"));
        tabLayout.addTab(tabLayout.newTab().setText("Coming Soon"));
        tabLayout.addTab(tabLayout.newTab().setText("Completed"));
        tabLayout.addTab(tabLayout.newTab().setText("Genres"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Khi đổi Tab -> Reset về trang 1
                currentTabIndex = tab.getPosition();
                currentPage = 1;

                // Xóa dữ liệu cũ trên giao diện ngay lập tức
                adapter.setBooks(new ArrayList<>());

                // Gọi API trang 1
                loadBooks(currentTabIndex, currentPage);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void loadBooks(int tabIndex, int page) {
        isLoading = true; // Đánh dấu đang tải để không gọi API trùng lặp

        // Nếu là trang 1 thì hiện loading hoặc xóa list cũ tùy ý
        // if (page == 1) adapter.setBooks(new ArrayList<>());

        executorService.execute(() -> {
            try {
                GetAllBook lb = new GetAllBook();
                ArrayList<Book> books = new ArrayList<>();

                // Truyền số trang (page) vào hàm API
                switch (tabIndex) {
                    case 0: // New Series
                        books = lb.getBooksTruyenMoi(page);
                        break;
                    case 1: // Coming Soon
                        books = lb.getBooksSapRaMat(page);
                        break;
                    case 2: // Completed
                        books = lb.getBooksHoanThanh(page);
                        // Lưu ý: Kiểm tra xem hàm getBooksHoanThanh của bạn có nhận tham số page không nhé
                        // Nếu class GetAllBook của bạn chưa hỗ trợ page cho hàm này, bạn phải sửa cả file đó.
                        break;
                    case 3: // Genres
                        // Thể loại thường ít khi phân trang kiểu này,
                        // hoặc API trả về full list. Nếu API không hỗ trợ page thì chỉ load lần đầu.
                        if (page == 1) books = lb.getBooksTheLoai();
                        break;
                }

                ArrayList<Book> finalBooks = books;
                runOnUiThread(() -> {
                    // Tải xong
                    isLoading = false;

                    if (finalBooks != null && !finalBooks.isEmpty()) {
                        if (page == 1) {
                            // Nếu là trang 1 -> Thay thế hoàn toàn dữ liệu
                            adapter.setBooks(finalBooks);
                        } else {
                            // Nếu là trang 2, 3... -> Nối thêm vào dưới đáy
                            adapter.addBooks(finalBooks);
                        }
                    } else {
                        // Nếu API trả về null hoặc rỗng -> Có thể là hết trang rồi
                        // Có thể hiển thị Toast "Đã hết truyện"
                        if (page > 1) currentPage--; // Trừ lại số trang nếu load thất bại hoặc hết data
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    isLoading = false;
                    if (page > 1) currentPage--;
                    Toast.makeText(BookListActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
                });
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
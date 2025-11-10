package com.example.truyengo; // Thay bằng package của bạn

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    private EditText etName;
    private TextView tvStatusMessage;
    private ImageView btnBack;
    private RecyclerView recyclerView;

    private BookAdapter bookAdapter; // Adapter của RecyclerView
    private List<Book> allBookList;

    // (Giả sử bạn có class Book.java)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_page);

        // 1. Ánh xạ View
        etName = findViewById(R.id.etName);
        tvStatusMessage = findViewById(R.id.tv_status_message);
        btnBack = findViewById(R.id.btnBack);
        recyclerView = findViewById(R.id.search_results_recycler_view);

        // 2. Tạo dữ liệu mẫu (Sách)
        createBookData();

        // 3. Thiết lập RecyclerView
        setupRecyclerView();

        // 4. Thiết lập các Listener
        setupSearchListener();
        setupBackButton();

        // Trạng thái ban đầu
        tvStatusMessage.setText("Hãy nhập từ khóa tìm kiếm...");
    }

    // Hàm thiết lập RecyclerView
    private void setupRecyclerView() {
        // Tạo Adapter ban đầu với danh sách RỖNG hoặc danh sách gợi ý
        bookAdapter = new BookAdapter(new ArrayList<>());
        recyclerView.setAdapter(bookAdapter);
        // Thiết lập LayoutManager (hiển thị danh sách theo chiều dọc)
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // Hàm tạo dữ liệu mẫu
    private void createBookData() {
        allBookList = new ArrayList<>();
        // Thay R.drawable.sample_illust1 bằng ID ảnh thật của bạn
        allBookList.add(new Book("Eighty Six", "Asato Asato", "Loading...", "Feb 22, 2022", "No", "Action | Sci-fi", R.drawable.sample_illust1));
        allBookList.add(new Book("The Alchemist", "Paulo Coelho", "Completed", "Jan 1, 2023", "Yes", "Philosophy", R.drawable.sample_illust2));
        allBookList.add(new Book("The Hobbit", "J.R.R. Tolkien", "Completed", "Mar 15, 2023", "No", "Fantasy", R.drawable.sample_illust2));
        allBookList.add(new Book("Brave New World", "Aldous Huxley", "Completed", "Dec 10, 2022", "No", "Dystopian", R.drawable.sample_illust1));
        allBookList.add(new Book("1984", "George Orwell", "Completed", "Nov 5, 2023", "Yes", "Dystopian", R.drawable.sample_illust1));
        allBookList.add(new Book("Brave Knight", "B.R. Knight", "Ongoing", "Oct 1, 2024", "Yes", "Adventure", R.drawable.sample_illust2));
    }


    // Hàm lắng nghe và thực hiện tìm kiếm
    private void setupSearchListener() {
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                filterBooks(s.toString());
            }
        });
    }

    // Hàm thực hiện lọc sách
    private void filterBooks(String query) {
        String lowerCaseQuery = query.toLowerCase(Locale.getDefault()).trim();
        List<Book> filteredList = new ArrayList<>();

        if (lowerCaseQuery.isEmpty()) {
            // Nếu ô tìm kiếm trống, hiển thị lại danh sách gợi ý hoặc danh sách trống
            bookAdapter.updateList(new ArrayList<>());
            tvStatusMessage.setText("Hãy nhập từ khóa tìm kiếm...");
            return;
        }

        for (Book book : allBookList) {
            // Tìm kiếm theo TÊN sách và TÊN TÁC GIẢ
            if (book.getName().toLowerCase(Locale.getDefault()).contains(lowerCaseQuery)) {

                filteredList.add(book);
            }
        }

        // Cập nhật Adapter của RecyclerView
        bookAdapter.updateList(filteredList);

        // Cập nhật TextView trạng thái
        if (filteredList.isEmpty()) {
            tvStatusMessage.setText("Không tìm thấy kết quả nào cho \"" + query + "\"");
        } else {
            tvStatusMessage.setText("Tìm thấy " + filteredList.size() + " cuốn sách.");
        }
    }

    // Hàm xử lý nút Back
    private void setupBackButton() {
        btnBack.setOnClickListener(v -> onBackPressed());
    }
}
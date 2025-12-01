package com.example.truyengo.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.truyengo.R;
import com.example.truyengo.commons.BannerAdapter;
import com.example.truyengo.commons.BookGridAdapter;
import com.example.truyengo.data.GetAllBook;
import com.example.truyengo.models.book.Book;
import com.example.truyengo.ui.book.BookListActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {

    private ViewPager2 viewPagerBanner;

    // Khai báo 4 RecyclerView
    private RecyclerView rvRecommended, rvNewGrid, rvComingUp, rvCompletedGrid;

    // Khai báo 4 Adapter riêng biệt (Quan trọng: không dùng chung adapter)
    private BookGridAdapter newBooksAdapter;
    private BookGridAdapter comingUpAdapter;
    private BookGridAdapter completedAdapter;

    private BannerAdapter bannerAdapter;

    private final ExecutorService executorService = Executors.newFixedThreadPool(3); // Tăng luồng lên để load nhanh hơn

    private Handler sliderHandler = new Handler(Looper.getMainLooper());
    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            if (viewPagerBanner != null && bannerAdapter != null) {
                int currentItem = viewPagerBanner.getCurrentItem();
                int totalItem = bannerAdapter.getItemCount();

                if (totalItem > 0) {
                    int nextItem = (currentItem + 1) % totalItem;
                    viewPagerBanner.setCurrentItem(nextItem);
                    sliderHandler.postDelayed(this, 5000);
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        TextView tvMoreNewBook = view.findViewById(R.id.tvMoreNewBook);
        TextView tvMoreUpComingBook = view.findViewById(R.id.tvMoreUpComingBook);
        TextView tvMoreCompletedBook= view.findViewById(R.id.tvMoreCompletedBook);

        // 1. Ánh xạ Views
        viewPagerBanner = view.findViewById(R.id.viewPagerBanner);
        rvNewGrid = view.findViewById(R.id.rvNewGrid);
        rvComingUp = view.findViewById(R.id.rvComingUpGrid);
        rvCompletedGrid = view.findViewById(R.id.rvCompletedGrid);

        // 2. Setup Adapters
        setupBanner();
        setupGrids(); // Setup chung cho 3 cái Grid 3x3

        // 3. Load dữ liệu
        loadAllData();

        tvMoreNewBook.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), BookListActivity.class);
//            intent.putExtra("TITLE", "New series");
            startActivity(intent);
        });

        tvMoreUpComingBook.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), BookListActivity.class);
//            intent.putExtra("TITLE", "Coming Up Series");
            startActivity(intent);
        });

        tvMoreCompletedBook.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), BookListActivity.class);
//            intent.putExtra("TITLE", "Completed Series");
            startActivity(intent);
        });

        return view;
    }

    private void setupBanner() {
        bannerAdapter = new BannerAdapter(getContext());
        viewPagerBanner.setAdapter(bannerAdapter);
        viewPagerBanner.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 5000);
            }
        });
    }


    private void setupGrids() {
        // Setup cho New Books
        newBooksAdapter = new BookGridAdapter(getContext());
        setupGridRecyclerView(rvNewGrid, newBooksAdapter);

        // Setup cho Coming Up
        comingUpAdapter = new BookGridAdapter(getContext());
        setupGridRecyclerView(rvComingUp, comingUpAdapter);

        // Setup cho Completed
        completedAdapter = new BookGridAdapter(getContext());
        setupGridRecyclerView(rvCompletedGrid, completedAdapter);
    }

    // Hàm hỗ trợ setup Grid giống nhau để đỡ lặp code
    private void setupGridRecyclerView(RecyclerView rv, BookGridAdapter adapter) {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        rv.setLayoutManager(layoutManager);
        rv.setNestedScrollingEnabled(false); // Quan trọng để không bị cuộn lồng
        rv.setAdapter(adapter);
    }

    private void loadAllData() {
        executorService.execute(() -> {
            try {
                GetAllBook lb = new GetAllBook();

                // 1. Lấy Truyện Mới (Cho Banner, Recommended và New Grid)
                ArrayList<Book> newBooks = lb.getBooksTruyenMoi(1);

                // 2. Lấy Truyện Sắp Ra Mắt (Cho Coming Up)
                ArrayList<Book> comingUpBooks = lb.getBooksSapRaMat(1);

                // 3. Lấy Truyện Hoàn Thành (Cho Completed)
                ArrayList<Book> completedBooks = lb.getBooksHoanThanh(1);

                // Cập nhật UI trên Main Thread
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {

                        // --- Update Banner & New ---
                        if (newBooks != null && !newBooks.isEmpty()) {
                            // Banner lấy 5 truyện đầu
                            bannerAdapter.setBooks(newBooks.size() > 5 ? newBooks.subList(0, 5) : newBooks);

                            // New Grid lấy 9 truyện
                            newBooksAdapter.setBooks(newBooks.size() > 9 ? newBooks.subList(0, 9) : newBooks);
                        }

                        // --- Update Coming Up ---
                        if (comingUpBooks != null) {
                            comingUpAdapter.setBooks(comingUpBooks.size() > 9 ? comingUpBooks.subList(0, 9) : comingUpBooks);
                        }

                        // --- Update Completed ---
                        if (completedBooks != null) {
                            completedAdapter.setBooks(completedBooks.size() > 9 ? completedBooks.subList(0, 9) : completedBooks);
                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Lỗi tải dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 5000);
    }
}
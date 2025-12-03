package com.example.truyengo.ui.main;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.truyengo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ImageView btnFav, btnHist, btnHome, btnChat, btnAcc;

    // 1. KHỞI TẠO CÁC FRAGMENT (Giữ tham chiếu để không bị tạo mới)
    private final Fragment homeFragment = new HomeFragment();
    private final Fragment favoriteFragment = new FavoriteFragment();
    private final Fragment historyFragment = new HistoryFragment();
    private final Fragment chatBotFragment = new ChatBotFragment();
    private final Fragment accountFragment = new AccountFragment();

    // 2. Biến theo dõi Fragment đang hiển thị (Mặc định là Home)
    private Fragment activeFragment = homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_main_activity);

        btnFav = findViewById(R.id.btnNavFavorite);
        btnHist = findViewById(R.id.btnNavHistory);
        btnHome = findViewById(R.id.btnNavHome);
        btnChat = findViewById(R.id.btnNavChatbot);
        btnAcc = findViewById(R.id.btnNavAccount);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, homeFragment, "HOME")
                .commit();

        updateTabUI(btnHome);

        btnFav.setOnClickListener(v -> {
            switchFragment(favoriteFragment);
            updateTabUI(btnFav);
        });

        btnHist.setOnClickListener(v -> {
            switchFragment(historyFragment);
            updateTabUI(btnHist);
        });

        btnHome.setOnClickListener(v -> {
            switchFragment(homeFragment);
            updateTabUI(btnHome);
        });

        btnChat.setOnClickListener(v -> {
            switchFragment(chatBotFragment);
            updateTabUI(btnChat);
        });

        btnAcc.setOnClickListener(v -> {
            switchFragment(accountFragment);
            updateTabUI(btnAcc);
        });
    }

    private void switchFragment(Fragment targetFragment) {
        // Nếu click vào đúng tab đang mở thì không làm gì cả
        if (activeFragment == targetFragment) return;

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        // Ẩn Fragment đang hiển thị
        transaction.hide(activeFragment);

        // Kiểm tra xem Fragment đích đã được thêm vào chưa
        if (!targetFragment.isAdded()) {
            // Nếu chưa -> Thêm mới (Add)
            transaction.add(R.id.fragment_container, targetFragment);
        } else {
            // Nếu rồi -> Chỉ cần hiện lại (Show)
            transaction.show(targetFragment);
        }

        // Cập nhật lại biến activeFragment
        activeFragment = targetFragment;

        transaction.commit();
    }

    private void updateTabUI(ImageView selectedBtn) {
        int colorUnselected = Color.parseColor("#757575");
        int colorSelected = Color.parseColor("#6BA67B");

        btnFav.setColorFilter(colorUnselected);
        btnHist.setColorFilter(colorUnselected);
        btnHome.setColorFilter(colorUnselected);
        btnChat.setColorFilter(colorUnselected);
        btnAcc.setColorFilter(colorUnselected);

        selectedBtn.setColorFilter(colorSelected);
    }
}
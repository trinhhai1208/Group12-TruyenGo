package com.example.truyengo.ui.main;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.truyengo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ImageView btnFav, btnHist, btnHome, btnChat, btnAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_main_activity);

        btnFav = findViewById(R.id.btnNavFavorite);
        btnHist = findViewById(R.id.btnNavHistory);
        btnHome = findViewById(R.id.btnNavHome);
        btnChat = findViewById(R.id.btnNavChatbot);
        btnAcc = findViewById(R.id.btnNavAccount);

        loadFragment(new HomeFragment());
        updateTabUI(btnHome);

        btnFav.setOnClickListener(v -> {
            loadFragment(new FavoriteFragment());
            updateTabUI(btnFav);
        });

        btnHist.setOnClickListener(v -> {
            loadFragment(new HistoryFragment());
            updateTabUI(btnHist);
        });

        btnHome.setOnClickListener(v -> {
            loadFragment(new HomeFragment());
            updateTabUI(btnHome);
        });

        btnChat.setOnClickListener(v -> {
            loadFragment(new ChatBotFragment());
            updateTabUI(btnChat);
        });

        btnAcc.setOnClickListener(v -> {
            loadFragment(new AccountFragment());
            updateTabUI(btnAcc);
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
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
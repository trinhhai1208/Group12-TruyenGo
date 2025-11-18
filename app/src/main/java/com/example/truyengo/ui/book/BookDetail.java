package com.example.truyengo.ui.book;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.truyengo.R;

public class BookDetail extends AppCompatActivity {
    ImageView btnBack, btnFavorite;
    TextView btnContinue;
    private TextView tabOverview, tabChapter;
    private View indicatorOverview, indicatorChapter;
    private LinearLayout layoutOverview, layoutChapter, chapterContainer;
    private GridLayout imageContainer;

    private boolean isFavorite = false;

    private int[] illustrationImages = {
            R.drawable.sample_illust1,
            R.drawable.sample_illust2,
            R.drawable.sample_illust1,
            R.drawable.sample_illust2
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnBack = findViewById(R.id.btnBack);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnContinue = findViewById(R.id.btnContinue);
        tabOverview = findViewById(R.id.tabOverview);
        tabChapter = findViewById(R.id.tabChapter);
        indicatorOverview = findViewById(R.id.indicatorOverview);
        indicatorChapter = findViewById(R.id.indicatorChapter);
        layoutOverview = findViewById(R.id.layoutOverview);
        layoutChapter = findViewById(R.id.layoutChapter);
        chapterContainer = findViewById(R.id.chapterContainer);
        imageContainer = findViewById(R.id.imageContainer);

        btnBack.setOnClickListener(v -> finish());
        btnFavorite.setOnClickListener(v -> Toast.makeText(this, "Added to favorites ❤️", Toast.LENGTH_SHORT).show());
        btnContinue.setOnClickListener(v -> Toast.makeText(this, "Continue reading...", Toast.LENGTH_SHORT).show());

        btnFavorite.setOnClickListener(v -> {
            isFavorite = !isFavorite; // đảo trạng thái

            if (isFavorite) {
                btnFavorite.setImageResource(R.drawable.ic_heart_red);
            } else {
                btnFavorite.setImageResource(R.drawable.ic_heart_black);
            }
        });

        addIllustrationImages();
        setupTabs();
        setupChapters();
    }

    private void setupTabs() {
        tabOverview.setOnClickListener(v -> showOverview());
        tabChapter.setOnClickListener(v -> showChapters());
        showOverview(); // mặc định tab đầu
    }

    private void showOverview() {
        layoutOverview.setVisibility(View.VISIBLE);
        layoutChapter.setVisibility(View.GONE);

        tabOverview.setTextColor(Color.parseColor("#3A785D"));
        indicatorOverview.setBackgroundColor(Color.parseColor("#3A785D"));

        tabChapter.setTextColor(Color.parseColor("#9E9E9E"));
        indicatorChapter.setBackgroundColor(Color.TRANSPARENT);
    }

    private void showChapters() {
        layoutOverview.setVisibility(View.GONE);
        layoutChapter.setVisibility(View.VISIBLE);

        tabChapter.setTextColor(Color.parseColor("#3A785D"));
        indicatorChapter.setBackgroundColor(Color.parseColor("#3A785D"));

        tabOverview.setTextColor(Color.parseColor("#9E9E9E"));
        indicatorOverview.setBackgroundColor(Color.TRANSPARENT);
    }

    private void addIllustrationImages() {
        for (int imgRes : illustrationImages) {
            ImageView imageView = new ImageView(this);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            int width = getResources().getDisplayMetrics().widthPixels / 2 - 48;
            params.width = width;
            params.height = width * 3 / 4;
            params.setMargins(8, 8, 8, 8);
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(imgRes);
            imageContainer.addView(imageView);
        }
    }

    private void setupChapters() {
        String[] titles = {
                "Eighty six vol 9 : Valkyrie Has Landed",
                "The Beast of Gluttony",
                "The King of Spades and Queen of Hearts’ Interminable, All Too Trivial Dispute",
                "The Ashen Battlefield",
                "The Final Chapter"
        };

        int[] thumbs = {
                R.drawable.sample_illust1,
                R.drawable.sample_illust2,
                R.drawable.sample_illust1,
                R.drawable.sample_illust2,
                R.drawable.sample_illust1
        };

        for (int i = 0; i < titles.length; i++) {
            LinearLayout item = new LinearLayout(this);
            item.setOrientation(LinearLayout.HORIZONTAL);
            item.setPadding(8, 8, 8, 8);

            ImageView thumb = new ImageView(this);
            thumb.setImageResource(thumbs[i]);
            LinearLayout.LayoutParams thumbParams = new LinearLayout.LayoutParams(120, 160);
            thumb.setLayoutParams(thumbParams);

            LinearLayout textContainer = new LinearLayout(this);
            textContainer.setOrientation(LinearLayout.VERTICAL);
            textContainer.setGravity(LinearLayout.VERTICAL);
            textContainer.setPadding(16, 0, 0, 0);

            TextView index = new TextView(this);
            index.setText("Chapter " + (i + 1));
            index.setTextColor(Color.BLACK);
            index.setTextSize(14);

            TextView title = new TextView(this);
            title.setText(titles[i]);
            title.setTextColor(Color.DKGRAY);
            title.setTextSize(13);

            textContainer.addView(index);
            textContainer.addView(title);
            item.addView(thumb);
            item.addView(textContainer);
            chapterContainer.addView(item);
        }
    }
}
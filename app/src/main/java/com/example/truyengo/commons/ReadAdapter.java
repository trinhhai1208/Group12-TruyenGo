package com.example.truyengo.commons;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.truyengo.R;

import java.util.ArrayList;
import java.util.List;

public class ReadAdapter extends RecyclerView.Adapter<ReadAdapter.ImageViewHolder> {

    private Context context;
    private List<String> imageUrls; // Danh sách link ảnh
    private String chapterPath; // Đường dẫn cơ sở của ảnh
    private final String BASE_IMAGE_URL = "https://sv1.otruyencdn.com/";

    public ReadAdapter(Context context) {
        this.context = context;
        this.imageUrls = new ArrayList<>();
    }

    public void setImages(List<String> urls, String path) {
        this.imageUrls = urls;
        this.chapterPath = path;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.book_read_image_activity, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String imageFile = imageUrls.get(position);
        // Tạo URL đầy đủ cho ảnh
        String fullUrl = BASE_IMAGE_URL + chapterPath + "/" + imageFile;

        Glide.with(context)
                .load(fullUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache ảnh để lướt mượt hơn
                .placeholder(android.R.color.darker_gray) // Ảnh chờ
                .into(holder.imgComicPage);
    }

    @Override
    public int getItemCount() {
        return imageUrls != null ? imageUrls.size() : 0;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imgComicPage;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imgComicPage = itemView.findViewById(R.id.imgComicPage);
        }
    }
}
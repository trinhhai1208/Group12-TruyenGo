package com.example.truyengo.commons;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.truyengo.R;
import com.example.truyengo.models.chapter.AllChapters;
import com.example.truyengo.ui.book.ReadActivity;

import java.util.ArrayList;
import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder> {

    private Context context;
    private List<AllChapters> chapterList;
    private String bookThumbUrl;
    private String bookName; // 1. Thêm biến lưu tên truyện

    // 2. Cập nhật Constructor nhận thêm bookName
    public ChapterAdapter(Context context, String bookThumbUrl, String bookName) {
        this.context = context;
        this.bookThumbUrl = bookThumbUrl;
        this.bookName = bookName;
        this.chapterList = new ArrayList<>();
    }

    public void setChapters(List<AllChapters> chapters) {
        this.chapterList = chapters;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Lưu ý: Đảm bảo bạn đã tạo file layout item_chapter.xml như hướng dẫn trước
        View view = LayoutInflater.from(context).inflate(R.layout.book_list_chapter_activity, parent, false);
        return new ChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        AllChapters chapter = chapterList.get(position);

        // Hiển thị tên chương
        holder.tvChapterName.setText("Chapter " + chapter.getChapter_name());

        // Hiển thị tên file hoặc title (nếu có)
        holder.tvFileName.setText(chapter.getFilename());

        // Load ảnh bìa (Ghép link nếu cần)
        String fullUrl = "https://img.otruyenapi.com/uploads/comics/" + bookThumbUrl;
        Glide.with(context).load(fullUrl).placeholder(R.drawable.bg_button_continue).into(holder.imgChapterThumb);

        // 3. Xử lý sự kiện click chuyển sang màn hình đọc
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReadActivity.class);

            // Truyền danh sách tất cả các chương (Để nút Next/Prev hoạt động)
            // Lưu ý: Class AllChapters phải implements Serializable
            intent.putExtra("ALL_CHAPTERS", (ArrayList<AllChapters>) chapterList);

            // Truyền vị trí chương hiện tại đang bấm
            intent.putExtra("CURRENT_INDEX", position);

            // Truyền tên truyện để hiển thị trên Header
            intent.putExtra("BOOK_NAME", bookName);

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return chapterList != null ? chapterList.size() : 0;
    }

    public class ChapterViewHolder extends RecyclerView.ViewHolder {
        ImageView imgChapterThumb;
        TextView tvChapterName, tvFileName;

        public ChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ View từ file item_chapter.xml
            imgChapterThumb = itemView.findViewById(R.id.imgChapterThumb);
            tvChapterName = itemView.findViewById(R.id.tvChapterName); // Hoặc tvChapterNum tùy ID bạn đặt
            tvFileName = itemView.findViewById(R.id.tvFileName); // Hoặc tvChapterTitle tùy ID bạn đặt
        }
    }
}
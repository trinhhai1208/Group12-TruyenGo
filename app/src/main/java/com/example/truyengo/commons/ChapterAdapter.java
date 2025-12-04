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
    private String bookName;

    // 1. Khai báo Interface để lắng nghe sự kiện click
    public interface OnChapterClickListener {
        void onChapterClick(AllChapters chapter);
    }

    private OnChapterClickListener listener;

    // 2. Cập nhật Constructor nhận thêm Listener
    public ChapterAdapter(Context context, String bookThumbUrl, String bookName, OnChapterClickListener listener) {
        this.context = context;
        this.bookThumbUrl = bookThumbUrl;
        this.bookName = bookName;
        this.listener = listener; // Lưu listener lại
        this.chapterList = new ArrayList<>();
    }

    // Constructor cũ (nếu bạn muốn giữ tương thích ngược, nhưng tốt nhất nên dùng cái trên)
    public ChapterAdapter(Context context, String bookThumbUrl, String bookName) {
        this(context, bookThumbUrl, bookName, null);
    }

    public void setChapters(List<AllChapters> chapters) {
        this.chapterList = chapters;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.book_list_chapter_activity, parent, false);
        return new ChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        AllChapters chapter = chapterList.get(position);

        // Hiển thị tên chương
        holder.tvChapterName.setText("Chapter " + chapter.getChapter_name());

        // Hiển thị tên file hoặc title
        holder.tvFileName.setText(chapter.getFilename());

        // Load ảnh bìa
        String fullUrl = "https://img.otruyenapi.com/uploads/comics/" + bookThumbUrl;
        Glide.with(context).load(fullUrl).placeholder(R.drawable.bg_button_continue).into(holder.imgChapterThumb);

        // 3. Xử lý sự kiện click
        holder.itemView.setOnClickListener(v -> {
            // A. Gọi listener để Activity biết mà lưu lịch sử API
            if (listener != null) {
                listener.onChapterClick(chapter);
            }

            // B. Chuyển sang màn hình đọc (Logic cũ của bạn)
            Intent intent = new Intent(context, ReadActivity.class);

            // Truyền danh sách chương để Next/Prev
            intent.putExtra("ALL_CHAPTERS", (ArrayList<AllChapters>) chapterList);

            // Truyền vị trí hiện tại
            intent.putExtra("CURRENT_INDEX", position);

            // Truyền tên truyện
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
            imgChapterThumb = itemView.findViewById(R.id.imgChapterThumb);
            tvChapterName = itemView.findViewById(R.id.tvChapterName);
            tvFileName = itemView.findViewById(R.id.tvFileName);
        }
    }
}
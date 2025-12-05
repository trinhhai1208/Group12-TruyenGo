package com.example.truyengo.commons;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder> {

    private Context context;
    private List<AllChapters> chapterList;
    private String bookThumbUrl;
    // private String bookName; // Không cần dùng biến này nữa vì Adapter không chuyển màn hình

    // Interface lắng nghe sự kiện
    public interface OnChapterClickListener {
        void onChapterClick(AllChapters chapter);
    }

    private OnChapterClickListener listener;

    public ChapterAdapter(Context context, String bookThumbUrl, String bookName, OnChapterClickListener listener) {
        this.context = context;
        this.bookThumbUrl = bookThumbUrl;
        // this.bookName = bookName;
        this.listener = listener;
        this.chapterList = new ArrayList<>();
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

        holder.tvChapterName.setText("Chapter " + chapter.getChapter_name());
        holder.tvFileName.setText(chapter.getFilename());

        String fullUrl = "https://img.otruyenapi.com/uploads/comics/" + bookThumbUrl;
        Glide.with(context).load(fullUrl).placeholder(R.drawable.bg_button_continue).into(holder.imgChapterThumb);

        // --- SỬA TẠI ĐÂY ---
        holder.itemView.setOnClickListener(v -> {
            // Chỉ gọi listener để Activity xử lý
            if (listener != null) {
                listener.onChapterClick(chapter);
            }
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
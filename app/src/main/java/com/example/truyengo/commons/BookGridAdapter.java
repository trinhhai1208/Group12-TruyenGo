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
import com.example.truyengo.models.book.Book;
import com.example.truyengo.ui.book.BookDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class BookGridAdapter extends RecyclerView.Adapter<BookGridAdapter.BookViewHolder> {

    private Context context;
    private List<Book> bookList;
    private final String BASE_URL = "https://img.otruyenapi.com/uploads/comics/";

    public BookGridAdapter(Context context) {
        this.context = context;
        this.bookList = new ArrayList<>();
    }

    public void setBooks(List<Book> books) {
        this.bookList = books;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_item_book_grid, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        if (book == null) return;

        holder.tvTitle.setText(book.getName());

        String fullImageUrl = BASE_URL + book.getThumbnail();

        Glide.with(context)
                .load(fullImageUrl)
                .placeholder(android.R.color.darker_gray)
                .error(android.R.color.holo_red_light)
                .into(holder.imgCover);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookDetailActivity.class);
            intent.putExtra("BOOK_SLUG", book.getSlug());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (bookList != null) return bookList.size();
        return 0;
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCover;
        TextView tvTitle;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCover = itemView.findViewById(R.id.imgGridCover);
            tvTitle = itemView.findViewById(R.id.tvBookTitle);
        }
    }

    public void addBooks(List<Book> newBooks) {
        int startPosition = this.bookList.size();
        this.bookList.addAll(newBooks);
        notifyItemRangeInserted(startPosition, newBooks.size());
    }
}
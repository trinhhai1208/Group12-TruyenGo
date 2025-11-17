package com.example.truyengo.commons;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.truyengo.R;
import com.example.truyengo.models.book.Book;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private List<Book> bookList;

    // Constructor để nhận dữ liệu
    public BookAdapter(List<Book> bookList) {
        this.bookList = bookList;
    }

    // Class ViewHolder: Ánh xạ View
    public static class BookViewHolder extends RecyclerView.ViewHolder {
        public ImageView bookImage;
        public TextView bookName;
        public TextView bookAuthor;
        public TextView bookState;
        public TextView bookDate;
        public TextView bookNewChapter;
        public TextView bookGenre;

        public BookViewHolder(View itemView) {
            super(itemView);
            // Thay R.id.bookImage bằng ID của ImageView trong list_item_book.xml
            bookImage = itemView.findViewById(R.id.bookImage);
            bookName = itemView.findViewById(R.id.bookName);
            bookAuthor = itemView.findViewById(R.id.bookAuthor);
            bookState = itemView.findViewById(R.id.bookState);
            bookDate = itemView.findViewById(R.id.bookDate);
            bookNewChapter = itemView.findViewById(R.id.bookNewChapter);
            bookGenre = itemView.findViewById(R.id.bookGenre);
        }
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // "Thổi phồng" layout item (list_item_book.xml)
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        // Gán dữ liệu cho từng item
        Book currentBook = bookList.get(position);

//        holder.bookImage.setImageResource(currentBook());
//        holder.bookName.setText(currentBook.getName());
//        holder.bookAuthor.setText(currentBook.getAuthor());
//        holder.bookState.setText(currentBook.getState());
//        holder.bookDate.setText(currentBook.getLastUpdated());
//        holder.bookNewChapter.setText(currentBook.getNewChapter());
//        holder.bookGenre.setText(currentBook.getGenre());

        // Bạn có thể thêm xử lý click cho item ở đây (ví dụ: mở chi tiết sách)
        holder.itemView.setOnClickListener(v -> {
            // Logic khi người dùng nhấn vào sách
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    // HÀM QUAN TRỌNG: Cập nhật danh sách khi tìm kiếm
    public void updateList(List<Book> newList) {
        bookList = newList;
        notifyDataSetChanged(); // Báo cho RecyclerView vẽ lại giao diện
    }
}
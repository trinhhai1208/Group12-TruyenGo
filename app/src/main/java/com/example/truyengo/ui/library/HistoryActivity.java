package com.example.truyengo.ui.library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.truyengo.R;
import com.example.truyengo.models.book.Book;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private LinearLayout bookContainer;
    private List<Book> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_history_activity);

        bookContainer = findViewById(R.id.book_container);

        createBookData();

        populateBookList();
    }

    private void createBookData() {
        bookList = new ArrayList<>();

//        bookList.add(new Book("Eighty Six", "Asato Asato", "Loading...", "Feb 22, 2022", "No", "Action | Sci-fi", R.drawable.sample_illust1));
//        bookList.add(new Book("Book Title 2", "Author 2", "Completed", "Jan 1, 2023", "Yes", "Fantasy", R.drawable.sample_illust1));
//        bookList.add(new Book("Book Title 3", "Author 3", "Ongoing", "Mar 15, 2023", "No", "Romance", R.drawable.sample_illust1));
//        bookList.add(new Book("Book Title 4", "Author 4", "Hiatus", "Dec 10, 2022", "No", "Mystery", R.drawable.sample_illust1));
//        bookList.add(new Book("Book Title 5", "Author 5", "Completed", "Nov 5, 2023", "Yes", "Slice of Life", R.drawable.sample_illust1));
//        bookList.add(new Book("Book Title 6", "Author 6", "Ongoing", "Oct 30, 2023", "No", "Horror", R.drawable.sample_illust1));
//        bookList.add(new Book("Book Title 7", "Author 7", "Ongoing", "Sep 12, 2023", "Yes", "Comedy", R.drawable.sample_illust1));
//        bookList.add(new Book("Book Title 8", "Author 8", "Completed", "Aug 20, 2023", "No", "Drama", R.drawable.sample_illust1));
//        bookList.add(new Book("Book Title 9", "Author 9", "Ongoing", "Jul 7, 2023", "Yes", "Adventure", R.drawable.sample_illust1));
//        bookList.add(new Book("Book Title 10", "Author 10", "Hiatus", "Jun 1, 2023", "No", "Mecha", R.drawable.sample_illust1));
    }

    private void populateBookList() {
        LayoutInflater inflater = LayoutInflater.from(this);

        for (Book book : bookList) {
            View itemView = inflater.inflate(R.layout.book_list_item_book_activity, bookContainer, false);

            ImageView bookImage = itemView.findViewById(R.id.bookImage);
            TextView bookName = itemView.findViewById(R.id.bookName);
            TextView bookAuthor = itemView.findViewById(R.id.bookAuthor);
            TextView bookState = itemView.findViewById(R.id.bookState);
            TextView bookDate = itemView.findViewById(R.id.bookDate);
            TextView bookNewChapter = itemView.findViewById(R.id.bookNewChapter);
            TextView bookGenre = itemView.findViewById(R.id.bookGenre);

//            bookImage.setImageResource(book.getImageResId());
//            bookName.setText(book.getName());
//            bookAuthor.setText(book.getAuthor());
//            bookState.setText(book.getState());
//            bookDate.setText(book.getLastUpdated());
//            bookNewChapter.setText(book.getNewChapter());
//            bookGenre.setText(book.getGenre());

            bookContainer.addView(itemView);
        }
    }
}
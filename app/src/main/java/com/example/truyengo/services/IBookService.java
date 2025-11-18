package com.example.truyengo.services;

import android.graphics.Color;

import com.example.truyengo.models.book.Book;

import org.bson.types.ObjectId;

import java.util.ArrayList;

public interface IBookService {
    void storageBookToUser(ObjectId bookId);

    int getLastReadIndexChapter(ObjectId bookId);

    void saveLastReadChapter(ObjectId bookId, double chapter);

    boolean checkIfExitBookInUser(ObjectId bookId);

    void updateDateToRead(ObjectId bookId);

    void insertBookToDB(Book book);

    Book getBookById(ObjectId bookId);

    ArrayList<Book> getBooksByPage(int pageNumber, int pageSize);

    void toggleFavorite(ObjectId bookId, Color level);

    ArrayList<Book> getFavoritesByPage(int pageNumber, int pageSize);

    Color checkFavorite(ObjectId bookId);
}

package com.example.truyengo.models.user;

import org.bson.types.ObjectId;

import java.time.LocalDateTime;

public class UserHistoryBooks {
    private ObjectId userId;
    private ObjectId bookId;
    private int lastReadChapter;
    private LocalDateTime lastReadDate;

    public UserHistoryBooks() {
        this.lastReadChapter = 1;
    }

    public UserHistoryBooks(ObjectId userId, ObjectId bookId, int lastReadChapter, LocalDateTime lastReadDate) {
        this.userId = userId;
        this.bookId = bookId;
        this.lastReadChapter = lastReadChapter;
        this.lastReadDate = lastReadDate;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public ObjectId getBookId() {
        return bookId;
    }

    public void setBookId(ObjectId bookId) {
        this.bookId = bookId;
    }

    public int getLastReadChapter() {
        return lastReadChapter;
    }

    public void setLastReadChapter(int lastReadChapter) {
        this.lastReadChapter = lastReadChapter;
    }

    public LocalDateTime getLastReadDate() {
        return lastReadDate;
    }

    public void setLastReadDate(LocalDateTime lastReadDate) {
        this.lastReadDate = lastReadDate;
    }
}

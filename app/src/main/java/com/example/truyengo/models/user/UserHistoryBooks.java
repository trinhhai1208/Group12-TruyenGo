package com.example.truyengo.models.user;

import lombok.Getter;
import lombok.Setter;

import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@Getter
@Setter
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
}

package com.example.truyengo.models.user;

import org.bson.types.ObjectId;

import java.time.LocalDateTime;

public class UserFavouriteBook {
    private ObjectId userId;
    private ObjectId bookId;
    private boolean favourite;
    private LocalDateTime lastAddFavDate;

    public UserFavouriteBook() {
    }

    public UserFavouriteBook(ObjectId userId, ObjectId bookId, boolean favourite, LocalDateTime lastAddFavDate) {
        this.userId = userId;
        this.bookId = bookId;
        this.favourite = favourite;
        this.lastAddFavDate = lastAddFavDate;
    }

    public boolean getFavourite() {
        return favourite;
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

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public LocalDateTime getLastAddFavDate() {
        return lastAddFavDate;
    }

    public void setLastAddFavDate(LocalDateTime lastAddFavDate) {
        this.lastAddFavDate = lastAddFavDate;
    }
}

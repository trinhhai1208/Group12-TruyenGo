package com.example.truyengo.models.user;

import lombok.Getter;
import lombok.Setter;

import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@Getter
@Setter
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
}

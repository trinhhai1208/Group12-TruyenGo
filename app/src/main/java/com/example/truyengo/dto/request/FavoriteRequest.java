package com.example.truyengo.dto.request;

public class FavoriteRequest {
    private String userId;
    private String bookId;

    public FavoriteRequest(String userId, String bookId) {
        this.userId = userId;
        this.bookId = bookId;
    }
}
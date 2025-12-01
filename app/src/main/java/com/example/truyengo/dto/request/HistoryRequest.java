package com.example.truyengo.dto.request;

public class HistoryRequest {
    private String userId;
    private String bookId;
    private String chapterName;
    private String chapterApiData;

    public HistoryRequest(String userId, String bookId, String chapterName, String chapterApiData) {
        this.userId = userId;
        this.bookId = bookId;
        this.chapterName = chapterName;
        this.chapterApiData = chapterApiData;
    }
}
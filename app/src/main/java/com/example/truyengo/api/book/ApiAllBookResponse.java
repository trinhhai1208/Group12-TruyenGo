package com.example.truyengo.api.book;

public class ApiAllBookResponse {
    private String status;
    private String message;
    private ApiAllBookData data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ApiAllBookData getData() {
        return data;
    }

    public void setData(ApiAllBookData data) {
        this.data = data;
    }
}

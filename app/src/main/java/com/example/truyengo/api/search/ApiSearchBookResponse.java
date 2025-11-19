package com.example.truyengo.api.search;

public class ApiSearchBookResponse {
    private String status;
    private String message;
    private ApiSearchBookData data;

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

    public ApiSearchBookData getData() {
        return data;
    }

    public void setData(ApiSearchBookData data) {
        this.data = data;
    }
}

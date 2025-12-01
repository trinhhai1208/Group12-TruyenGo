package com.example.truyengo.dto.response;

import com.google.gson.annotations.SerializedName;

public class BaseResponse<T> {
    @SerializedName("status")
    private String status;

    @SerializedName("message") // Đảm bảo tên trường này khớp với JSON trả về
    private String message;

    @SerializedName("data")
    private T data;

    public boolean isSuccess() {
        return "SUCCESS".equalsIgnoreCase(status) || "OK".equalsIgnoreCase(status);
    }

    public T getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }

    // THÊM HÀM NÀY ĐỂ SỬA LỖI
    public String getMessage() {
        return message;
    }
}
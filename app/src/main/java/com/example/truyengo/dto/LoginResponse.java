package com.example.truyengo.dto;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

@Getter
public class LoginResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private LoginData data;

    public static class LoginData {
        @Getter
        @SerializedName("status")
        private String status;

        @Getter
        @SerializedName("message")
        private String message;

        @Getter
        @SerializedName("accessToken")
        private String accessToken;

        @SerializedName("refreshToken")
        private String refreshToken;

        @SerializedName("id")
        private String userId;
    }
}
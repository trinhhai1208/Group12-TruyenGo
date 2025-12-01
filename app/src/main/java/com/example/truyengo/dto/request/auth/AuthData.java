package com.example.truyengo.dto.request.auth;

import com.google.gson.annotations.SerializedName;

public class AuthData {
    @SerializedName("accessToken")
    private String accessToken;

    @SerializedName("refreshToken")
    private String refreshToken;

    @SerializedName("id")
    private String id;

    @SerializedName("tokenType")
    private String tokenType;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private String status;

    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public String getId() { return id; }
    public String getTokenType() { return tokenType; }
    public String getMessage() { return message; }
}
package com.example.truyengo.dto.request.auth;

import com.google.gson.annotations.SerializedName;

public class LoginRequestDto {
    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    public LoginRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

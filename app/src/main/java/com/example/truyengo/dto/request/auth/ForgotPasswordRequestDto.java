package com.example.truyengo.dto.request.auth;

import com.google.gson.annotations.SerializedName;

public class ForgotPasswordRequestDto {
    @SerializedName("email")
    private String email;

    public ForgotPasswordRequestDto(String email) {
        this.email = email;
    }
}
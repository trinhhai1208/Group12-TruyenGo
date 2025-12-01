package com.example.truyengo.dto.request.auth;

import com.google.gson.annotations.SerializedName;

public class ResetPasswordRequestDto {
    @SerializedName("email")
    private String email;

    @SerializedName("newPassword")
    private String newPassword;

    @SerializedName("reEnterPassword")
    private String reEnterPassword;

    public ResetPasswordRequestDto(String email, String newPassword, String reEnterPassword) {
        this.email = email;
        this.newPassword = newPassword;
        this.reEnterPassword = reEnterPassword;
    }
}
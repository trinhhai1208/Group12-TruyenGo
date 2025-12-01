package com.example.truyengo.dto.request.auth;

import com.google.gson.annotations.SerializedName;

public class VerifyOtpRequestDto {
    @SerializedName("email")
    private String email;

    @SerializedName("otp")
    private String otp;

    public VerifyOtpRequestDto(String email, String otp) {
        this.email = email;
        this.otp = otp;
    }
}

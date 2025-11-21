package com.example.truyengo.services;

import com.example.truyengo.dto.LoginRequest;
import com.example.truyengo.dto.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("api/v1/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);
}

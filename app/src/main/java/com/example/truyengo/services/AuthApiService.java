package com.example.truyengo.services;

import com.example.truyengo.dto.request.FavoriteRequest;
import com.example.truyengo.dto.request.HistoryRequest;
import com.example.truyengo.dto.request.auth.AuthData;
import com.example.truyengo.dto.request.auth.ForgotPasswordRequestDto;
import com.example.truyengo.dto.request.auth.LoginRequestDto;
import com.example.truyengo.dto.request.auth.RegisterRequestDto;
import com.example.truyengo.dto.request.auth.ResetPasswordRequestDto;
import com.example.truyengo.dto.request.auth.VerifyOtpRequestDto;
import com.example.truyengo.dto.response.BaseResponse;
import com.example.truyengo.dto.response.UserResponseDto;
import com.example.truyengo.models.book.Book;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AuthApiService {
    @POST("api/v1/auth/login")
    Call<BaseResponse<AuthData>> login(@Body LoginRequestDto request);

    @POST("api/v1/auth/register")
    Call<BaseResponse<String>> register(@Body RegisterRequestDto request);

    @POST("api/v1/auth/verify-otp")
    Call<BaseResponse<UserResponseDto>> verifyOtp(@Body VerifyOtpRequestDto request);

    @POST("api/v1/auth/forgot-password")
    Call<BaseResponse<String>> forgotPassword(@Body ForgotPasswordRequestDto request);

    @POST("api/v1/auth/verify-otp-to-reset-password")
    Call<BaseResponse<Boolean>> verifyOtpResetPassword(@Body VerifyOtpRequestDto request);

    @POST("api/v1/auth/reset-password")
    Call<BaseResponse<UserResponseDto>> resetPassword(@Body ResetPasswordRequestDto request);

    // User
    @GET("api/v1/user/profile")
    Call<BaseResponse<UserResponseDto>> getUserProfile(@Header("Authorization") String token);

    // Book
    @POST("api/v1/user/history")
    Call<String> addToHistory(@Body HistoryRequest request);

    @GET("api/v1/user/history/{userId}")
    Call<List<Book>> getHistory(@Path("userId") String userId);

    @POST("api/v1/user/favorite")
    Call<Void> toggleFavorite(@Body FavoriteRequest request);

    @GET("api/v1/user/favorite/{userId}")
    Call<List<Book>> getFavorites(@Path("userId")String userId);

    @GET("api/v1/user/favorite/check")
    Call<Boolean> checkIsFavorite(@Query("userId") String userId, @Query("bookId") String bookId);
}

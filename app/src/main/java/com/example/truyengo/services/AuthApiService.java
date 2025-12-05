package com.example.truyengo.services;

import com.example.truyengo.dto.request.auth.AuthData;
import com.example.truyengo.dto.request.auth.ForgotPasswordRequestDto;
import com.example.truyengo.dto.request.auth.LoginRequestDto;
import com.example.truyengo.dto.request.auth.RegisterRequestDto;
import com.example.truyengo.dto.request.auth.ResetPasswordRequestDto;
import com.example.truyengo.dto.request.auth.VerifyOtpRequestDto;
import com.example.truyengo.dto.response.BaseResponse;
import com.example.truyengo.dto.response.HistoryAndFavoriteResponseDto;
import com.example.truyengo.dto.response.LastReadHistoryResponseDto;
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
    Call<BaseResponse<String>> addToHistory(
            @Header("Authorization") String token,
            @Query("userId") String userId,
            @Query("slug") String slug,
            @Query("chapter") int chapter
    );

    @GET("api/v1/user/history/book")
    Call<BaseResponse<LastReadHistoryResponseDto>> getLastReadChapter(
            @Header("Authorization") String token,
            @Query("userId") String userId,
            @Query("slug") String slug
    );

    @GET("api/v1/user/history/{userId}")
    Call<BaseResponse<List<HistoryAndFavoriteResponseDto>>> getHistory(
            @Header("Authorization") String token,
            @Path("userId") String userId
    );

    @POST("api/v1/user/favorite")
    Call<BaseResponse<Boolean>> toggleFavorite(
            @Header("Authorization") String token,
            @Query("userId") String userId,
            @Query("slug") String slug
    );

    @GET("api/v1/user/favorite/{userId}")
    Call<BaseResponse<List<HistoryAndFavoriteResponseDto >>> getFavorites(
            @Header("Authorization") String token,
            @Path("userId") String userId
    );

    @GET("api/v1/user/favorite/check")
    Call<BaseResponse<Boolean>> checkIsFavorite(
            @Header("Authorization") String token,
            @Query("userId") String userId,
            @Query("slug") String slug
    );
}

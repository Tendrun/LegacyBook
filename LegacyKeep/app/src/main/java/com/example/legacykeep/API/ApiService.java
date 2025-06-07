package com.example.legacykeep.API;

import com.example.legacykeep.DTO.AuthResponse;
import com.example.legacykeep.DTO.LoginRequest;
import com.example.legacykeep.DTO.RegisterRequest;
import com.example.legacykeep.DTO.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/api/auth/login")
    Call<AuthResponse> login(@Body LoginRequest request);

    @POST("/api/auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    @GET("/api/auth/hello")
    Call<String> getHello(@Header("Authorization") String token);

}

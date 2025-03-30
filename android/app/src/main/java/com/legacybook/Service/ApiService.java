package com.legacybook.Service;

import retrofit2.Call;

import com.legacybook.Model.LoginRequest;
import com.legacybook.Model.LoginResponse;
import com.legacybook.Model.RegisterRequest;

import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("auth/register")
    Call<Void> register(@Body RegisterRequest request);

    // Add more endpoints as needed for MainActivity
}

package com.example.my_app.MyApi;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("/api/register")
    Call<ApiResponse> register(@Body RegisterRequest registerRequest);

    @POST("/api/login")
    Call<ApiResponse> login(@Body LoginRequest loginRequest);
}

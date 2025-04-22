package com.example.text.Apis;

import com.example.text.dataModel.LoginRequest;
import com.example.text.dataModel.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface ApiService {
    @POST("/account/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("/file/getAvatar")
    Call<AvatarResponse> getAvatar(@Body AvatarRequest request);
}
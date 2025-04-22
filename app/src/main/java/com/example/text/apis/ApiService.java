package com.example.text.apis;

import com.example.text.dataModel.request.AvatarRequest;
import com.example.text.dataModel.response.AvatarResponse;
import com.example.text.dataModel.request.LoginRequest;
import com.example.text.dataModel.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;


public interface ApiService {
    @POST("/account/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("/file/getAvatar")
    Call<AvatarResponse> getAvatar(@Body AvatarRequest request);

//    @POST("/contact/getContact")
//    Call<FriendListResponse> getFriendList(@Header("token") String token);
//    Call<String> getAvatar(@Body AvatarRequest request);
}
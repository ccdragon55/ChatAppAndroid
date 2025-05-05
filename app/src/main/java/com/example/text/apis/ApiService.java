package com.example.text.apis;

import com.example.text.dataModel.request.AddFriendRequest;
import com.example.text.dataModel.request.UserIdRequest;
import com.example.text.dataModel.request.ChangePasswordRequest;
import com.example.text.dataModel.request.CreateGroupRequest;
import com.example.text.dataModel.request.DealWithApplyRequest;
import com.example.text.dataModel.request.DeleteContactRequest;
import com.example.text.dataModel.request.FriendInfoRequest;
import com.example.text.dataModel.request.RegisterRequest;
import com.example.text.dataModel.request.SendMessageRequest;
import com.example.text.dataModel.request.UpdateInfoRequest;
import com.example.text.dataModel.response.AvatarResponse;
import com.example.text.dataModel.request.LoginRequest;
import com.example.text.dataModel.response.CreateGroupResponse;
import com.example.text.dataModel.response.FetchApplyInfoResponse;
import com.example.text.dataModel.response.FetchContactsResponse;
import com.example.text.dataModel.response.FriendInfoResponse;
import com.example.text.dataModel.response.LoginResponse;
import com.example.text.dataModel.response.RegisterResponse;
import com.example.text.dataModel.response.SendCheckCodeResponse;
import com.example.text.dataModel.response.SendMessageResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface ApiService {
    @POST("/account/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("/file/getAvatar")
    Call<AvatarResponse> getAvatar(@Body UserIdRequest request);

    @GET("/account/checkcode")
    Call<SendCheckCodeResponse> sendVerificationCode();

    @POST("/account/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    @POST("/chat/sendMessage")
    Call<SendMessageResponse> sendMessage(@Body SendMessageRequest request, @Header("token") String token);

    @POST("/account/getUser")
    Call<FriendInfoResponse> showFriendInfo(@Body FriendInfoRequest request, @Header("token") String token);

    @POST("/contact/delContact")
    Call<Void> deleteContact(@Body DeleteContactRequest request, @Header("token") String token);

    @Multipart
    @POST("file/uploadAvatar")
    Call<AvatarResponse> uploadAvatar(@Part MultipartBody.Part file, @Header("token") String token );

    @POST("/account/update")
    Call<Void> uploadUserInfo(@Body UpdateInfoRequest request, @Header("token") String token );

    @POST("/account/updatePassword")
    Call<Void> updatePassword(@Body ChangePasswordRequest request, @Header("token") String token );

    @POST("/group/saveGroup")
    Call<CreateGroupResponse> saveGroup(@Body CreateGroupRequest request, @Header("token") String token );

    @POST("/contact/applyAdd")
    Call<Void> applyAdd(@Body AddFriendRequest request, @Header("token") String token );

    @POST("/contact/dealWithApply")
    Call<Void> dealWithApply(@Body DealWithApplyRequest request, @Header("token") String token );

    @POST("/contact/searchApply")
    Call<FetchApplyInfoResponse> searchApply(@Body UserIdRequest request, @Header("token") String token );

    //TODO
    @POST("/contact/getContact")
    Call<FetchContactsResponse> getContact(@Header("token") String token );

//    @POST("/contact/getContact")
//    Call<FriendListResponse> getFriendList(@Header("token") String token);
//    Call<String> getAvatar(@Body AvatarRequest request);
}
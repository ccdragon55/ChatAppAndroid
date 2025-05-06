package com.example.text.activities.profileActivities;

import static com.example.text.activities.loginActivities.LoginActivity.md5;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.text.R;
import com.example.text.apis.ApiService;
import com.example.text.dataModel.FriendInfo;
import com.example.text.dataModel.request.ChangePasswordRequest;
import com.example.text.dataModel.request.FriendInfoRequest;
import com.example.text.dataModel.request.UpdateInfoRequest;
import com.example.text.dataModel.response.AvatarResponse;
import com.example.text.dataModel.response.FriendInfoResponse;
import com.example.text.database.AvatarModel;
import com.example.text.retrofits.RetrofitClient;
import com.example.text.utils.Store;
import com.google.android.material.button.MaterialButton;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    private ImageButton btnBack;
    private ImageView ivAvatar;
    private TextView tvUserName;
    private TextView tvUserId;
    private TextView tvGender;
    private TextView tvEmail;
    private TextView tvSignature;
    private MaterialButton btnChangeProfile;
    private MaterialButton btnChangePassword;
    private String userId;
    private FriendInfo info;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btnBack=findViewById(R.id.btn_back);
        ivAvatar=findViewById(R.id.iv_avatar);
        tvUserName=findViewById(R.id.tv_username);
        tvUserId=findViewById(R.id.tv_userId);
        tvGender=findViewById(R.id.tv_gender);
        tvEmail=findViewById(R.id.tv_email);
        tvSignature=findViewById(R.id.tv_signature);
        btnChangeProfile=findViewById(R.id.btn_changeProfile);
        btnChangePassword=findViewById(R.id.btn_changePassword);

        userId=Store.getInstance(getApplicationContext()).getUserId();
        showInfo(userId);

//        tvUserName.setText("昵称:"+"wjc");
//        tvUserId.setText("ID:"+"123456");
//        tvGender.setText("性别:"+"男");
//        tvEmail.setText("邮箱:"+"qq.com");
//        tvSignature.setText("个性签名:"+"我是wjccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc");

        btnBack.setOnClickListener(v->finish());
        btnChangeProfile.setOnClickListener(v->showUpdateDialog());
        btnChangePassword.setOnClickListener(v->showChangePasswordDialog());
//        btnSendMessage.setOnClickListener(v -> finish());
//        btnDeleteFriend.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void showInfo(String contactId){
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<FriendInfoResponse> call = apiService.showFriendInfo(new FriendInfoRequest(contactId), Store.getInstance(getApplicationContext()).getData("token"));

        call.enqueue(new Callback<FriendInfoResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<FriendInfoResponse> call, @NonNull Response<FriendInfoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    info=response.body().getData();
                    tvUserName.setText("昵称:"+info.getNickName());
                    tvUserId.setText("ID:"+info.getUserId());
                    tvGender.setText("性别:"+info.getSex());
                    tvEmail.setText("邮箱:"+info.getEmail());
                    tvSignature.setText("个性签名:"+info.getPersonalSignature());
                    runOnUiThread(() -> {
                        Glide.with(ProfileActivity.this)
                                .load(info.getAvatarUrl())
                                .apply(RequestOptions.circleCropTransform())
                                .into(ivAvatar);
                    });
                } else {
                    Toast.makeText(ProfileActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<FriendInfoResponse> call, @NonNull Throwable t) {
                Toast.makeText(ProfileActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showUpdateDialog() {
        ModifyProfileDialogFragment dialog = new ModifyProfileDialogFragment();

        // 正确实例化接口（通过匿名内部类）
        dialog.setOnProfileModifiedListener(new ModifyProfileDialogFragment.OnProfileModifiedListener() {
            @Override
            public void onProfileModified(String nickname, int gender, String email, String signature, File avatar) {
                // 处理数据提交
                AddRecord(nickname, gender, email, signature, avatar);
            }
        });

        dialog.show(getSupportFragmentManager(), "修改个人资料");
    }

    private void AddRecord(String nickname, int sex, String email, String personalSignature, File avatar){
        if (avatar != null) {
            // 构建文件 RequestBody
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), avatar);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", avatar.getName(), requestFile);

            // 获取 token
            String token = Store.getInstance(getApplicationContext()).getData("token");

            // 创建请求
            ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
            Call<AvatarResponse> call = apiService.uploadAvatar(filePart, token);

            call.enqueue(new Callback<AvatarResponse>() {
                @Override
                public void onResponse(@NonNull Call<AvatarResponse> call, @NonNull Response<AvatarResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String avatarUrl = response.body().getData();
                        // 保存头像 URL 到本地
                        Store.getInstance(getApplicationContext()).setData("selfAvatarUrl", avatarUrl);
                        // 更新 UI
                        runOnUiThread(() -> {
                            Glide.with(ProfileActivity.this)
                                    .load(avatarUrl)
                                    .apply(RequestOptions.circleCropTransform())
                                    .into(ivAvatar);
                            Toast.makeText(ProfileActivity.this, "头像更新成功", Toast.LENGTH_SHORT).show();
                        });
                        AvatarModel avatarModel=new AvatarModel(getApplicationContext());
                        avatarModel.saveAvatar(userId,avatarUrl);
                    } else {
                        Toast.makeText(ProfileActivity.this, "上传失败: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AvatarResponse> call, @NonNull Throwable t) {
                    Toast.makeText(ProfileActivity.this, "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<Void> call = apiService.uploadUserInfo(new UpdateInfoRequest(nickname,sex,personalSignature), Store.getInstance(getApplicationContext()).getData("token"));

        call.enqueue(new Callback<Void>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                tvGender.setText(sex);
                tvSignature.setText(personalSignature);
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(ProfileActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showChangePasswordDialog(){
        ChangePasswordDialogFragment dialog = new ChangePasswordDialogFragment();
        dialog.setOnPasswordChangedListener(newPassword -> {
            // 处理密码修改逻辑（例如调用 API）
            Log.d("Password", "新密码: " + newPassword);
            ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
            Call<Void> call = apiService.updatePassword(new ChangePasswordRequest(md5(newPassword)), Store.getInstance(getApplicationContext()).getData("token"));

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    Toast.makeText(ProfileActivity.this, "密码修改成功", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    Toast.makeText(ProfileActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                }
            });
        });
        dialog.show(getSupportFragmentManager(), "修改密码");
    }
}

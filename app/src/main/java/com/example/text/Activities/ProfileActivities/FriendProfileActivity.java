package com.example.text.Activities.ProfileActivities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.text.R;
import com.google.android.material.button.MaterialButton;

public class FriendProfileActivity extends AppCompatActivity {
    private ImageButton btnBack;
    private ImageView ivAvatar;
    private TextView tvUserName;
    private TextView tvUserId;
    private TextView tvGender;
    private TextView tvEmail;
    private TextView tvSignature;
    private MaterialButton btnChangeProfile;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);

        btnBack=findViewById(R.id.btn_back);
        ivAvatar=findViewById(R.id.iv_avatar);
        tvUserName=findViewById(R.id.tv_username);
        tvUserId=findViewById(R.id.tv_userId);
        tvGender=findViewById(R.id.tv_gender);
        tvEmail=findViewById(R.id.tv_email);
        tvSignature=findViewById(R.id.tv_signature);
        btnChangeProfile=findViewById(R.id.btn_changeProfile);

        runOnUiThread(() -> {
            Glide.with(FriendProfileActivity.this)
                    .load("http://10.29.61.159:5050/images/2024-11-27/2.png")
                    .apply(RequestOptions.circleCropTransform())
                    .into(ivAvatar);
        });

        tvUserName.setText("昵称:"+"wjc");
        tvUserId.setText("ID:"+"123456");
        tvGender.setText("性别:"+"男");
        tvEmail.setText("邮箱:"+"qq.com");
        tvSignature.setText("个性签名:"+"我是wjccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc");

        btnBack.setOnClickListener(v->finish());
//        btnSendMessage.setOnClickListener(v -> finish());
//        btnDeleteFriend.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }
}

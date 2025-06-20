package com.example.text.sip.test;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.text.R;
import com.example.text.activities.profileActivities.FriendProfileActivity;
import com.example.text.apis.ApiService;
import com.example.text.dataModel.FriendInfo;
import com.example.text.dataModel.request.FriendInfoRequest;
import com.example.text.dataModel.response.FriendInfoResponse;
import com.example.text.retrofits.RetrofitClient;
import com.example.text.sip.LinphoneManager;
import com.example.text.utils.Store;

import org.linphone.core.Call;

import retrofit2.Callback;
import retrofit2.Response;

public class IncomingCallActivity extends AppCompatActivity {
    private ImageView imageViewAvatar;
    private TextView callerInfo;
    private Chronometer chronometer;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_call);

        imageViewAvatar=findViewById(R.id.iv_avatar);
        chronometer=findViewById(R.id.chronometer);

        // 显示来电者信息
        callerInfo = findViewById(R.id.caller_info);
        String remoteAddress = getIntent().getStringExtra("remoteAddress");
        boolean isVideoCall = getIntent().getBooleanExtra("isVideoCall",false);
        showCallerInfo("U"+remoteAddress.substring(2,13),isVideoCall);

        // 接受和拒绝按钮
        Button acceptButton = findViewById(R.id.accept_call);
        Button declineButton = findViewById(R.id.decline_call);

        acceptButton.setOnClickListener(v -> {
            Call call = LinphoneManager.getInstance(getApplicationContext()).getCore().getCurrentCall();
            if (call != null) {
                try {
                    call.setCameraEnabled(true);
                    call.accept();
                    startActivity(new Intent(this, CallActivity.class));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        declineButton.setOnClickListener(v -> {
            Call call = LinphoneManager.getInstance(getApplicationContext()).getCore().getCurrentCall();
            if (call != null) {
                try {
                    call.terminate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            finish();
        });

        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    private void showCallerInfo(String userId,boolean isVideoCall){
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        retrofit2.Call<FriendInfoResponse> call = apiService.showFriendInfo(new FriendInfoRequest(userId), Store.getInstance(getApplicationContext()).getData("token"));

        call.enqueue(new Callback<FriendInfoResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull retrofit2.Call<FriendInfoResponse> call, @NonNull Response<FriendInfoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    FriendInfo friendInfo=response.body().getData();
                    runOnUiThread(() -> {
                        Glide.with(IncomingCallActivity.this)
                                .load(friendInfo.getAvatarUrl())
                                .apply(RequestOptions.circleCropTransform())
                                .into(imageViewAvatar);
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        chronometer.start();
                    });
                    callerInfo.setText("来自好友" + friendInfo.getNickName() + (isVideoCall?"的视频电话":"的音频电话"));
                } else {
                    Toast.makeText(IncomingCallActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<FriendInfoResponse> call, @NonNull Throwable t) {
                Toast.makeText(IncomingCallActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

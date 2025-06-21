package com.example.text.activities.profileActivities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.text.R;
import com.example.text.activities.chatActivities.ChatActivity;
import com.example.text.apis.ApiService;
import com.example.text.dataModel.FriendInfo;
import com.example.text.dataModel.SessionListItem;
import com.example.text.dataModel.request.DeleteContactRequest;
import com.example.text.dataModel.request.FriendInfoRequest;
import com.example.text.dataModel.response.FriendInfoResponse;
import com.example.text.database.ChatSessionModel;
import com.example.text.retrofits.RetrofitClient;
import com.example.text.utils.Store;
import com.google.android.material.button.MaterialButton;
//import com.example.text.adapters.MemberAdapter;
//import com.example.text.models.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupProfileActivity extends AppCompatActivity {
    private ImageButton btnBack;
    private ImageView ivAvatar;
    private TextView tvUserName;
    private TextView tvUserId;
    private TextView tvGender;
    private TextView tvEmail;
    private TextView tvSignature;
    private MaterialButton btnSendMessage;
    private MaterialButton btnDeleteFriend;
    private String friendId;
    private FriendInfo friendInfo;

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
        btnSendMessage=findViewById(R.id.btn_sendMessage);
        btnDeleteFriend=findViewById(R.id.btn_deleteFriend);

        Intent intent=getIntent();
        friendId=intent.getStringExtra("friendId");

        showFriendInfo(friendId);

//        tvUserName.setText("昵称:"+"wjc");
//        tvUserId.setText("ID:"+"123456");
//        tvGender.setText("性别:"+"男");
//        tvEmail.setText("邮箱:"+"qq.com");
//        tvSignature.setText("个性签名:"+"我是wjccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc");

        btnBack.setOnClickListener(v->finish());
        btnSendMessage.setOnClickListener(v->sendMessage());
        btnDeleteFriend.setOnClickListener(v->deleteContact());

//        btnSendMessage.setOnClickListener(v -> finish());
//        btnDeleteFriend.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void showFriendInfo(String contactId){
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<FriendInfoResponse> call = apiService.showFriendInfo(new FriendInfoRequest(contactId), Store.getInstance(getApplicationContext()).getData("token"));

        call.enqueue(new Callback<FriendInfoResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<FriendInfoResponse> call, @NonNull Response<FriendInfoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    friendInfo=response.body().getData();
                    tvUserName.setText("昵称:"+friendInfo.getNickName());
                    tvUserId.setText("ID:"+friendInfo.getUserId());
                    tvGender.setText("性别:"+friendInfo.getSex());
                    tvEmail.setText("邮箱:"+friendInfo.getEmail());
                    tvSignature.setText("个性签名:"+friendInfo.getPersonalSignature());
                    runOnUiThread(() -> {
                        Glide.with(GroupProfileActivity.this)
                                .load(friendInfo.getAvatarUrl())
                                .apply(RequestOptions.circleCropTransform())
                                .into(ivAvatar);
                    });
                } else {
                    Toast.makeText(GroupProfileActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<FriendInfoResponse> call, @NonNull Throwable t) {
                Toast.makeText(GroupProfileActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage(){
        Intent intent = new Intent(this, ChatActivity.class);
        ChatSessionModel chatSessionModel=new ChatSessionModel(getApplicationContext());
        chatSessionModel.showChatSession(friendId);
        SessionListItem sessionListItem=new SessionListItem(chatSessionModel.getChatSessionByContactId(friendId));
        intent.putExtra("userId", sessionListItem.getUserId());
        intent.putExtra("sessionId", sessionListItem.getSessionId());
        intent.putExtra("contactId", sessionListItem.getContactId());
        intent.putExtra("sendUserNickName", sessionListItem.getContactName());
        intent.putExtra("contactType", sessionListItem.getContactType());
        startActivity(intent);
    }

    private void deleteContact(){
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<Void> call = apiService.deleteContact(new DeleteContactRequest(friendId), Store.getInstance(getApplicationContext()).getData("token"));

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                Toast.makeText(GroupProfileActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(GroupProfileActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
        });

    }

//    private RecyclerView recyclerGroupMembers;
//    private TextView tvAnnouncementContent;
//    private Button btnExitGroup;
//    private ImageButton btnBack;
//
//    private List<User> memberList;
//    private MemberAdapter memberAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_group_profile);
//
//        initViews();
//        setupRecyclerView();
//        loadGroupInfo();
//        setupListeners();
//    }
//
//    private void initViews() {
//        recyclerGroupMembers = findViewById(R.id.recycler_group_members);
//        tvAnnouncementContent = findViewById(R.id.tv_announcement_content);
//        btnExitGroup = findViewById(R.id.btn_exit_group);
//        btnBack = findViewById(R.id.btn_back);
//    }
//
//    private void setupRecyclerView() {
//        memberList = new ArrayList<>();
//        memberAdapter = new MemberAdapter(memberList, this);
//        recyclerGroupMembers.setLayoutManager(new GridLayoutManager(this, 4)); // 4列九宫格
//        recyclerGroupMembers.setAdapter(memberAdapter);
//    }
//
//    private void loadGroupInfo() {
//        // 模拟加载群成员
//        for (int i = 1; i <= 8; i++) {
//            memberList.add(new User("用户" + i, R.drawable.default_avatar));
//        }
//        memberAdapter.notifyDataSetChanged();
//
//        // 设置群公告
//        tvAnnouncementContent.setText("本群每日 21:00 开始语音学习，禁止灌水~");
//    }
//
//    private void setupListeners() {
//        btnExitGroup.setOnClickListener(v -> {
//            Toast.makeText(this, "已退出群聊", Toast.LENGTH_SHORT).show();
//            finish();
//        });
//
//        btnBack.setOnClickListener(v -> finish());
//    }
}

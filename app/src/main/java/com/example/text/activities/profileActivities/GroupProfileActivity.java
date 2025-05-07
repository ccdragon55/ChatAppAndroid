package com.example.text.activities.profileActivities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.text.R;
//import com.example.text.adapters.MemberAdapter;
//import com.example.text.models.User;

import java.util.ArrayList;
import java.util.List;

public class GroupProfileActivity extends AppCompatActivity {

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

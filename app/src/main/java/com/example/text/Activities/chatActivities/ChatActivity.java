package com.example.text.Activities.chatActivities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.text.Adapters.ChatMessageAdapter;
import com.example.text.R;
import com.example.text.dataModel.ChatMessage;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private Intent intent;
//    private Button buttonBack;
    private ImageButton buttonBack;

    private TextView textViewTopFriendNickName;
    private RecyclerView recyclerView;
    private ChatMessageAdapter adapter;
    private List<ChatMessage> messageList = new ArrayList<>();
    private String friendName;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        intent=getIntent();
        friendName=intent.getStringExtra("friendName");

        textViewTopFriendNickName=findViewById(R.id.chat_TextView_useName);
        textViewTopFriendNickName.setText(friendName);

        // 初始化 RecyclerView
        recyclerView = findViewById(R.id.chat_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // 添加测试数据
        messageList.add(new ChatMessage(friendName,"你好！", ChatMessage.TYPE_RECEIVED, "10:00"));
        messageList.add(new ChatMessage("self","你好呀~", ChatMessage.TYPE_SENT, "10:01"));
        messageList.add(new ChatMessage(friendName,"你好yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy！", ChatMessage.TYPE_RECEIVED, "10:00"));
        messageList.add(new ChatMessage("self","你好呀~jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj", ChatMessage.TYPE_SENT, "10:01"));
        messageList.add(new ChatMessage(friendName,"你好yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy！", ChatMessage.TYPE_RECEIVED, "10:00"));
        messageList.add(new ChatMessage("self","你好呀~jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj", ChatMessage.TYPE_SENT, "10:01"));
        messageList.add(new ChatMessage(friendName,"你好yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy！", ChatMessage.TYPE_RECEIVED, "10:00"));
        messageList.add(new ChatMessage("self","你好呀~jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj", ChatMessage.TYPE_SENT, "10:01"));
        messageList.add(new ChatMessage(friendName,"你好yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy！", ChatMessage.TYPE_RECEIVED, "10:00"));
        messageList.add(new ChatMessage("self","你好呀~jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj", ChatMessage.TYPE_SENT, "10:01"));
        messageList.add(new ChatMessage(friendName,"你好yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy！", ChatMessage.TYPE_RECEIVED, "10:00"));
        messageList.add(new ChatMessage("self","你好呀~jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj", ChatMessage.TYPE_SENT, "10:01"));
        messageList.add(new ChatMessage(friendName,"你好yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy！", ChatMessage.TYPE_RECEIVED, "10:00"));
        messageList.add(new ChatMessage("self","你好呀~jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj", ChatMessage.TYPE_SENT, "10:01"));
        messageList.add(new ChatMessage(friendName,"你好yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy！", ChatMessage.TYPE_RECEIVED, "10:00"));
        messageList.add(new ChatMessage("self","你好呀~jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj", ChatMessage.TYPE_SENT, "10:01"));

        // 设置 Adapter
        adapter = new ChatMessageAdapter(messageList);
        recyclerView.setAdapter(adapter);

//        buttonBack=findViewById(R.id.button_back);
//        buttonBack.setOnClickListener(v -> finish());
        buttonBack = findViewById(R.id.button_back); // 使用新ID
//        buttonBack.setNavigationOnClickListener(v -> finish());  // MaterialToolbar 专用方法
        buttonBack.setOnClickListener(v -> finish());  // MaterialToolbar 专用方法
    }
}

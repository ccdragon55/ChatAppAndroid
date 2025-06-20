package com.example.text.activities.chatActivities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.text.activities.loginActivities.LoginActivity;
import com.example.text.activities.loginActivities.RegisterActivity;
import com.example.text.adapters.ChatMessageAdapter;
import com.example.text.R;
import com.example.text.apis.ApiService;
import com.example.text.dataModel.ChatMessage;
import com.example.text.dataModel.ChatSession;
import com.example.text.dataModel.SendMessage;
import com.example.text.dataModel.request.LoginRequest;
import com.example.text.dataModel.request.SendMessageRequest;
import com.example.text.dataModel.response.LoginResponse;
import com.example.text.dataModel.response.SendMessageResponse;
import com.example.text.database.ChatMessageModel;
import com.example.text.database.ChatSessionModel;
import com.example.text.database.DBManager;
import com.example.text.retrofits.RetrofitClient;
import com.example.text.sip.LinphoneManager;
import com.example.text.sip.test.IncomingCallActivity;
import com.example.text.utils.ChatInputUtils;
import com.example.text.utils.Constants;
import com.example.text.utils.DateUtils;
import com.example.text.utils.JsonUtils;
import com.example.text.utils.Store;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;
import org.linphone.core.TransportType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 100;
    private Intent intent;
//    private Button buttonBack;
    private ImageButton buttonBack;
    private EditText etInput;
    private MaterialButton buttonSend;

    private TextView textViewTopFriendNickName;
    private RecyclerView recyclerView;
    private ChatMessageAdapter adapter;
    private List<ChatMessage> messageList = new ArrayList<>();
    private String sendUserNickName;
    private String userId;
    private String sessionId;
    private String contactId;
    private int contactType;
    private ChatMessageModel chatMessageModel;

    private int currentPage;
    private int pageSize;
    private int totalMessage;
    private long startTime;
    private SharedPreferences sharedPreferences;
    private DBManager dbManager;
    private ChatSessionModel chatSessionModel;
    private String sipUserName,sipPassword,friendSipAddress;

//    private BroadcastReceiver groupReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context ctx, Intent intent) {
//            String raw = intent.getStringExtra("payload");
//            // 处理建群、加群等逻辑
//        }
//    };
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        LocalBroadcastManager.getInstance(this)
//                .registerReceiver(groupReceiver,
//                        new IntentFilter("ACTION_WS_MSG_3"));
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        LocalBroadcastManager.getInstance(this)
//                .unregisterReceiver(groupReceiver);
//    }

    private BroadcastReceiver wsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String json = intent.getStringExtra("dataJson"); // 公共字段

            try {
                // 根据 Action 分发不同逻辑
                if (action == null) return;

                switch (action) {
                    case "ACTION_WS_receiveSingleMessage":
                        handleReceiveSingleMessage(json);
                        break;
                    default:
                        break;
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        private void handleReceiveSingleMessage(String json) throws JSONException {
            JSONObject jsonObject=new JSONObject(json);
            Map<String,Object> message= JsonUtils.jsonToStrObjMap(jsonObject);
            if(Objects.equals(sessionId, (String) message.get("sessionId"))){
                ChatMessage chatMessage=new ChatMessage(message);
                chatMessage.setDate(DateUtils.formatDate(chatMessage.getSendTime()));
                messageList.add(chatMessage);
                adapter.notifyItemInserted(messageList.size() - 1);
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("ACTION_WS_receiveSingleMessage");
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(wsReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(wsReceiver);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        intent=getIntent();
//        friendName=intent.getStringExtra("friendName");
        userId=intent.getStringExtra("userId");
        sessionId=intent.getStringExtra("sessionId");
        contactId=intent.getStringExtra("contactId");
        sendUserNickName=intent.getStringExtra("sendUserNickName");
        contactType=intent.getIntExtra("contactType",0);

        sharedPreferences=getSharedPreferences("userPrefs", MODE_PRIVATE);
        dbManager = new DBManager(getApplicationContext());
        chatSessionModel = new ChatSessionModel(getApplicationContext());

        textViewTopFriendNickName=findViewById(R.id.chat_TextView_useName);
        textViewTopFriendNickName.setText(sendUserNickName);
        etInput=findViewById(R.id.et_input);
        if(ChatInputUtils.messageInput.containsKey(sessionId)){
            etInput.setText(ChatInputUtils.messageInput.get(sessionId));
        }

        chatMessageModel=new ChatMessageModel(getApplicationContext());
        currentPage = 0;
        pageSize = 10;
        totalMessage = (chatMessageModel.getTotalMessageCount(sessionId)+pageSize-1) / pageSize;
        startTime = System.currentTimeMillis();

        // 初始化 RecyclerView
        recyclerView = findViewById(R.id.chat_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // 添加测试数据
//        messageList.add(new ChatMessage(friendName,"你好！", ChatMessage.TYPE_RECEIVED, "10:00"));
//        messageList.add(new ChatMessage("self","你好呀~", ChatMessage.TYPE_SENT, "10:01"));
//        messageList.add(new ChatMessage(friendName,"你好yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy！", ChatMessage.TYPE_RECEIVED, "10:00"));
//        messageList.add(new ChatMessage("self","你好呀~jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj", ChatMessage.TYPE_SENT, "10:01"));
//        messageList.add(new ChatMessage(friendName,"你好yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy！", ChatMessage.TYPE_RECEIVED, "10:00"));
//        messageList.add(new ChatMessage("self","你好呀~jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj", ChatMessage.TYPE_SENT, "10:01"));
//        messageList.add(new ChatMessage(friendName,"你好yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy！", ChatMessage.TYPE_RECEIVED, "10:00"));
//        messageList.add(new ChatMessage("self","你好呀~jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj", ChatMessage.TYPE_SENT, "10:01"));
//        messageList.add(new ChatMessage(friendName,"你好yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy！", ChatMessage.TYPE_RECEIVED, "10:00"));
//        messageList.add(new ChatMessage("self","你好呀~jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj", ChatMessage.TYPE_SENT, "10:01"));
//        messageList.add(new ChatMessage(friendName,"你好yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy！", ChatMessage.TYPE_RECEIVED, "10:00"));
//        messageList.add(new ChatMessage("self","你好呀~jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj", ChatMessage.TYPE_SENT, "10:01"));
//        messageList.add(new ChatMessage(friendName,"你好yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy！", ChatMessage.TYPE_RECEIVED, "10:00"));
//        messageList.add(new ChatMessage("self","你好呀~jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj", ChatMessage.TYPE_SENT, "10:01"));
//        messageList.add(new ChatMessage(friendName,"你好yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy！", ChatMessage.TYPE_RECEIVED, "10:00"));
//        messageList.add(new ChatMessage("self","你好呀~jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj", ChatMessage.TYPE_SENT, "10:01"));

        // 设置 Adapter
        adapter = new ChatMessageAdapter(messageList);
        recyclerView.setAdapter(adapter);

        // 监听滚动事件
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
//
//                if (currentPage < totalMessage) {
//                    loadChatMessageData();
//                }
//            }
//        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(-1)) {
                    if (currentPage < totalMessage) {
                        loadChatMessageData();
                    }
                }
            }
        });

        loadChatMessageData();

//        buttonBack=findViewById(R.id.button_back);
//        buttonBack.setOnClickListener(v -> finish());
        buttonBack = findViewById(R.id.button_back); // 使用新ID
//        buttonBack.setNavigationOnClickListener(v -> finish());  // MaterialToolbar 专用方法
        buttonBack.setOnClickListener(v -> back());  // MaterialToolbar 专用方法

        buttonSend = findViewById(R.id.btn_send);
        buttonSend.setOnClickListener(v->sendMessage());

        // sip部分
        // 请求权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA
            }, PERMISSION_REQUEST_CODE);
        }

        // 拨号按钮
        ImageButton audioCallButton = findViewById(R.id.btn_audio_call);
        ImageButton videoCallButton = findViewById(R.id.btn_video_call);

        sipUserName = "0"+userId.substring(1);
        sipPassword = "123456";
        friendSipAddress = "0"+contactId.substring(1)+"@"+ Constants.SIP_URL;
        initializeLinphone();

        audioCallButton.setOnClickListener(v -> {
            //initializeLinphone();
            if (!friendSipAddress.isEmpty()) {
                LinphoneManager.getInstance(getApplicationContext()).makeCall(friendSipAddress, false);
                Intent intent = new Intent(this, IncomingCallActivity.class);
                intent.putExtra("remoteAddress", friendSipAddress);
                intent.putExtra("isVideoCall", false);// 是否是视频通话
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        videoCallButton.setOnClickListener(v -> {
            //initializeLinphone();
            if (!friendSipAddress.isEmpty()) {
                LinphoneManager.getInstance(getApplicationContext()).makeCall(friendSipAddress, true);
                Intent intent = new Intent(this, IncomingCallActivity.class);
                intent.putExtra("remoteAddress", friendSipAddress);
                intent.putExtra("isVideoCall", true);// 是否是视频通话
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadChatMessageData() {
        List<Map<String,Object>> data = chatMessageModel.selectChatMessageByPage(sessionId,currentPage,pageSize,startTime);

        Log.e("fk","data:"+data.toString());

        if(data==null){
            return;
        }
        Collections.reverse(data);
        currentPage++;

        List<ChatMessage> messageData=new ArrayList<>();
        for(Map<String,Object> d:data){
            messageData.add(new ChatMessage(d));
        }

        // 插入到头部
        messageList.addAll(0, messageData);

        // 格式化日期字段
        for (int i=0;i<messageList.size();++i) {
            messageList.get(i).setDate(DateUtils.formatDate(messageList.get(i).getSendTime()));
            messageList.get(i).setShowDate(notSameDate(i));
        }

        Log.e("fk","messageList:"+messageList.toString());

        adapter.notifyDataSetChanged();

        // 如果是第一页，滚动到底部
        if (currentPage == 1) {
            recyclerView.post(() -> {
                int lastPos = adapter.getItemCount() - 1;
                if (lastPos >= 0) {
                    recyclerView.scrollToPosition(lastPos);
                }
            });
        }
    }

    public void sendMessage() {
        if (sessionId == null)
            return;

        String content = String.valueOf(etInput.getText());
        if (content.trim().isEmpty())
            return;

        // 构造本地消息对象
        Map<String, Object> data = new HashMap<>();
        data.put("userId", sharedPreferences.getString("userId",""));
        data.put("sessionId", sessionId);
        data.put("messageType", 2);
        data.put("messageContent", content);
        data.put("contactType", contactType);
        data.put("sendUserId", sharedPreferences.getString("userId",""));
        data.put("sendUserNickName", sharedPreferences.getString("nickName",""));
        data.put("sendTime", System.currentTimeMillis());
        data.put("status", 1);

        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<SendMessageResponse> call = apiService.sendMessage(new SendMessageRequest(contactId, 2, content), Store.getInstance(getApplicationContext()).getData("token"));
        call.enqueue(new Callback<SendMessageResponse>() {
            @Override
            public void onResponse(Call<SendMessageResponse> call, Response<SendMessageResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SendMessage sendMessageResponse = response.body().getData();
                    data.put("messageId", sendMessageResponse.getMessageId());
                    data.put("userId", Store.getInstance(getApplicationContext()).getUserId());

                    new Thread(() -> dbManager.insertOrReplace("chat_message", data)).start();
                    new Thread(() -> chatSessionModel.updateChatSessionLastInfo(contactId, (String) data.get("messageContent"), (long) data.get("sendTime"))).start();
//                    chatSessionModel.updateChatSessionLastInfo(contactId, (String) data.get("messageContent"), (long) data.get("sendTime"));

                    etInput.setText("");

//                    data.put("date", DateUtils.formatDate((long) data.get("sendTime")));
                    data.put("url", sharedPreferences.getString("selfAvatarUrl", ""));
                    messageList.add(new ChatMessage(data));
                    messageList.get(messageList.size() - 1).setDate(DateUtils.formatDate((long) messageList.get(messageList.size() - 1).getSendTime()));
                    messageList.get(messageList.size() - 1).setShowDate(notSameDate(messageList.size() - 1));

                    runOnUiThread(() -> {
                        adapter.notifyItemInserted(messageList.size() - 1);
//                        recyclerView.scrollToPosition(messageList.size() - 1);
                    });
                } else {
                    Toast.makeText(ChatActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SendMessageResponse> call, Throwable t) {
//                Toast.makeText(ChatActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                runOnUiThread(() -> Toast.makeText(ChatActivity.this, "发送失败", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void back(){
        String input= String.valueOf(etInput.getText()).trim();
        if(!input.equals("")){
            ChatInputUtils.messageInput.put(sessionId,input);
        }
        finish();
    }

    private boolean notSameDate(int index) {
        return index == 0 || !Objects.equals(messageList.get(index).getDate(), messageList.get(index-1).getDate());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initializeLinphone();
        }
    }

    private void initializeLinphone() {
        // 初始化 Linphone 并注册
        LinphoneManager manager = LinphoneManager.getInstance(getApplicationContext());
        //manager.createProxyConfig("your_username", "your_password", "10.129.156.163:5060", TransportType.Udp);
        manager.createProxyConfig(sipUserName, sipPassword, "10.129.156.163", TransportType.Udp);
    }
}

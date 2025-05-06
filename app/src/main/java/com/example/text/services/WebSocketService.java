package com.example.text.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.text.R;
import com.example.text.dataModel.ChatSession;
import com.example.text.database.AvatarModel;
import com.example.text.database.ChatMessageModel;
import com.example.text.database.ChatSessionModel;
import com.example.text.utils.JsonUtils;
import com.example.text.utils.Store;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class WebSocketService extends Service {
    private final IBinder binder = new LocalBinder();
    private OkHttpClient client;
    private WebSocket ws;
    private int reconnectCount = 0;
    private final int maxReconnect = 5;
    private Handler handler;
    private final long HEARTBEAT_INTERVAL = 5000L;
    private boolean isRunning = false;

    private AvatarModel avatarModel;
    private ChatMessageModel chatMessageModel;
    private ChatSessionModel chatSessionModel;

    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "WebSocketChannel";

    @SuppressLint("ForegroundServiceType")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 必须立即创建通知并调用 startForeground()
        createNotificationChannel();
        Notification notification = buildNotification();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC);
        } else {
            startForeground(NOTIFICATION_ID, notification);
        }
//        startForeground(NOTIFICATION_ID, notification);

        return START_STICKY; // 根据需求选择合适的返回值
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) { // 🔥 避免 manager 为空导致崩溃
                NotificationChannel channel = new NotificationChannel(
                        CHANNEL_ID,
                        "WebSocket Service",
                        NotificationManager.IMPORTANCE_LOW
                );
                manager.createNotificationChannel(channel);
            }
        }
    }

//    private void createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(
//                    CHANNEL_ID,
//                    "WebSocket Service",
//                    NotificationManager.IMPORTANCE_LOW
//            );
//            NotificationManager manager = getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(channel);
//        }
//    }

    private Notification buildNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("WebSocket 服务运行中")
                .setContentText("正在连接服务器...")
                .build();
    }

    public class LocalBinder extends Binder {
        public WebSocketService getService() {
            return WebSocketService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        client = new OkHttpClient.Builder()
                .pingInterval(5, TimeUnit.SECONDS)
                .build();
        handler = new Handler();
        avatarModel=new AvatarModel(getApplicationContext());
        chatMessageModel=new ChatMessageModel((getApplicationContext()));
        chatSessionModel=new ChatSessionModel((getApplicationContext()));
        startWebSocket();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        if (ws != null) {
            ws.close(1000, "Service destroyed");
        }
        handler.removeCallbacksAndMessages(null);
    }

    private void startWebSocket() {
        String token = Store.getInstance(getApplicationContext()).getData("token"); // 自定义的 Token 存储工具
        Request request = new Request.Builder()
                .url("ws://10.29.61.159:5051/ws?token=" + token)
                .build();

        Log.e("fk", "startWebSocket");

        ws = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                reconnectCount = 0;
                sendHeartbeat();
                broadcast("WS_OPEN");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                handleMessage(text);
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                handleMessage(bytes.utf8());
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                webSocket.close(code, null);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                reconnect();
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                reconnect();
            }
        });
        isRunning = true;
    }

    private void sendHeartbeat() {
        if (!isRunning || ws == null) return;
        ws.send("heart beat");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sendHeartbeat();
            }
        }, HEARTBEAT_INTERVAL);
    }

    private void reconnect() {
        if (reconnectCount < maxReconnect) {
            reconnectCount++;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startWebSocket();
                }
            }, HEARTBEAT_INTERVAL);
        } else {
            broadcast("WS_TIMEOUT");
        }
    }

    private void handleMessage(String raw) {
        try {
            JSONObject message = new JSONObject(raw);
            int messageType = message.getInt("messageType");

            switch (messageType) {
                case 0:{
                    // ws连接成功
//                    JSONObject data0 = message.getJSONObject("extendData");
//                    JSONArray sessions = data0.getJSONArray("chatSessionList");
//                    JSONArray messages = data0.getJSONArray("chatMessageList");
//                    // TODO: 解析并批量保存会话 & 消息，例如：
//
//                    chatSessionModel.saveOrUpdateChatSessionBatch4Init(JsonUtils.fromJsonList(sessions.toString(), ChatSession.class));
//                    // ChatSessionDao.insertOrUpdateList(JsonUtils.fromJson(sessions, ChatSession.class));
//                    // ChatMessageDao.insertList(JsonUtils.fromJson(messages, ChatMessage.class));
//                    broadcast("receiveMessage", null);



                    JSONObject data0 = message.getJSONObject("extendData");
                    JSONArray sessions = data0.getJSONArray("chatSessionList");
                    JSONArray messages = data0.getJSONArray("chatMessageList");

                    Log.e("fk", "data0: " + data0.toString());
                    Log.e("fk", "sessions: " + sessions.toString());
                    Log.e("fk", "messages: " + messages.toString());


                    // TODO: 解析并批量保存会话 & 消息，例如：
                    List<Map<String, Object>> sessionsList = new ArrayList<>();
                    for (int i = 0; i < sessions.length(); i++) {
                        JSONObject jo = sessions.getJSONObject(i);
                        // 用 LinkedHashMap 保持 JSON 字段的原有顺序，如果不在意顺序也可用 HashMap
//                        Map<String, Object> map = new LinkedHashMap<>();
//                        Iterator<String> it = jo.keys();
//                        while (it.hasNext()) {
//                            String key = it.next();
//                            map.put(key, jo.get(key));
//                        }
//                        sessionsList.add(map);
                        sessionsList.add(JsonUtils.jsonToStrObjMap(jo));
                    }
                    Log.e("fk", "sessionsList: " +sessionsList.toString());
                    chatSessionModel.saveOrUpdateChatSessionBatch4Init(sessionsList);
                    List<Map<String, Object>> messagesList = new ArrayList<>();
                    for (int i = 0; i < messages.length(); i++) {
                        JSONObject jo = messages.getJSONObject(i);
                        // 用 LinkedHashMap 保持 JSON 字段的原有顺序，如果不在意顺序也可用 HashMap
//                        Map<String, Object> map = new LinkedHashMap<>();
//                        Iterator<String> it = jo.keys();
//                        while (it.hasNext()) {
//                            String key = it.next();
//                            map.put(key, jo.get(key));
//                        }
//                        messagesList.add(map);
                        messagesList.add(JsonUtils.jsonToStrObjMap(jo));
                    }
//                    chatMessageModel.saveChatMessageBatch(messagesList);
                    Map<String,Object> info=new HashMap<>();
                    info.put("messageType",message.get("messageType"));
                    broadcast("receiveMessage",info);
                    break;
                }
                case 1: { // 同意好友申请
                    // 保存头像
                    String contactId = message.getString("contactId");
                    String avatarUrl = message.getString("avatarUrl");
                    // AvatarDao.save(new Avatar(contactId, avatarUrl));
                    avatarModel.saveAvatar(contactId, avatarUrl);

                    // 保存会话
                    // ChatSession newSession = JsonUtils.fromJson(message, ChatSession.class);
                    // ChatSessionDao.insert(newSession);
                    Map<String, Object> newSession = new LinkedHashMap<>();
                    newSession.put("contactId",message.get("contactId"));
                    newSession.put("contactType",message.get("contactType"));
                    newSession.put("sessionId",message.get("sessionId"));
                    newSession.put("status",message.get("status"));
                    newSession.put("contactName",message.get("contactName"));
                    newSession.put("sendUserId",message.get("sendUserId"));
                    newSession.put("lastMessage",message.get("lastMessage"));
                    newSession.put("lastReceiveTime",message.get("sendTime"));
                    chatSessionModel.addChatSession(newSession);

                    // 保存消息
                    // ChatMessage newMsg = JsonUtils.fromJson(message, ChatMessage.class);
                    // ChatMessageDao.insert(newMsg);
                    Map<String, Object> newMessage = new LinkedHashMap<>();
                    newMessage.put("contactType",message.get("contactType"));
                    newMessage.put("messageContent",message.get("messageContent"));
                    newMessage.put("messageId",message.get("messageId"));
                    newMessage.put("messageType",message.get("messageType"));
                    newMessage.put("sendTime",message.get("sendTime"));
                    newMessage.put("sendUserId",message.get("sendUserId"));
                    newMessage.put("sendUserNickName",message.get("sendUserNickName"));
                    newMessage.put("sessionId",message.get("sessionId"));
                    newMessage.put("status",message.get("status"));
                    chatMessageModel.saveMessage(newMessage);

                    // 通知界面更新
//                    broadcast("receiveNewSession", message.getString("sessionId"));
//                    broadcast("addNewFriend", contactId);
                    newSession.put("url",message.get("avatarUrl"));
                    newSession.put("topType",0);
                    broadcast("receiveNewSession", newSession);

                    Map<String,Object> map= new LinkedHashMap<>();
                    map.put("contactId",message.get("contactId"));
                    map.put("contactName",message.get("contactName"));
                    map.put("avatarUrl",message.get("avatarUrl"));
                    broadcast("addNewFriend", map);
                    break;
                }
                case 2: { // 普通聊天信息
                    // 过滤自己发送的
                    if (message.getInt("contactType") == 1
                            && message.getString("sendUserId").equals(Store.getInstance(getApplicationContext()).getUserId())) {
                        return;
                    }
                    // 更新会话最后信息
                    String contactId = message.getString("contactId");
                    String lastMessage = message.getString("lastMessage");
                    long sendTime = message.getLong("sendTime");
                    // ChatSessionDao.updateLastInfo(cid, lastMsg, sendTime);
                    chatSessionModel.updateChatSessionLastInfoAndShow(contactId,lastMessage,sendTime);

                    // 获取会话并通知列表页
                    // ChatSession sessionData = ChatSessionDao.findByContactId(cid);
                    Map<String,Object> sessionDate=chatSessionModel.getChatSessionByContactId(contactId);
                    broadcast("receiveSingleMessageUpdateChatSession", sessionDate);

                    // 保存消息
                    // ChatMessage msg2 = JsonUtils.fromJson(message, ChatMessage.class);
                    // ChatMessageDao.insert(msg2);
                    String avatarUrl=message.getString("avatarUrl");
                    // 要删除的字段列表
                    List<String> keysToRemove = Arrays.asList(
                            "extendData",
                            "contactName",
                            "lastMessage",
                            "memberCount",
                            "avatarUrl"
                    );
                    // 新建一个 JSONObject 存放过滤结果
                    JSONObject newMessageJson = new JSONObject();
                    Iterator<String> it = message.keys();
                    while (it.hasNext()) {
                        String key = it.next();
                        if (!keysToRemove.contains(key)) {
                            // JSONObject#get 会抛 JSONException，需要处理
                            try {
                                newMessageJson.put(key, message.get(key));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
//                    Map<String, Object> newMessageMap = new LinkedHashMap<>();
//                    Iterator<String> newMessageIt = newMessageJson.keys();
//                    while (newMessageIt.hasNext()) {
//                        String key = newMessageIt.next();
//                        newMessageMap.put(key, newMessageJson.get(key));
//                    }
//                    chatMessageModel.saveMessage(newMessageMap);
                    Map<String, Object> newMessageMap = JsonUtils.jsonToStrObjMap(newMessageJson);
                    chatMessageModel.saveMessage(newMessageMap);
                    newMessageMap.put("url",avatarUrl);

                    broadcast("receiveSingleMessage", newMessageMap);
                    break;
                }
                case 3: { // 创建群聊
                    // 解析 extendData
                    JSONObject groupSession = message.getJSONObject("extendData");
                    chatSessionModel.addChatSession(JsonUtils.jsonToStrObjMap(groupSession));
                    Map<String, Object> groupMessage = new LinkedHashMap<>();
                    groupMessage.put("contactType",message.get("contactType"));
                    groupMessage.put("messageContent",message.get("messageContent"));
                    groupMessage.put("messageId",message.get("messageId"));
                    groupMessage.put("messageType",message.get("messageType"));
                    groupMessage.put("sendTime",message.get("sendTime"));
                    groupMessage.put("sessionId",message.get("sessionId"));
                    groupMessage.put("status",message.get("status"));
                    // GroupSessionDao.insert(JsonUtils.fromJson(groupData, GroupSession.class));
                    // 保存欢迎消息
                    // ChatMessage msg3 = JsonUtils.fromJson(message, ChatMessage.class);
                    // ChatMessageDao.insert(msg3);
                    chatMessageModel.saveMessage(groupMessage);
                    broadcast("receiveSingleMessage", groupMessage);
                    break;
                }
                case 4: // 好友申请
                    broadcast("receiveNewApply");
                    break;

                case 9: { // 同意加入群组
                    String contactId = message.getString("contactId");
                    String url = message.getString("avatarUrl");
                    // AvatarDao.save(new Avatar(cId, url));
                    // ChatSession session = JsonUtils.fromJson(message, ChatSession.class);
                    // ChatSessionDao.insertOrUpdate(session);

                    // ChatMessage msg9 = JsonUtils.fromJson(message, ChatMessage.class);
                    // ChatMessageDao.insert(msg9);

                    avatarModel.saveAvatar(contactId,url);
                    List<Map<String,Object>> sessionSelect=chatSessionModel.selectSessionBySessionId(message.getString("sessionId"));
                    boolean noSession = (sessionSelect == null || sessionSelect.isEmpty());
                    Map<String,Object> session = noSession ? null : sessionSelect.get(0);

                    Map<String, Object> newSession = new LinkedHashMap<>();
                    newSession.put("contactId",message.get("contactId"));
                    newSession.put("contactType",message.get("contactType"));
                    newSession.put("sessionId",message.get("sessionId"));
                    newSession.put("status",message.get("status"));
                    newSession.put("contactName",message.get("contactName"));
                    newSession.put("lastMessage",message.get("lastMessage"));
                    newSession.put("lastReceiveTime",message.get("sendTime"));
                    chatSessionModel.saveSession(newSession);
                    Map<String, Object> newMessage = new LinkedHashMap<>();
                    newMessage.put("contactType",message.get("contactType"));
                    newMessage.put("messageContent",message.get("messageContent"));
                    newMessage.put("messageId",message.get("messageId"));
                    newMessage.put("messageType",message.get("messageType"));
                    newMessage.put("sendTime",message.get("sendTime"));
                    newMessage.put("sessionId",message.get("sessionId"));
                    newMessage.put("status",message.get("status"));
                    chatMessageModel.saveMessage(newMessage);

//                    broadcast("addNewGroup", contactId);
//                    broadcast("receiveSingleMessage", message.getString("messageId"));
                    if (noSession) {
                        Map<String, Object> info = new LinkedHashMap<>();
                        info.put("contactId",message.get("contactId"));
                        info.put("contactName",message.get("contactName"));
                        info.put("avatarUrl",message.get("avatarUrl"));
                        broadcast("addNewGroup", info);
                    }
                    if (noSession || (session != null && Integer.parseInt((String) Objects.requireNonNull(session.get("status")))  == 0)) {
                        newSession.put("url",message.get("avatarUrl"));
                        newSession.put("topType",0);
                        broadcast("receiveNewSession", newSession);
                    }
                    broadcast("receiveSingleMessage", newMessage);
                    break;
                }
                case 10: // 更新群昵称
                    broadcast("changeGroupNameReturn");
                    Map<String, Object> info = new LinkedHashMap<>();
                    info.put("contactId",message.get("contactId"));
                    info.put("contactName",message.get("extendData"));
                    broadcast("receiveChangeGroupNameMessage", info);
                    break;

                case 11: // 退出群聊
                    // Optional: handle leave group logic
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void broadcast(String action) {
        Intent intent = new Intent("ACTION_WS_" + action);
        LocalBroadcastManager.getInstance(this)
                .sendBroadcast(intent);
    }

    private void broadcast(String action, String data) {
        Intent intent = new Intent("ACTION_WS_" + action);
        if (data != null) intent.putExtra("data", data);
        LocalBroadcastManager.getInstance(this)
                .sendBroadcast(intent);
    }

    // 发送
    private void broadcast(String action, Map<String, Object> dataMap) {
        Intent intent = new Intent("ACTION_WS_" + action);
        String json = new JSONObject(dataMap).toString();
        intent.putExtra("dataJson", json);
        LocalBroadcastManager.getInstance(this)
                .sendBroadcast(intent);
    }

//    private void handleMessage(String raw) {
//        try {
//            JSONObject msg = new JSONObject(raw);
//            int type = msg.getInt("messageType");
//            Intent intent = new Intent("ACTION_WS_MESSAGE");
//            intent.putExtra("messageType", type);
//            intent.putExtra("payload", raw);
//            LocalBroadcastManager.getInstance(this)
//                    .sendBroadcast(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void broadcast(String action, String data) {
//        Intent intent = new Intent("ACTION_WS_" + action);
//        if (data != null) {
//            intent.putExtra("data", data);
//        }
//        LocalBroadcastManager.getInstance(this)
//                .sendBroadcast(intent);
//    }
}

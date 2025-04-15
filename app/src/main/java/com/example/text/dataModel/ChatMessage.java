package com.example.text.dataModel;

public class ChatMessage {
    public static final int TYPE_RECEIVED = 0; // 接收的消息（左边）
    public static final int TYPE_SENT = 1;     // 发送的消息（右边）

    private String sender;

    private String content;    // 消息内容
    private int type;          // 消息类型（TYPE_RECEIVED 或 TYPE_SENT）
    private String timestamp;  // 时间戳（可选）

    public ChatMessage(String sender,String content, int type, String timestamp) {
        this.sender = sender;
        this.content = content;
        this.type = type;
        this.timestamp = timestamp;
    }

    // Getter 方法
    public String getSender() { return sender; }
    public String getContent() { return content; }
    public int getType() { return type; }
    public String getTimestamp() { return timestamp; }
}

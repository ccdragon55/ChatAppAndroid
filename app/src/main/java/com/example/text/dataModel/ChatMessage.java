package com.example.text.dataModel;

import java.util.Map;
import java.util.Objects;

public class ChatMessage {
    public static final int TYPE_RECEIVED_AUDIO = -2; // 接收的消息（左边）
    public static final int TYPE_RECEIVED_TEXT = -1; // 接收的消息（左边）
    public static final int TYPE_SYSTEM = 0; // 系统发送的消息（中间）
    public static final int TYPE_SENT_TEXT = 1;     // 发送的消息（右边）
    public static final int TYPE_SENT_AUDIO = 2;     // 发送的消息（右边）
    private int type;

    private String userId;
    private long messageId;
    private String sessionId;
    private int messageType;
    private String messageContent;
    private int contactType;
    private String sendUserId;
    private String sendUserNickName;
    private long sendTime;
    private boolean showDate;
    private String date;//时间戳sendTime经过处理后，显示的标准化时间
    private int status;
    private long fileSize;
    private String fileName;
    private String filePath;
    private int fileType;
    private String url;

    public ChatMessage(String userId, long messageId, String sessionId, int messageType, String messageContent, int contactType, String sendUserId, String sendUserNickName, long sendTime, int status, long fileSize, String fileName, String filePath, int fileType, String url) {
        this.userId = userId;
        this.messageId = messageId;
        this.sessionId = sessionId;
        this.messageType = messageType;
        this.messageContent = messageContent;
        this.contactType = contactType;
        this.sendUserId = sendUserId;
        this.sendUserNickName = sendUserNickName;
        this.sendTime = sendTime;
        this.status = status;
        this.fileSize = fileSize;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileType = fileType;
        this.url = url;
        if(sendUserId==null){
            type=TYPE_SYSTEM;
        }else if(sendUserId.equals(userId)){
            type=TYPE_SENT_TEXT;
        }else{
            type=TYPE_RECEIVED_TEXT;
        }
    }

    public ChatMessage(Map<String, Object> map) {
        this.userId = Objects.toString(map.get("userId"), "");

        Object messageIdValue = map.get("messageId");
        if (messageIdValue instanceof Long) {
            this.messageId = (long) messageIdValue;
        } else if (messageIdValue instanceof Integer) {
            this.messageId = (Long) messageIdValue; // 需要强转
        } else {
            this.messageId = 0;
        }

        this.sessionId = Objects.toString(map.get("sessionId"), "");

        Object messageTypeObj = map.get("contactType");
        if (messageTypeObj instanceof Integer) {
            this.messageType = (int) messageTypeObj;
        } else {
            this.messageType = 0;
        }

        this.messageContent = Objects.toString(map.get("messageContent"), "");

        Object contactTypeObj = map.get("contactType");
        if (contactTypeObj instanceof Integer) {
            this.contactType = (int) contactTypeObj;
        } else {
            this.contactType = 0;
        }

        this.sendUserId = Objects.toString(map.get("sendUserId"), "");
        this.sendUserNickName = Objects.toString(map.get("sendUserNickName"), "");

        Object sendTimeObj = map.get("contactType");
        if (sendTimeObj instanceof Long) {
            this.sendTime = (long) sendTimeObj;
        } else if (sendTimeObj instanceof Integer) {
            this.sendTime = ((Integer) sendTimeObj).longValue();
        } else {
            this.sendTime = 0;
        }

        Object statusObj = map.get("contactType");
        if (statusObj instanceof Integer) {
            this.status = (int) statusObj;
        } else {
            this.status = 0;
        }

        Object fileSizeObj = map.get("contactType");
        if (fileSizeObj instanceof Long) {
            this.fileSize = (long) fileSizeObj;
        } else if (fileSizeObj instanceof Integer) {
            this.fileSize = ((Integer) fileSizeObj).longValue();
        } else {
            this.fileSize = 0;
        }

        this.fileName = Objects.toString(map.get("fileName"), "");
        this.filePath = Objects.toString(map.get("filePath"), "");
        this.url = Objects.toString(map.get("url"), "");

        Object fileTypeObj = map.get("contactType");
        if (fileTypeObj instanceof Integer) {
            this.fileType = (int) fileTypeObj;
        } else {
            this.fileType = 0;
        }

        // 根据sendUserId是否为空设置类型
        if (sendUserId.isEmpty()) {
            this.type = TYPE_SYSTEM;
        } else if (sendUserId.equals(userId)) {
            this.type = TYPE_SENT_TEXT;
        } else {
            this.type = TYPE_RECEIVED_TEXT;
        }
    }

//    public ChatMessage(Map<String, Object> map) {
//        this.userId = (String) map.getOrDefault("userId", "");
//        Object messageIdValue = map.getOrDefault("messageId", 0);
//        this.messageId = (messageIdValue instanceof Long) ? (long) messageIdValue : 0;
//        this.sessionId = (String) map.getOrDefault("sessionId", "");
//        Object messageTypeValue = map.getOrDefault("contactType", 0);
//        this.messageType = (messageTypeValue instanceof Integer) ? (int) messageTypeValue : 0;
//        this.messageContent = (String) map.getOrDefault("messageContent", "");
//        Object contactTypeValue = map.getOrDefault("contactType", 0);
//        this.contactType = (contactTypeValue instanceof Integer) ? (int) contactTypeValue : 0;
//        this.sendUserId = (String) map.getOrDefault("sendUserId", "");
//        this.sendUserNickName = (String) map.getOrDefault("sendUserNickName", "");
//        Object sendTimeValue = map.getOrDefault("contactType", 0);
//        this.sendTime = (sendTimeValue instanceof Long) ? (long) sendTimeValue : 0;
//        Object statusValue = map.getOrDefault("contactType", 0);
//        this.status = (statusValue instanceof Integer) ? (int) statusValue : 0;
//        Object fileSizeValue = map.getOrDefault("contactType", 0);
//        this.fileSize = (fileSizeValue instanceof Long) ? (long) fileSizeValue : 0;
//        this.fileName = (String) map.getOrDefault("fileName", "");
//        this.filePath = (String) map.getOrDefault("filePath", "");
//        Object fileTypeValue = map.getOrDefault("contactType", 0);
//        this.fileType = (fileTypeValue instanceof Integer) ? (int) fileTypeValue : 0;
//        this.url = (String) map.getOrDefault("url", "");
//        if(sendUserId==null){
//            type=TYPE_SYSTEM;
//        }else if(sendUserId.equals(userId)){
//            type=TYPE_SENT_TEXT;
//        }else{
//            type=TYPE_RECEIVED_TEXT;
//        }
//    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public int getContactType() {
        return contactType;
    }

    public void setContactType(int contactType) {
        this.contactType = contactType;
    }

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getSendUserNickName() {
        return sendUserNickName;
    }

    public void setSendUserNickName(String sendUserNickName) {
        this.sendUserNickName = sendUserNickName;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public boolean isShowDate() {
        return showDate;
    }

    public void setShowDate(boolean showDate) {
        this.showDate = showDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "userId='" + userId + '\'' +
                ", messageId='" + messageId + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", messageType=" + messageType +
                ", messageContent='" + messageContent + '\'' +
                ", contactType=" + contactType +
                ", sendUserId='" + sendUserId + '\'' +
                ", sendUserNickName='" + sendUserNickName + '\'' +
                ", sendTime=" + sendTime +
                ", date='" + date + '\'' +
                ", status=" + status +
                ", fileSize=" + fileSize +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileType=" + fileType +
                ", url='" + url + '\'' +
                '}';
    }
}

//package com.example.text.dataModel;
//
//public class ChatMessage {
//    public static final int TYPE_RECEIVED = 0; // 接收的消息（左边）
//    public static final int TYPE_SENT = 1;     // 发送的消息（右边）
//
//    private String sender;
//
//    private String content;    // 消息内容
//    private int type;          // 消息类型（TYPE_RECEIVED 或 TYPE_SENT）
//    private String timestamp;  // 时间戳（可选）
//
//    public ChatMessage(String sender,String content, int type, String timestamp) {
//        this.sender = sender;
//        this.content = content;
//        this.type = type;
//        this.timestamp = timestamp;
//    }
//
//    // Getter 方法
//    public String getSender() { return sender; }
//    public String getContent() { return content; }
//    public int getType() { return type; }
//    public String getTimestamp() { return timestamp; }
//}

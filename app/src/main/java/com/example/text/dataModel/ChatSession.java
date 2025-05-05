package com.example.text.dataModel;

public class ChatSession {
    private String userId;
    private String contactID;
    private int contactType;
    private String sessionId;
    private int status;
    private String contactName;
    private String lastMessage;
    private long lastReceiveTime;
    private int noReadCount;
    private int memberCount;
    private int topType;

    public ChatSession(String userId, String contactID, int contactType, String sessionId, int status, String contactName, String lastMessage, long lastReceiveTime, int noReadCount, int memberCount, int topType) {
        this.userId = userId;
        this.contactID = contactID;
        this.contactType = contactType;
        this.sessionId = sessionId;
        this.status = status;
        this.contactName = contactName;
        this.lastMessage = lastMessage;
        this.lastReceiveTime = lastReceiveTime;
        this.noReadCount = noReadCount;
        this.memberCount = memberCount;
        this.topType = topType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContactID() {
        return contactID;
    }

    public void setContactID(String contactID) {
        this.contactID = contactID;
    }

    public int getContactType() {
        return contactType;
    }

    public void setContactType(int contactType) {
        this.contactType = contactType;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getLastReceiveTime() {
        return lastReceiveTime;
    }

    public void setLastReceiveTime(long lastReceiveTime) {
        this.lastReceiveTime = lastReceiveTime;
    }

    public int getNoReadCount() {
        return noReadCount;
    }

    public void setNoReadCount(int noReadCount) {
        this.noReadCount = noReadCount;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public int getTopType() {
        return topType;
    }

    public void setTopType(int topType) {
        this.topType = topType;
    }

    @Override
    public String toString() {
        return "ChatSession{" +
                "userId='" + userId + '\'' +
                ", contactID='" + contactID + '\'' +
                ", contactType=" + contactType +
                ", sessionId='" + sessionId + '\'' +
                ", status=" + status +
                ", contactName='" + contactName + '\'' +
                ", lastMessage='" + lastMessage + '\'' +
                ", lastReceiveTime=" + lastReceiveTime +
                ", noReadCount=" + noReadCount +
                ", memberCount=" + memberCount +
                ", topType=" + topType +
                '}';
    }
}

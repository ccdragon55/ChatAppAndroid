package com.example.text.dataModel;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SessionListItem {
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
    private String url; // 头像

    public SessionListItem(String userId, String contactID, int contactType, String sessionId, int status, String contactName, String lastMessage, long lastReceiveTime, int noReadCount, int memberCount, int topType) {
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

    public SessionListItem(Map<String, Object> map) {
        this.userId = (String) map.getOrDefault("userId", "");
        this.contactID = (String) map.getOrDefault("contactID", "");
        Object contactTypeValue = map.getOrDefault("contactType", 0);
        this.contactType = (contactTypeValue instanceof Integer) ? (int) contactTypeValue : 0;
        this.sessionId = (String) map.getOrDefault("sessionId", "");
        Object statusValue = map.getOrDefault("status", 1);
        this.status = (statusValue instanceof Integer) ? (int) statusValue : 1;
        this.contactName = (String) map.getOrDefault("contactName", "");
        this.lastMessage = (String) map.getOrDefault("lastMessage", "");
        Object lastReceiveTimeValue = map.getOrDefault("lastReceiveTime", 0);
        this.lastReceiveTime = (lastReceiveTimeValue instanceof Long) ? (long) lastReceiveTimeValue : 0;
        Object noReadCountValue = map.getOrDefault("noReadCount", 0);
        this.noReadCount = (noReadCountValue instanceof Integer) ? (int) noReadCountValue : 0;
        Object memberCountValue = map.getOrDefault("memberCount", 1);
        this.memberCount = (memberCountValue instanceof Integer) ? (int) memberCountValue : 1;
        Object topTypeValue = map.getOrDefault("topType", 0);
        this.topType = (topTypeValue instanceof Integer) ? (int) topTypeValue : 0;
        this.url=(String) map.getOrDefault("url", "");
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "SessionListItem{" +
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
                ", url='" + url + '\'' +
                '}';
    }

    public static void sortByLastReceiveTime(List<SessionListItem> list){
        list.sort((f1, f2) -> {
            if (f1.getTopType() != f2.getTopType()) {
                return f2.getTopType() - f1.getTopType();
            }
            return (int) (f2.getLastReceiveTime() - f1.getLastReceiveTime());
        });
//        Collections.sort(list, (f1, f2) -> (int)(f2.getLastReceiveTime()-f1.getLastReceiveTime()));
    }

    public void addNoReadCount(){
        noReadCount++;
    }
}

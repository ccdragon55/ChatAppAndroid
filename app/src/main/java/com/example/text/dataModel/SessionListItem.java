package com.example.text.dataModel;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SessionListItem {
    private String userId;
    private String contactId;
    private int contactType;
    private String sessionId;
    private int status;
    private String contactName;
    private String lastMessage;
    private long lastReceiveTime;
    private String date;//时间戳lastReceiveTime经过处理后，显示的标准化时间
    private int noReadCount;
    private int memberCount;
    private int topType;
    private String avatarUrl; // 头像

    public SessionListItem(String userId, String contactId, int contactType, String sessionId, int status, String contactName, String lastMessage, long lastReceiveTime, int noReadCount, int memberCount, int topType) {
        this.userId = userId;
        this.contactId = contactId;
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
        this.userId = Objects.toString(map.get("userId"), "");
        this.contactId = Objects.toString(map.get("contactId"), "");
        Object contactTypeValue = map.getOrDefault("contactType", 0);
        this.contactType = (contactTypeValue instanceof Long) ? ((Long) contactTypeValue).intValue() : 0;
        this.sessionId = Objects.toString(map.get("sessionId"), "");
        Object statusValue = map.getOrDefault("status", 1);
        this.status = (statusValue instanceof Long) ? ((Long) statusValue).intValue() : 1;
        this.contactName = Objects.toString(map.get("contactName"), "");
        this.lastMessage = Objects.toString(map.get("lastMessage"), "");
        Object lastReceiveTimeValue = map.getOrDefault("lastReceiveTime", 0);
        this.lastReceiveTime = (lastReceiveTimeValue instanceof Long) ? (long) lastReceiveTimeValue : 0;
        Object noReadCountValue = map.getOrDefault("noReadCount", 0);
        this.noReadCount = (noReadCountValue instanceof Long) ? ((Long) noReadCountValue).intValue() : 0;
        Object memberCountValue = map.getOrDefault("memberCount", 1);
        this.memberCount = (memberCountValue instanceof Long) ? ((Long) memberCountValue).intValue() : 1;
        Object topTypeValue = map.getOrDefault("topType", 0);
        this.topType = (topTypeValue instanceof Long) ? ((Long) topTypeValue).intValue() : 0;
        if(map.containsKey("avatarUrl")){
            this.avatarUrl = Objects.toString(map.get("avatarUrl"), "");
        }else if(map.containsKey("url")){
            this.avatarUrl = Objects.toString(map.get("url"), "");
        }

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Override
    public String toString() {
        return "SessionListItem{" +
                "userId='" + userId + '\'' +
                ", contactID='" + contactId + '\'' +
                ", contactType=" + contactType +
                ", sessionId='" + sessionId + '\'' +
                ", status=" + status +
                ", contactName='" + contactName + '\'' +
                ", lastMessage='" + lastMessage + '\'' +
                ", lastReceiveTime=" + lastReceiveTime +
                ", noReadCount=" + noReadCount +
                ", memberCount=" + memberCount +
                ", topType=" + topType +
                ", url='" + avatarUrl + '\'' +
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

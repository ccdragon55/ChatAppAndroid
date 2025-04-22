package com.example.text.dataModel;

import java.util.Collections;
import java.util.List;

public class SessionListItem {
    private String name;
    private String avatar; // 头像

    private String lastMessage;
    private long lastReceiveTime;

    public SessionListItem(String name,String avatar,String lastMessage,long lastReceiveTime){
        this.name=name;
        this.avatar=avatar;
        this.lastMessage=lastMessage;
        this.lastReceiveTime=lastReceiveTime;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public long getLastReceiveTime() {
        return lastReceiveTime;
    }

    public static void sortByLastReceiveTime(List<SessionListItem> list){
        Collections.sort(list, (f1, f2) -> (int)(f2.getLastReceiveTime()-f1.getLastReceiveTime()));
    }
}

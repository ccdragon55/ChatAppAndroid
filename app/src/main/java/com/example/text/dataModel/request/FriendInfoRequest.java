package com.example.text.dataModel.request;

public class FriendInfoRequest {
    private String userId;

    public FriendInfoRequest(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

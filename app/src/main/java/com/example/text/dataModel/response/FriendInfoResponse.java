package com.example.text.dataModel.response;

import com.example.text.dataModel.FriendInfo;

public class FriendInfoResponse {
    private FriendInfo data;

    public FriendInfoResponse(FriendInfo data) {
        this.data = data;
    }

    public FriendInfo getData() {
        return data;
    }

    public void setData(FriendInfo data) {
        this.data = data;
    }
}

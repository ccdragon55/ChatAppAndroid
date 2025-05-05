package com.example.text.dataModel.response;

import com.example.text.dataModel.FriendListItem;

import java.util.List;

public class FetchContactsResponse {
    private List<FriendListItem> data;

    public FetchContactsResponse(List<FriendListItem> data) {
        this.data = data;
    }

    public List<FriendListItem> getData() {
        return data;
    }

    public void setData(List<FriendListItem> data) {
        this.data = data;
    }
}

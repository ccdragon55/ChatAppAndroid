package com.example.text.dataModel.response;

import com.example.text.dataModel.UserInfo;

public class LoginResponse {
    private UserInfo data;

    public UserInfo getData() {
        return data;
    }

    public void setData(UserInfo data) {
        this.data = data;
    }
}
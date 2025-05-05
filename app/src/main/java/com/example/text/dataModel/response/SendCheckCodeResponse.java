package com.example.text.dataModel.response;

import com.example.text.dataModel.CheckCode;

public class SendCheckCodeResponse {
    private CheckCode data;

    public SendCheckCodeResponse(CheckCode data) {
        this.data = data;
    }

    public CheckCode getData() {
        return data;
    }

    public void setData(CheckCode data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SendVerificationCodeResponse{" +
                "data='" + data + '\'' +
                '}';
    }
}

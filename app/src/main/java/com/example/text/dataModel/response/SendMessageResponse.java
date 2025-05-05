package com.example.text.dataModel.response;

import com.example.text.dataModel.SendMessage;

public class SendMessageResponse {
    private SendMessage data;

    public SendMessageResponse(SendMessage data) {
        this.data = data;
    }

    public SendMessage getData() {
        return data;
    }

    public void setData(SendMessage data) {
        this.data = data;
    }
}

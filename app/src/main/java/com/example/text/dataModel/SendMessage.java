package com.example.text.dataModel;

public class SendMessage {
    private String messageId;

    public SendMessage(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}

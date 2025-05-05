package com.example.text.dataModel.request;

public class SendMessageRequest {
    private String contactId;
    private int messageType;
    private String messageContent;

    public SendMessageRequest(String contactId, int messageType, String messageContent) {
        this.contactId = contactId;
        this.messageType = messageType;
        this.messageContent = messageContent;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}

package com.example.text.dataModel.request;

public class AddFriendRequest {
    private String contactId;
    private String applyInfo;

    public AddFriendRequest(String contactId, String applyInfo) {
        this.contactId = contactId;
        this.applyInfo = applyInfo;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getApplyInfo() {
        return applyInfo;
    }

    public void setApplyInfo(String applyInfo) {
        this.applyInfo = applyInfo;
    }
}

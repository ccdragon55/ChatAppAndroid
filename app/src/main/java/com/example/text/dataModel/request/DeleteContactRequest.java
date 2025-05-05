package com.example.text.dataModel.request;

public class DeleteContactRequest {
    private String contactId;

    public DeleteContactRequest(String contactId) {
        this.contactId = contactId;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }
}

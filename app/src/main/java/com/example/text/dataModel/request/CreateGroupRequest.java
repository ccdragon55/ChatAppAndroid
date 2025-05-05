package com.example.text.dataModel.request;

public class CreateGroupRequest {
    private String groupName;
    private String groupNotice;

    public CreateGroupRequest(String groupName, String groupNotice) {
        this.groupName = groupName;
        this.groupNotice = groupNotice;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupNotice() {
        return groupNotice;
    }

    public void setGroupNotice(String groupNotice) {
        this.groupNotice = groupNotice;
    }
}

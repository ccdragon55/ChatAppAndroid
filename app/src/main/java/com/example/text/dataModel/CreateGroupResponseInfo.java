package com.example.text.dataModel;

public class CreateGroupResponseInfo {
    private String groupId;
    private String avatarUrl;

    public CreateGroupResponseInfo(String groupId, String avatarUrl) {
        this.groupId = groupId;
        this.avatarUrl = avatarUrl;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}

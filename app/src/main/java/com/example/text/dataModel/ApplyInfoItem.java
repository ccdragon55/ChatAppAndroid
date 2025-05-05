package com.example.text.dataModel;

public class ApplyInfoItem {
    private String applyId;
    private String avatarUrl;
    private String applyUserNickName;
    private String applyInfo;
    private int status;//-1:已拒绝 0:未处理 1:已同意
    private boolean isExpanded;

    public ApplyInfoItem(String applyId, String avatarUrl, String applyUserNickName, String applyInfo, int status) {
        this.applyId = applyId;
        this.avatarUrl = avatarUrl;
        this.applyUserNickName = applyUserNickName;
        this.applyInfo = applyInfo;
        this.status = status;
        this.isExpanded=false;
    }

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getApplyUserNickName() {
        return applyUserNickName;
    }

    public void setApplyUserNickName(String applyUserNickName) {
        this.applyUserNickName = applyUserNickName;
    }

    public String getApplyInfo() {
        return applyInfo;
    }

    public void setApplyInfo(String applyInfo) {
        this.applyInfo = applyInfo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}

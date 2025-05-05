package com.example.text.dataModel;

public class UserInfo {
    private Boolean admin;
    private Integer contactStatus;
    private Integer joinType;
    private String nickName;
    private String personalSignature;
    private Integer sex;
    private String token;
    private String userId;
    private String avatarUrl;

    public UserInfo(Boolean admin, Integer contactStatus, Integer joinType, String nickName, String personalSignature, Integer sex, String token, String userId, String avatarUrl) {
        this.admin = admin;
        this.contactStatus = contactStatus;
        this.joinType = joinType;
        this.nickName = nickName;
        this.personalSignature = personalSignature;
        this.sex = sex;
        this.token = token;
        this.userId = userId;
        this.avatarUrl = avatarUrl;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public Integer getContactStatus() {
        return contactStatus;
    }

    public void setContactStatus(Integer contactStatus) {
        this.contactStatus = contactStatus;
    }

    public Integer getJoinType() {
        return joinType;
    }

    public void setJoinType(Integer joinType) {
        this.joinType = joinType;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPersonalSignature() {
        return personalSignature;
    }

    public void setPersonalSignature(String personalSignature) {
        this.personalSignature = personalSignature;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "admin=" + admin +
                ", contactStatus=" + contactStatus +
                ", joinType=" + joinType +
                ", nickName='" + nickName + '\'' +
                ", personalSignature='" + personalSignature + '\'' +
                ", sex=" + sex +
                ", token='" + token + '\'' +
                ", userId='" + userId + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
    }
}

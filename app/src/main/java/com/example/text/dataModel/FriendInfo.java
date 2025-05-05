package com.example.text.dataModel;

public class FriendInfo {
    private String userId;
    private String nickName;
    private String avatarUrl;
    private int sex;
    private String email;
    private String personalSignature;

    public FriendInfo(String userId, String nickName, String avatarUrl, int sex, String email, String personalSignature) {
        this.userId = userId;
        this.nickName = nickName;
        this.avatarUrl = avatarUrl;
        this.sex = sex;
        this.email = email;
        this.personalSignature = personalSignature;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPersonalSignature() {
        return personalSignature;
    }

    public void setPersonalSignature(String personalSignature) {
        this.personalSignature = personalSignature;
    }
}

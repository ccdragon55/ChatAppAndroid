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

    public Boolean getAdmin() {
        return admin;
    }

    public Integer getContactStatus() {
        return contactStatus;
    }

    public Integer getJoinType() {
        return joinType;
    }

    public String getNickName() {
        return nickName;
    }

    public Integer getSex() {
        return sex;
    }

    public String getPersonalSignature() {
        return personalSignature;
    }

    public String getToken() {
        return token;
    }

    public String getUserId() {
        return userId;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public void setContactStatus(Integer contactStatus) {
        this.contactStatus = contactStatus;
    }

    public void setJoinType(Integer joinType) {
        this.joinType = joinType;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setPersonalSignature(String personalSignature) {
        this.personalSignature = personalSignature;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

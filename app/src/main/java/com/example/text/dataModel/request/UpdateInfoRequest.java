package com.example.text.dataModel.request;

public class UpdateInfoRequest {
    private String userNickName;
    private int sex;
    private String personalSignature;

    public UpdateInfoRequest(String userNickName, int sex, String personalSignature) {
        this.userNickName = userNickName;
        this.sex = sex;
        this.personalSignature = personalSignature;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPersonalSignature() {
        return personalSignature;
    }

    public void setPersonalSignature(String personalSignature) {
        this.personalSignature = personalSignature;
    }
}

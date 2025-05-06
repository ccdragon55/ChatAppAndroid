package com.example.text.dataModel.request;

public class RegisterRequest {
    String checkCodeKey;
    String email;
    String password;
    String nickname;
    String checkCode;

    public RegisterRequest(String checkCodeKey, String email, String password, String nickname, String checkCode) {
        this.checkCodeKey = checkCodeKey;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.checkCode = checkCode;
    }

    public String getCheckCodeKey() {
        return checkCodeKey;
    }

    public void setCheckCodeKey(String checkCodeKey) {
        this.checkCodeKey = checkCodeKey;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    @Override
    public String toString() {
        return "RegisterRequest{" +
                "checkCodeKey='" + checkCodeKey + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", checkCode='" + checkCode + '\'' +
                '}';
    }
}

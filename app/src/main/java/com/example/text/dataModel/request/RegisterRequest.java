package com.example.text.dataModel.request;

public class RegisterRequest {
    String checkCodeKey;
    String email;
    String password;
    String username;
    String verificationCode;

    public RegisterRequest(String checkCodeKey, String email, String password, String username, String verificationCode) {
        this.checkCodeKey = checkCodeKey;
        this.email = email;
        this.password = password;
        this.username = username;
        this.verificationCode = verificationCode;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    @Override
    public String toString() {
        return "RegisterRequest{" +
                "checkCodeKey='" + checkCodeKey + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", verificationCode='" + verificationCode + '\'' +
                '}';
    }
}

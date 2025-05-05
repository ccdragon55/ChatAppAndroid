package com.example.text.dataModel;

public class CheckCode {
    String checkCode;
    String checkCodeKey;

    public CheckCode(String checkCode, String checkCodeKey) {
        this.checkCode = checkCode;
        this.checkCodeKey = checkCodeKey;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public String getCheckCodeKey() {
        return checkCodeKey;
    }

    public void setCheckCodeKey(String checkCodeKey) {
        this.checkCodeKey = checkCodeKey;
    }

    @Override
    public String toString() {
        return "CheckCode{" +
                "checkCode='" + checkCode + '\'' +
                ", checkCodeKey='" + checkCodeKey + '\'' +
                '}';
    }
}

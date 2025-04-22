package com.example.text.dataModel.response;

public class AvatarResponse {
    private String data;

    public AvatarResponse(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "AvatarResponse{" +
                "data='" + data + '\'' +
                '}';
    }
}

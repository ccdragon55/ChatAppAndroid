package com.example.text.dataModel.response;

import com.example.text.dataModel.CreateGroupResponseInfo;

public class CreateGroupResponse {
    private CreateGroupResponseInfo data;

    public CreateGroupResponse(CreateGroupResponseInfo data) {
        this.data = data;
    }

    public CreateGroupResponseInfo getData() {
        return data;
    }

    public void setData(CreateGroupResponseInfo data) {
        this.data = data;
    }
}

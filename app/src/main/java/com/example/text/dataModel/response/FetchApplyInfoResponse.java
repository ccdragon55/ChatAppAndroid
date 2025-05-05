package com.example.text.dataModel.response;

import com.example.text.dataModel.ApplyInfoItem;

import java.util.List;

public class FetchApplyInfoResponse {
    private List<ApplyInfoItem> data;

    public FetchApplyInfoResponse(List<ApplyInfoItem> data) {
        this.data = data;
    }

    public List<ApplyInfoItem> getData() {
        return data;
    }

    public void setData(List<ApplyInfoItem> data) {
        this.data = data;
    }
}

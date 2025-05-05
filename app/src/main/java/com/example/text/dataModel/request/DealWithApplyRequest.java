package com.example.text.dataModel.request;

public class DealWithApplyRequest {
    private String applyId;
    private String ok;

    public DealWithApplyRequest(String applyId, String ok) {
        this.applyId = applyId;
        this.ok = ok;
    }

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public String getOk() {
        return ok;
    }

    public void setOk(String ok) {
        this.ok = ok;
    }
}

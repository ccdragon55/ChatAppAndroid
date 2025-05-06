package com.example.text.dataModel;

import android.os.Parcel;
import android.os.Parcelable;

public class ApplyInfoItem implements Parcelable {
    private String applyId;
    private String avatarUrl;
    private String applyUserNickName;
    private String applyInfo;
    private int statu;//-1:已拒绝 0:未处理 1:已同意
    private boolean isExpanded;

    public ApplyInfoItem(String applyId, String avatarUrl, String applyUserNickName, String applyInfo, int statu) {
        this.applyId = applyId;
        this.avatarUrl = avatarUrl;
        this.applyUserNickName = applyUserNickName;
        this.applyInfo = applyInfo;
        this.statu = statu;
        this.isExpanded=false;
    }

    // 从 Parcel 中读取数据的构造方法
    protected ApplyInfoItem(Parcel in) {
        applyId = in.readString();
        avatarUrl = in.readString();
        applyUserNickName = in.readString();
        applyInfo = in.readString();
        statu = in.readInt();
        isExpanded = in.readByte() != 0; // 布尔值需要特殊处理
    }

    // 将对象数据写入 Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(applyId);
        dest.writeString(avatarUrl);
        dest.writeString(applyUserNickName);
        dest.writeString(applyInfo);
        dest.writeInt(statu);
        dest.writeByte((byte) (isExpanded ? 1 : 0)); // 布尔值需要特殊处理
    }

    @Override
    public int describeContents() {
        return 0; // 通常返回 0，除非有特殊需求
    }

    // Parcelable 的 Creator，用于从 Parcel 创建对象
    public static final Creator<ApplyInfoItem> CREATOR = new Creator<ApplyInfoItem>() {
        @Override
        public ApplyInfoItem createFromParcel(Parcel in) {
            return new ApplyInfoItem(in);
        }

        @Override
        public ApplyInfoItem[] newArray(int size) {
            return new ApplyInfoItem[size];
        }
    };

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getApplyUserNickName() {
        return applyUserNickName;
    }

    public void setApplyUserNickName(String applyUserNickName) {
        this.applyUserNickName = applyUserNickName;
    }

    public String getApplyInfo() {
        return applyInfo;
    }

    public void setApplyInfo(String applyInfo) {
        this.applyInfo = applyInfo;
    }

    public int getStatu() {
        return statu;
    }

    public void setStatu(int statu) {
        this.statu = statu;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}

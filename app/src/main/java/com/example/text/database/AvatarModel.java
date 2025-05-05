package com.example.text.database;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class AvatarModel extends DBManager{
    private static AvatarModel instance;

    public static synchronized AvatarModel getInstance(Context context) {
        if (instance == null) {
            instance = new AvatarModel(context);
        }
        return instance;
    }
    public AvatarModel(Context context){
        super(context);
    }

    /**
     * 查询指定 contactId 的头像记录
     * @param contactId 用户联系人 ID
     * @return 包含字段 id, url 的 Map；若不存在则返回 null
     */
    public Map<String, Object> selectUserAvatarByContactId(String contactId) {
        String sql = "SELECT * FROM avatar WHERE id = ?";
        String[] params = { String.valueOf(contactId) };
        return queryOne(sql, params);
    }

    /**
     * 更新聊天头像 URL
     * @param contactId 用户联系人 ID
     * @param url 新头像地址
     * @return 影响行数
     */
    public int updateChatAvatar(String contactId, String url) {
        Map<String, Object> updateInfo = new HashMap<>();
        updateInfo.put("url", url);
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("id", contactId);
        return update("avatar", updateInfo, conditions);
    }

    /**
     * 插入一条聊天头像记录，若已存在则忽略
     * @param contactId 用户联系人 ID
     * @param url 头像地址
     * @return 影响行数（0 表示已存在未插入）
     */
    public int addChatAvatar(String contactId, String url) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", contactId);
        data.put("url", url);
        return insertOrIgnore("avatar", data);
    }

    /**
     * 保存头像记录，若已存在则替换
     * @param contactId 用户联系人 ID
     * @param url 头像地址
     * @return 影响行数
     */
    public int saveAvatar(String contactId, String url) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", contactId);
        data.put("url", url);
        return insertOrReplace("avatar", data);
    }
}

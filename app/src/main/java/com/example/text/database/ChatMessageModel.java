package com.example.text.database;

import android.content.Context;

import com.example.text.utils.Store;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class ChatMessageModel extends DBManager{
    private static ChatMessageModel instance;
    private Context context;

    public static synchronized ChatMessageModel getInstance(Context context) {
        if (instance == null) {
            instance = new ChatMessageModel(context);
        }
        return instance;
    }
    public ChatMessageModel(Context context){
        super(context);
        this.context=context;
    }

    /**
     * 保存或替换单条聊天消息
     * @param data 键值对，包含 chat_message 表中的字段
     * @return 影响行数
     */
    public int saveMessage(Map<String, Object> data) {
        // 假定有一个 Store 工具类获取当前用户 ID
        String userId = Store.getInstance(context).getUserId();
        data.put("userId", userId);
        return insertOrReplace("chat_message", data);
    }

    /**
     * 批量保存聊天消息，并更新会话未读数
     * @param chatMessageList 每项为 chat_message 表字段的键值对
     */
    public void saveChatMessageBatch(List<Map<String, Object>> chatMessageList) {
        ChatSessionModel chatSessionModel=new ChatSessionModel(context);
        // 统计每个会话的新消息数
        Map<String, Integer> sessionCountMap = new HashMap<>();
        for (Map<String, Object> item : chatMessageList) {
            String contactId;
            int contactType = (int) item.getOrDefault("contactType", 1);
            if (contactType == 1) {
                contactId = (String) item.get("contactId");
            } else {
                contactId = (String) item.get("sendUserId");
            }
            int count = sessionCountMap.containsKey(contactId) ? sessionCountMap.get(contactId) : 0;
            sessionCountMap.put(contactId, count + 1);
        }
        // 更新未读数
        for (Map.Entry<String, Integer> entry : sessionCountMap.entrySet()) {
            String contactId = entry.getKey();
            int noReadCount = entry.getValue();
            chatSessionModel.updateNoReadCount(contactId, noReadCount);
        }
        // 插入或替换每条消息
        for (Map<String, Object> item : chatMessageList) {
            saveMessage(item);
        }
    }

    /**
     * 查询当前用户、指定会话的消息总数
     * @param sessionId 会话 ID
     * @return 消息总数
     */
    public int getTotalMessageCount(String sessionId) {
        String sql = "SELECT COUNT(*) FROM chat_message WHERE user_id = ? AND session_id = ?";
        String[] params = { String.valueOf(Store.getInstance(context).getUserId()), String.valueOf(sessionId) };
        return queryCount(sql, params);
    }

    /**
     * 分页查询聊天消息（倒序）
     * @param sessionId 会话 ID
     * @param currentPage 当前页（从0开始）
     * @param pageSize 每页条数
     * @param startTime 查询起始时间（毫秒）
     * @return List\<Map\<String,Object\>\>，每项包含 chat_message + avatar 表字段（已驼峰化）
     */
    public List<Map<String, Object>> selectChatMessageByPage(String sessionId, int currentPage, int pageSize, long startTime) {
        String sql = "SELECT cm.*, a.url AS avatarUrl FROM chat_message cm " +
                "LEFT JOIN avatar a ON cm.send_user_id = a.id " +
                "WHERE cm.user_id = ? AND cm.session_id = ? AND cm.send_time < ? " +
                "ORDER BY cm.send_time DESC LIMIT ?, ?";
        String[] params = {
                String.valueOf(Store.getInstance(context).getUserId()),
                String.valueOf(sessionId),
                String.valueOf(startTime),
                String.valueOf(currentPage * pageSize),
                String.valueOf(pageSize)
        };
        return queryAll(sql, params);
    }
}


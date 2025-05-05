package com.example.text.database;

import android.content.Context;
import android.util.Log;

import com.example.text.utils.Store;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class ChatSessionModel extends DBManager{
    private static ChatSessionModel instance;
    private Context context;

    public static synchronized ChatSessionModel getInstance(Context context) {
        if (instance == null) {
            instance = new ChatSessionModel(context);
        }
        return instance;
    }
    public ChatSessionModel(Context context){
        super(context);
        this.context=context;
    }

    /**
     * 更新会话信息
     */
    public int updateChatSession(Map<String, Object> sessionInfo) {
        String userId = Store.getInstance(context).getUserId();
        String contactId = (String) sessionInfo.get("contactId");

        Map<String, Object> conditions = new HashMap<>();
        conditions.put("userId", userId);
        conditions.put("contactId", contactId);

        // 准备更新内容：排除主键字段
        Map<String, Object> updateInfo = new HashMap<>(sessionInfo);
        updateInfo.remove("userId");
        updateInfo.remove("contactId");

        return update("chat_session_user", updateInfo, conditions);
    }

    /**
     * 初始化或批量保存/更新会话
     */
    public void saveOrUpdateChatSessionBatch4Init(List<Map<String, Object>> chatSessionList) {
        Log.e("fk", "saveOrUpdateChatSessionBatch4Init: ");
        AvatarModel avatarModel=new AvatarModel(context);
        for (Map<String, Object> sessionInfo : chatSessionList) {
            // 标记为有效
            sessionInfo.put("status", 1);
            String contactId = (String) sessionInfo.get("contactId");

            // 同步头像
            Object raw = sessionInfo.get("avatarUrl");
            String avatarUrl = (raw instanceof String) ? (String) raw : null;
            Map<String, Object> avatarData = avatarModel.selectUserAvatarByContactId(contactId);
            if (avatarUrl != null && !avatarUrl.isEmpty()) {
                if (avatarData != null) {
                    avatarModel.updateChatAvatar(contactId, avatarUrl);
                } else {
                    avatarModel.addChatAvatar(contactId, avatarUrl);
                }
            }
            sessionInfo.remove("avatarUrl");

            // 判断会话是否存在
            Map<String, Object> sessionData = selectUserSessionByContactId(contactId);
            if (sessionData != null) {
                updateChatSession(sessionInfo);
            } else {
                addChatSession(sessionInfo);
            }
        }
    }

    /**
     * 保存或替换会话
     */
    public int saveSession(Map<String, Object> data) {
        data.put("userId", Store.getInstance(context).getUserId());
        return insertOrReplace("chat_session_user", data);
    }

    /**
     * 根据 contactId 查询单条会话
     */
    public Map<String, Object> selectUserSessionByContactId(String contactId) {
        String sql = "SELECT * FROM chat_session_user WHERE user_id = ? AND contact_id = ?";
        String[] params = {
                String.valueOf(Store.getInstance(context).getUserId()),
                String.valueOf(contactId)
        };
        return queryOne(sql, params);
    }

    /**
     * 新增会话（若存在则忽略）
     */
    public int addChatSession(Map<String, Object> sessionInfo) {
        sessionInfo.put("userId", Store.getInstance(context).getUserId());
        return insertOrIgnore("chat_session_user", sessionInfo);
    }

    /**
     * 增加未读数
     */
    public int updateNoReadCount(String contactId, int noReadCount) {
        String sql = "UPDATE chat_session_user SET no_read_count = no_read_count + ? WHERE user_id = ? AND contact_id = ?";
        Object[] params = {
                noReadCount,
                Store.getInstance(context).getUserId(),
                contactId
        };
        return run(sql, params);
    }

    /**
     * 获取用户所有会话列表
     */
    public List<Map<String, Object>> selectUserSessionList() {
        String sql = "SELECT csu.*, a.url AS avatarUrl " +
                "FROM chat_session_user csu " +
                "LEFT JOIN avatar a ON csu.contact_id = a.id " +
                "WHERE csu.user_id = ? AND csu.status = 1";
        String[] params = { String.valueOf(Store.getInstance(context).getUserId()) };
        return queryAll(sql, params);
    }

    /**
     * 置顶/显示会话
     */
    public int showChatSession(String contactId) {
        Map<String, Object> info = new HashMap<>();
        info.put("status", 1);
        Map<String, Object> cond = new HashMap<>();
        cond.put("userId", Store.getInstance(context).getUserId());
        cond.put("contactId", contactId);
        return update("chat_session_user", info, cond);
    }

    /**
     * 隐藏/删除会话
     */
    public int delChatSession(String contactId) {
        Map<String, Object> info = new HashMap<>();
        info.put("status", 0);
        Map<String, Object> cond = new HashMap<>();
        cond.put("userId", Store.getInstance(context).getUserId());
        cond.put("contactId", contactId);
        return update("chat_session_user", info, cond);
    }

    /**
     * 获取会话状态
     */
    public int getChatSessionStatus(String contactId) {
        String sql = "SELECT status FROM chat_session_user WHERE user_id = ? AND contact_id = ?";
        String[] params = {
                String.valueOf(Store.getInstance(context).getUserId()),
                String.valueOf(contactId)
        };
        Map<String, Object> row = queryOne(sql, params);
        return row != null && row.get("status") != null
                ? ((Number) row.get("status")).intValue()
                : 0;
    }

    /**
     * 根据 contactId 获取会话及头像
     */
    public Map<String, Object> getChatSessionByContactId(String contactId) {
        String sql = "SELECT csu.*, a.url AS avatarUrl " +
                "FROM chat_session_user csu " +
                "LEFT JOIN avatar a ON csu.contact_id = a.id " +
                "WHERE csu.user_id = ? AND csu.contact_id = ?";
        String[] params = {
                String.valueOf(Store.getInstance(context).getUserId()),
                String.valueOf(contactId)
        };
        return queryOne(sql, params);
    }

    /**
     * 模糊搜索会话
     */
    public List<Map<String, Object>> searchChatSessionByCondition(String condition) {
        String pattern = "%" + condition.replace(" ", "%") + "%";
        String sql = "SELECT csu.*, a.url AS avatarUrl " +
                "FROM chat_session_user csu " +
                "LEFT JOIN avatar a ON csu.contact_id = a.id " +
                "WHERE csu.user_id = ? AND csu.status = 1 AND csu.contact_name LIKE ?";
        String[] params = {
                String.valueOf(Store.getInstance(context).getUserId()),
                pattern
        };
        return queryAll(sql, params);
    }

    /**
     * 根据 sessionId 查询会话
     */
    public List<Map<String, Object>> selectSessionBySessionId(String sessionId) {
        String sql = "SELECT csu.*, a.url AS avatarUrl " +
                "FROM chat_session_user csu " +
                "LEFT JOIN avatar a ON csu.contact_id = a.id " +
                "WHERE csu.user_id = ? AND csu.session_id = ?";
        String[] params = {
                String.valueOf(Store.getInstance(context).getUserId()),
                String.valueOf(sessionId)
        };
        return queryAll(sql, params);
    }

    /**
     * 更新最后消息信息
     */
    public int updateChatSessionLastInfo(String contactId, String lastMessage, long lastReceiveTime) {
        Map<String, Object> info = new HashMap<>();
        info.put("lastMessage", lastMessage);
        info.put("lastReceiveTime", lastReceiveTime);
        Map<String, Object> cond = new HashMap<>();
        cond.put("userId", Store.getInstance(context).getUserId());
        cond.put("contactId", contactId);
        return update("chat_session_user", info, cond);
    }

    /**
     * 更新最后消息并显示
     */
    public int updateChatSessionLastInfoAndShow(String contactId, String lastMessage, long lastReceiveTime) {
        Map<String, Object> info = new HashMap<>();
        info.put("lastMessage", lastMessage);
        info.put("lastReceiveTime", lastReceiveTime);
        info.put("status", 1);
        Map<String, Object> cond = new HashMap<>();
        cond.put("userId", Store.getInstance(context).getUserId());
        cond.put("contactId", contactId);
        return update("chat_session_user", info, cond);
    }

    /**
     * 更新群组名称
     */
    public int updateGroupName(String contactId, String contactName) {
        Map<String, Object> info = new HashMap<>();
        info.put("contactName", contactName);
        Map<String, Object> cond = new HashMap<>();
        cond.put("contactId", contactId);
        return update("chat_session_user", info, cond);
    }

    /**
     * 设置置顶类型
     */
    public int setTopSession(String contactId, int topType) {
        Map<String, Object> info = new HashMap<>();
        info.put("topType", topType);
        Map<String, Object> cond = new HashMap<>();
        cond.put("userId", Store.getInstance(context).getUserId());
        cond.put("contactId", contactId);
        return update("chat_session_user", info, cond);
    }

    /**
     * 清除未读数
     */
    public int clearNoReadCount(String sessionId) {
        Map<String, Object> info = new HashMap<>();
        info.put("noReadCount", 0);
        Map<String, Object> cond = new HashMap<>();
        cond.put("userId", Store.getInstance(context).getUserId());
        cond.put("sessionId", sessionId);
        return update("chat_session_user", info, cond);
    }
}

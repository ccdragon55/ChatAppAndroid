package com.example.text.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class Store {
    private static final String PREF_NAME = "app_prefs";
    private static Store instance;
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;
    private String userId;

    private Store(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public static synchronized Store getInstance(Context context) {
        if (instance == null) {
            instance = new Store(context);
        }
        return instance;
    }

    /**
     * 初始化当前用户 ID（登录后调用）
     */
    public void initUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 获取当前用户 ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 存储通用键值对（String 类型）
     */
    public void setData(String key, String value) {
        editor.putString(key, value).apply();
    }

    /**
     * 获取通用键值对（String 类型）
     */
    public String getData(String key) {
        return prefs.getString(key, null);
    }

    /**
     * 存储当前用户的键值对
     */
    public void setUserData(String key, String value) {
        if (userId != null) {
            setData(userId + "_" + key, value);
        }
    }

    /**
     * 获取当前用户的键值对
     */
    public String getUserData(String key) {
        if (userId != null) {
            return getData(userId + "_" + key);
        }
        return null;
    }

    /**
     * 删除当前用户的键值对
     */
    public void deleteUserData(String key) {
        if (userId != null) {
            editor.remove(userId + "_" + key).apply();
        }
    }

    /**
     * 清空Store
     */
    public void clear(){
        editor.clear().apply();
    }
}


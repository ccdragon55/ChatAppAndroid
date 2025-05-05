package com.example.text.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JsonUtils {
    private static final Gson gson = new Gson();

    // 解析 JSON 字符串到对象
    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    // 解析 JSON 字符串到对象列表
    public static <T> List<T> fromJsonList(String json, Class<T> clazz) {
        Type type = TypeToken.getParameterized(List.class, clazz).getType();
        return gson.fromJson(json, type);
    }

    // 将对象转为 JSON 字符串
    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    public static Map<String,Object> jsonToStrObjMap(JSONObject jsonObject) throws JSONException {
        Map<String, Object> map = new HashMap<>();
        Iterator<String> newMessageIt = jsonObject.keys();
        while (newMessageIt.hasNext()) {
            String key = newMessageIt.next();
            map.put(key, jsonObject.get(key));
        }
        return map;
    }
}

package com.example.text.apis;

import com.example.text.BuildConfig;

public class ApiConfig {
    private static String baseUrl = BuildConfig.API_BASE_URL;

    public static void setBaseUrl(String url) {
        if (!url.endsWith("/")) url += "/";  // 确保路径规范
        baseUrl = url;
    }

    public static String getBaseUrl() {
        return baseUrl;
    }
}
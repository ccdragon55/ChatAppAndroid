package com.example.text.apis;

import com.example.text.BuildConfig;

public class ApiConfig {
    private static String baseUrl = BuildConfig.API_BASE_URL;
    private static String sipBaseUrl = BuildConfig.SIP_BASE_URL;

    public static void setBaseUrl(String url) {
        if (!url.endsWith("/")) url += "/";  // 确保路径规范
        baseUrl = url;
    }

    public static void setSipBaseUrl(String url) {
        if (!url.endsWith("/")) url += "/";  // 确保路径规范
        sipBaseUrl = url;
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

    public static String getSipBaseUrl() {
        return sipBaseUrl;
    }
}
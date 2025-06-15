package com.example.text.sip;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class LinphoneUtils {
    public static boolean checkIfNetworkHasLowBandwidth(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnected()) {
                int type = activeNetwork.getType();
                return type == ConnectivityManager.TYPE_MOBILE; // 假设移动网络为低带宽
            }
        }
        return false;
    }
}
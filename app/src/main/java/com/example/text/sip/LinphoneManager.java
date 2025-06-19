package com.example.text.sip;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.text.sip.test.IncomingCallActivity;

import org.linphone.core.*;

import java.util.Timer;
import java.util.TimerTask;

public class LinphoneManager {
    private static LinphoneManager instance;
    private Core core;
    private Context context;

    private LinphoneManager(Context context) {
        this.context = context;
        // 初始化 LinphoneCore
        Factory factory = Factory.instance();
        factory.setDebugMode(true, "LinphoneDemo");
        core = factory.createCore(null, null, context);

        core.setVideoCaptureEnabled(true);
        core.setVideoDisplayEnabled(true);
        core.setVideoPreviewEnabled(true);
        core.setVideoSourceReuseEnabled(true);

        // 配置视频激活策略
        VideoActivationPolicy videoPolicy = core.getVideoActivationPolicy().clone();
//        videoPolicy = factory.createVideoActivationPolicy();
        videoPolicy.setAutomaticallyInitiate(true);
        videoPolicy.setAutomaticallyAccept(true);
        core.setVideoActivationPolicy(videoPolicy);

        // 启用常用视频编解码器
        for (PayloadType codec : core.getVideoPayloadTypes()) {
            if (codec.getMimeType().equals("H264") || codec.getMimeType().equals("VP8")) {
                codec.enable(true);
                Log.d("Linphone", "Enabled codec: " + codec.getMimeType());
            } else {
                codec.enable(false);
            }
        }

        // 设置合理的视频分辨率
        core.setPreferredVideoDefinitionByName("720p"); // 或 "VGA" 以降低带宽需求
        core.setPreferredFramerate(15); // 设置较低帧率以提高兼容性

        // 配置网络设置
        core.setRtpBundleEnabled(true); // 优化 RTP 传

        core.start();

        // 添加核心监听器
        core.addListener(new CoreListenerStub() {
            @Override
            public void onAccountRegistrationStateChanged(Core core, Account account, RegistrationState state, String message) {
                Log.i("Linphone", "Account registration state: " + state + ", message: " + message);
            }

            @Override
            public void onCallStateChanged(Core core, Call call, Call.State state, String message) {
                if (state == Call.State.IncomingReceived) {
                    Intent intent = new Intent(context, IncomingCallActivity.class);
                    intent.putExtra("remoteAddress", call.getRemoteAddress().asString());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else if (state == Call.State.Connected) {
                    Log.i("Linphone", "Call connected!");
                } else if (state == Call.State.End || state == Call.State.Released) {
                    Log.i("Linphone", "Call ended: " + message);
                }
            }
        });

        TimerTask iterateTask = new TimerTask() {
            @Override
            public void run() {
                core.iterate();
            }
        };
        Timer timer = new Timer("Linphone Iterate");
        timer.schedule(iterateTask, 0, 20);
    }

    public static synchronized LinphoneManager getInstance(Context context) {
        if (instance == null) {
//            instance = new LinphoneManager(context.getApplicationContext());
            instance = new LinphoneManager(context);
        }
        return instance;
    }

    public Core getCore() {
        return core;
    }

    public void createProxyConfig(String username, String password, String domain, TransportType transport) {
        try {
            // 创建 SIP 身份地址
            String identity = "sip:" + username + "@" + domain;
            Address identityAddress = Factory.instance().createAddress(identity);
            identityAddress.setPort(5060);

            // 创建服务器地址
            Address serverAddress = Factory.instance().createAddress("sip:" + domain);
            serverAddress.setPort(5060);

            // 创建 AccountParams
            AccountParams params = core.createAccountParams();
            params.setIdentityAddress(identityAddress);
            params.setServerAddress(serverAddress);
            params.setTransport(transport);
            params.setRegisterEnabled(true);

            // 创建 AuthInfo
            AuthInfo authInfo = Factory.instance().createAuthInfo(username, null, password, null, null, domain, null);
            core.addAuthInfo(authInfo);

            // 创建 Account 并添加到 Core
            Account account = core.createAccount(params);
            core.addAccount(account);
            core.setDefaultAccount(account);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makeCall(String destination, boolean isVideoCall) {
        try {
            Address addressToCall = Factory.instance().createAddress("sip:" + destination);
            CallParams params = core.createCallParams(null);
            params.setVideoEnabled(isVideoCall);
            if (isVideoCall) {
                params.setVideoDirection(MediaDirection.SendRecv); // 设置视频方向为发送和接收
                params.setEarlyMediaSendingEnabled(true); // 启用早期媒体以便协商
            }

            // 优化低带宽网络
            if (LinphoneUtils.checkIfNetworkHasLowBandwidth(context)) {
                core.setUploadBandwidth(512); // 设置上行带宽限制（kbps）
                core.setDownloadBandwidth(512); // 设置下行带宽限制（kbps）
                Log.w("Linphone", "Setting low bandwidth limits!");
            } else {
                core.setUploadBandwidth(0); // 0 表示无限制
                core.setDownloadBandwidth(0);
            }

            core.inviteAddressWithParams(addressToCall, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

//import android.content.Context;
//import android.content.Intent;
//import android.util.Log;
//
//import org.linphone.core.*;
//
//public class LinphoneManager {
//    private static LinphoneManager instance;
//    private Core core;
//    private Context context;
//
//    private LinphoneManager(Context context) {
//        this.context = context;
//        // 初始化 LinphoneCore
//        Factory factory = Factory.instance();
//        factory.setDebugMode(true, "LinphoneDemo");
//        core = factory.createCore(null, null, context);
//
//        // 配置视频激活策略
//        VideoActivationPolicy videoPolicy = core.getVideoActivationPolicy();
//        if (videoPolicy == null) {
//            videoPolicy = factory.createVideoActivationPolicy();
//            videoPolicy.setAutomaticallyInitiate(true);
//            videoPolicy.setAutomaticallyAccept(true);
//            core.setVideoActivationPolicy(videoPolicy);
//        }
//
//        core.start();
//
//        // 添加核心监听器
//        core.addListener(new CoreListenerStub() {
//            @Override
//            public void onAccountRegistrationStateChanged(Core core, Account account, RegistrationState state, String message) {
//                Log.i("Linphone", "Account registration state: " + state + ", message: " + message);
//            }
//
//            @Override
//            public void onCallStateChanged(Core core, Call call, Call.State state, String message) {
//                if (state == Call.State.IncomingReceived) {
//                    Intent intent = new Intent(context, IncomingCallActivity.class);
//                    intent.putExtra("remoteAddress", call.getRemoteAddress().asString());
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent);
//                } else if (state == Call.State.Connected) {
//                    Log.i("Linphone", "Call connected!");
//                } else if (state == Call.State.End || state == Call.State.Released) {
//                    Log.i("Linphone", "Call ended: " + message);
//                }
//            }
//        });
//    }
//
//    public static synchronized LinphoneManager getInstance(Context context) {
//        if (instance == null) {
//            instance = new LinphoneManager(context.getApplicationContext());
//        }
//        return instance;
//    }
//
//    public Core getCore() {
//        return core;
//    }
//
//    public void createProxyConfig(String username, String password, String domain, TransportType transport) {
//        try {
//            // 创建 SIP 身份地址
//            String identity = "sip:" + username + "@" + domain;
//            Address identityAddress = Factory.instance().createAddress(identity);
//
//            // 创建 AccountParams
//            AccountParams params = core.createAccountParams();
//            params.setIdentityAddress(identityAddress);
//            params.setServerAddress("sip:" + domain);
//            params.setTransport(transport);
//            params.setRegisterEnabled(true);
//
//            // 创建 AuthInfo
//            AuthInfo authInfo = Factory.instance().createAuthInfo(username, null, password, null, null, domain, null);
//            core.addAuthInfo(authInfo);
//
//            // 创建 Account 并添加到 Core
//            Account account = core.createAccount(params);
//            core.addAccount(account);
//            core.setDefaultAccount(account);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void makeCall(String destination, boolean isVideoCall) {
//        try {
//            Address addressToCall = Factory.instance().createAddress("sip:" + destination);
//            CallParams params = core.createCallParams(null);
//            params.setVideoEnabled(isVideoCall);
//            if (isVideoCall) {
//                params.setVideoDirection(MediaDirection.SendRecv); // 设置视频方向为发送和接收
//            }
//
//            // 优化低带宽网络
//            if (LinphoneUtils.checkIfNetworkHasLowBandwidth(context)) {
//                params.setBandwidthLimit(512); // 限制带宽到 512kbps
//                Log.w("Linphone", "Setting low bandwidth limit!");
//            }
//
//            core.inviteAddressWithParams(addressToCall, params);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
package com.example.text.sip;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.example.text.sip.test.IncomingCallActivity;
import org.linphone.core.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LinphoneManager {
    private static LinphoneManager instance;
    private Core core;
    private Context context;
    private ScheduledExecutorService executor;
    private Runnable iterateRunnable;

    public static final String ACTION_CALL_STATE_CHANGED = "call_state_changed";
    public static final String EXTRA_CALL_OUTGOINGINIT = "call_outgoingInit";
    public static final String EXTRA_CALL_OUTGOINGPROGRESS = "call_outgoingProgress";
    public static final String EXTRA_CALL_CONNECTED = "call_connected";
    public static final String EXTRA_CALL_STREAMSRUNNING = "call_streamsRunning";
    public static final String EXTRA_CALL_END_OR_RELEASED = "call_end_or_released";

    private LinphoneManager(Context context) {
        this.context = context.getApplicationContext(); // 使用 ApplicationContext 避免内存泄漏
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
        videoPolicy.setAutomaticallyInitiate(true);
        videoPolicy.setAutomaticallyAccept(true);
        core.setVideoActivationPolicy(videoPolicy);

        core.setRtpBundleEnabled(false); // 禁用 RTP 捆绑，增加兼容性

        // 启用常用音频编解码器
        for (PayloadType codec : core.getAudioPayloadTypes()) {
            if (codec.getMimeType().equals("opus") || codec.getMimeType().equals("PCMU") || codec.getMimeType().equals("PCMA")) {
                codec.enable(true);
                Log.d("LinphoneManager", "Enabled audio codec: " + codec.getMimeType());
            }
        }

        // 启用常用视频编解码器
        for (PayloadType codec : core.getVideoPayloadTypes()) {
            if (codec.getMimeType().equals("H264") || codec.getMimeType().equals("VP8")) {
                codec.enable(true);
                Log.d("LinphoneManager", "Enabled codec: " + codec.getMimeType());
            } else {
                codec.enable(false);
            }
        }

        // 设置合理的视频分辨率
        core.setPreferredVideoDefinitionByName("720p"); // 或 "VGA" 以降低带宽需求
        core.setPreferredFramerate(15); // 设置较低帧率以提高兼容性

        // 配置网络设置
        core.setRtpBundleEnabled(true); // 优化 RTP 传输

        core.start();

        // 添加核心监听器
        core.addListener(new CoreListenerStub() {
            @Override
            public void onAccountRegistrationStateChanged(Core core, Account account, RegistrationState state, String message) {
                Log.i("LinphoneManager", "Account registration state: " + state + ", message: " + message);
            }

            @Override
            public void onCallStateChanged(Core core, Call call, Call.State state, String message) {
                // 发送广播
                Intent broadcastIntent = new Intent(ACTION_CALL_STATE_CHANGED);
                if (state == Call.State.IncomingReceived && call.getDir() == Call.Dir.Incoming) {
                    Log.i("LinphoneManager", "Incoming call from: " + call.getRemoteAddress().asString());
                    Log.i("LinphoneManager", "Is audio call: " + call.getCurrentParams().isAudioEnabled());
                    Log.i("LinphoneManager", "Is video call: " + call.getCurrentParams().isVideoEnabled());
                    Intent intent = new Intent(context, IncomingCallActivity.class);
                    intent.putExtra("remoteAddress", call.getRemoteAddress().asString());
                    intent.putExtra("isVideoCall", call.getCurrentParams().isVideoEnabled());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else if (state == Call.State.OutgoingInit) {
                    Log.i("LinphoneManager", "Call OutgoingInit!");
                    broadcastIntent.putExtra("callStatus", EXTRA_CALL_OUTGOINGINIT);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
                } else if (state == Call.State.OutgoingProgress) {
                    Log.i("LinphoneManager", "Call OutgoingProgress!");
                    broadcastIntent.putExtra("callStatus", EXTRA_CALL_OUTGOINGPROGRESS);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
                } else if (state == Call.State.Connected) {
                    Log.i("LinphoneManager", "Call connected!");
                    broadcastIntent.putExtra("callStatus", EXTRA_CALL_CONNECTED);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
                } else if (state == Call.State.StreamsRunning) {
                    Log.i("LinphoneManager", "Call StreamsRunning: " + message);
                    Log.i("LinphoneManager", "StreamsRunning Is audio enabled: " + call.getCurrentParams().isAudioEnabled());
                    Log.i("LinphoneManager", "StreamsRunning Is video enabled: " + call.getCurrentParams().isVideoEnabled());
                    broadcastIntent.putExtra("callStatus", EXTRA_CALL_STREAMSRUNNING);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
                } else if (state == Call.State.End || state == Call.State.Released) {
                    Log.i("LinphoneManager", "Call ended: " + message);
                    broadcastIntent.putExtra("callStatus", EXTRA_CALL_END_OR_RELEASED);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
                }
            }
        });

        // 初始化 ScheduledExecutorService
        executor = Executors.newSingleThreadScheduledExecutor();
        iterateRunnable = () -> {
            try {
                core.iterate(); // 在后台线程调用
            } catch (Exception e) {
                Log.e("LinphoneManager", "Error in core.iterate()", e);
            }
        };
        // 每 50ms 调用一次 core.iterate()
        executor.scheduleAtFixedRate(iterateRunnable, 0, 50, TimeUnit.MILLISECONDS);
    }

    public static synchronized LinphoneManager getInstance(Context context) {
        if (instance == null) {
            instance = new LinphoneManager(context.getApplicationContext());
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
            Log.e("LinphoneManager", "Error creating proxy config", e);
        }
    }

    public void makeCall(String destination, boolean isVideoCall) {
        try {
            Address addressToCall = Factory.instance().createAddress("sip:" + destination);
            CallParams params = core.createCallParams(null);
            params.setAudioEnabled(true);
            params.setVideoEnabled(isVideoCall);
            if (isVideoCall) {
                Log.i("LinphoneManager", "true");
                params.setVideoDirection(MediaDirection.SendRecv);
                params.setEarlyMediaSendingEnabled(true);
            }else{
                Log.i("LinphoneManager", "false");
            }

            // 优化低带宽网络
            if (LinphoneUtils.checkIfNetworkHasLowBandwidth(context)) {
                core.setUploadBandwidth(512);
                core.setDownloadBandwidth(512);
                Log.w("LinphoneManager", "Setting low bandwidth limits!");
            } else {
                core.setUploadBandwidth(0);
                core.setDownloadBandwidth(0);
            }

            Call call =core.inviteAddressWithParams(addressToCall, params);
            if (call != null) {
                Log.i("LinphoneManager", "Call initiated, audio enabled: " + call.getCurrentParams().isAudioEnabled());
                Log.i("LinphoneManager", "Call initiated, video enabled: " + call.getCurrentParams().isVideoEnabled());
            }
        } catch (Exception e) {
            Log.e("LinphoneManager", "Error making call", e);
        }
    }

    public void acceptCall(Call call, boolean enableVideo) {
        try {
            CallParams params = core.createCallParams(call);
            params.setAudioEnabled(true); // 强制启用音频
            params.setVideoEnabled(enableVideo);
            call.setCameraEnabled(true);
            if (enableVideo) {
                params.setVideoDirection(MediaDirection.SendRecv);
                Log.i("LinphoneManager", "Accepting call with video enabled");
            }
            call.acceptWithParams(params);
            Log.i("LinphoneManager", "Call accepted, audio enabled: " + call.getCurrentParams().isAudioEnabled());
        } catch (Exception e) {
            Log.e("LinphoneManager", "Error accepting call", e);
        }
    }

    // 添加清理方法
    public void destroy() {
        if (executor != null) {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt(); // 恢复中断状态
            }
        }
        if (core != null) {
            core.stop();
            core = null;
        }
        instance = null;
    }
}
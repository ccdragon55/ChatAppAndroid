package com.example.text.sip.test;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.TextureView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.text.R;
import com.example.text.sip.LinphoneManager;

import org.linphone.core.Call;
import org.linphone.core.CallStats;
import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;

import java.util.Objects;

public class CallActivity extends AppCompatActivity {
    private Intent intent;
    private TextureView localVideoTexture;
    private TextureView remoteVideoTexture;
    private Chronometer chronometer;
    private TextView textViewInfo;

    private final BroadcastReceiver callStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (LinphoneManager.ACTION_CALL_STATE_CHANGED.equals(intent.getAction())) {
                String callStatus = intent.getStringExtra("callStatus");
                Log.d("CallActivity", "Received broadcast: state=" + callStatus);

                if (Objects.equals(callStatus, LinphoneManager.EXTRA_CALL_OUTGOINGINIT)) {
                    Log.i("Call", "EXTRA_CALL_OUTGOINGINIT");
                    runOnUiThread(() -> {
                        textViewInfo.setText("正在拨号...");
                    });
                }else if (Objects.equals(callStatus, LinphoneManager.EXTRA_CALL_OUTGOINGPROGRESS)) {
                    Log.i("Call", "EXTRA_CALL_OUTGOINGPROGRESS");
                    runOnUiThread(() -> {
                        textViewInfo.setText("对方响铃中...");
                    });
                }else if (Objects.equals(callStatus, LinphoneManager.EXTRA_CALL_CONNECTED)) {
                    Log.i("Call", "EXTRA_CALL_CONNECTED");
                    runOnUiThread(() -> {
                        textViewInfo.setText("对方已接听...");
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        chronometer.start();
                    });
                }else if (Objects.equals(callStatus, LinphoneManager.EXTRA_CALL_STREAMSRUNNING)) {
                    runOnUiThread(() -> {
                        textViewInfo.setText("通话中...");
                    });
                    Log.i("Call", "Media streams are running (audio + video)");
                }else if (Objects.equals(callStatus, LinphoneManager.EXTRA_CALL_END_OR_RELEASED)) {
                    Log.i("Call", "end");
                    runOnUiThread(() -> {
                        textViewInfo.setText("通话结束...");
                        chronometer.stop();
                        finish();
                    });
                }else{
                    Log.i("Call", "fuck");
                }
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        // 注册广播接收器
        IntentFilter filter = new IntentFilter(LinphoneManager.ACTION_CALL_STATE_CHANGED);
        LocalBroadcastManager.getInstance(this).registerReceiver(callStateReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 注销广播接收器
        LocalBroadcastManager.getInstance(this).unregisterReceiver(callStateReceiver);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        localVideoTexture = findViewById(R.id.local_video_texture);
        remoteVideoTexture = findViewById(R.id.remote_video_texture);
        chronometer=findViewById(R.id.chronometer);
        textViewInfo=findViewById(R.id.tv_info);

        Core core = LinphoneManager.getInstance(getApplicationContext()).getCore();

        intent=getIntent();
        boolean isVideoCall=intent.getBooleanExtra("isVideoCall",false);

        if(isVideoCall){
            core.setNativeVideoWindowId(remoteVideoTexture);
            core.setNativePreviewWindowId(localVideoTexture);
        }

        Button hangUpButton = findViewById(R.id.hang_up);
        hangUpButton.setOnClickListener(v -> {
            Call call = core.getCurrentCall();
            Log.i("Call",call.toString());
            Log.i("Call",call.getRemoteAddress().toString());
            Log.i("Call",call.getRemoteContact().toString());
            if (call != null) {
                try {
                    Log.i("Call","core.getCurrentCall()!=null");
                    call.terminate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        core.addListener(new CoreListenerStub() {
            @Override
            public void onCallStateChanged(Core core, Call call, Call.State state, String message) {
                if (state == Call.State.OutgoingInit) {
                    runOnUiThread(() -> {
                        textViewInfo.setText("正在拨号...");
                    });
                }
                if (state == Call.State.OutgoingProgress) {
                    runOnUiThread(() -> {
                        textViewInfo.setText("对方响铃中...");
                    });
                }
                if (state == Call.State.Connected) {
                    runOnUiThread(() -> {
                        textViewInfo.setText("对方已接听...");
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        chronometer.start();
                    });
                }
                if (state == Call.State.StreamsRunning) {
                    runOnUiThread(() -> {
                        textViewInfo.setText("通话中...");
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        chronometer.start();
                    });
                    Log.i("Call", "Media streams are running (audio + video)");
                    if (call.getCurrentParams().isVideoEnabled()) {
                        Log.i("Call", "Video is enabled in call!");
                        if (call.getRemoteParams() != null && call.getRemoteParams().isVideoEnabled()) {
                            Log.i("Call", "Remote video is enabled!");
                        } else {
                            Log.i("Call", "Remote video is NOT enabled!");
                        }
                    } else {
                        Log.i("Call", "Video is NOT enabled in call!");
                    }
                }
                if (state == Call.State.End || state == Call.State.Released) {
                    Log.i("Call", "end_onCallStateChanged");
                    runOnUiThread(() -> {
                        textViewInfo.setText("通话结束...");
                        chronometer.stop();
                        finish();
                    });
                }
            }

            @Override
            public void onCallStatsUpdated(@NonNull Core core, @NonNull Call call, @NonNull CallStats callStats) {
                super.onCallStatsUpdated(core, call, callStats);
                Log.i("STATS", "Video download bandwidth: " + callStats.getDownloadBandwidth());
            }
        });

        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }
}

package com.example.text.sip.test;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.TextureView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.text.R;
import com.example.text.sip.LinphoneManager;

import org.linphone.core.Call;
import org.linphone.core.CallStats;
import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;

public class CallActivity extends AppCompatActivity {
    private Intent intent;
    private TextureView localVideoTexture;
    private TextureView remoteVideoTexture;
    private Chronometer chronometer;
    private TextView textViewInfo;

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
            if (call != null) {
                try {
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
                    textViewInfo.setText("正在拨号...");
                }
                if (state == Call.State.OutgoingProgress) {
                    textViewInfo.setText("对方响铃中...");
                }
                if (state == Call.State.Connected) {
                    textViewInfo.setText("对方已接听...");
                    runOnUiThread(() -> {
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        chronometer.start();
                    });
                }
                if (state == Call.State.StreamsRunning) {
                    textViewInfo.setText("通话中...");
                    Log.d("Call", "Media streams are running (audio + video)");
                    if (call.getCurrentParams().isVideoEnabled()) {
                        Log.d("Call", "Video is enabled in call!");
                        if (call.getRemoteParams() != null && call.getRemoteParams().isVideoEnabled()) {
                            Log.d("Call", "Remote video is enabled!");
                        } else {
                            Log.w("Call", "Remote video is NOT enabled!");
                        }
                    } else {
                        Log.w("Call", "Video is NOT enabled in call!");
                    }
                }
                if (state == Call.State.End || state == Call.State.Released) {
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
                Log.d("STATS", "Video download bandwidth: " + callStats.getDownloadBandwidth());
            }
        });

        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }
}

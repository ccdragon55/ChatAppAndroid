package com.example.text.sip.test;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.text.R;
import com.example.text.sip.LinphoneManager;

import org.linphone.core.Call;
import org.linphone.core.CallStats;
import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;

public class CallActivity extends AppCompatActivity {
    private TextureView localVideoTexture;
    private TextureView remoteVideoTexture;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        localVideoTexture = findViewById(R.id.local_video_texture);
        remoteVideoTexture = findViewById(R.id.remote_video_texture);

        Core core = LinphoneManager.getInstance(getApplicationContext()).getCore();
        core.setNativeVideoWindowId(remoteVideoTexture);
        core.setNativePreviewWindowId(localVideoTexture);

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
            //finish();
        });

        core.addListener(new CoreListenerStub() {
            @Override
            public void onCallStateChanged(Core core, Call call, Call.State state, String message) {
                if (state == Call.State.StreamsRunning) {
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
                    runOnUiThread(() -> finish());
                }
            }

            @Override
            public void onCallStatsUpdated(@NonNull Core core, @NonNull Call call, @NonNull CallStats callStats) {
                super.onCallStatsUpdated(core, call, callStats);
                Log.d("STATS", "Video download bandwidth: " + callStats.getDownloadBandwidth());
            }
        });

    }
}

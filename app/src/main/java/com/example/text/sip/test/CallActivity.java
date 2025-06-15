package com.example.text.sip.test;

import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.text.R;
import com.example.text.sip.LinphoneManager;

import org.linphone.core.Call;
import org.linphone.core.Core;

public class CallActivity extends AppCompatActivity {
    private SurfaceView localVideoSurface;
    private SurfaceView remoteVideoSurface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        localVideoSurface = findViewById(R.id.local_video_surface);
        remoteVideoSurface = findViewById(R.id.remote_video_surface);

        Core core = LinphoneManager.getInstance(this).getCore();
        core.setNativeVideoWindowId(remoteVideoSurface);
        core.setNativePreviewWindowId(localVideoSurface);

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
            finish();
        });
    }
}
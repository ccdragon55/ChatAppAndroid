package com.example.text.sip.test;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.text.R;
import com.example.text.sip.LinphoneManager;

import org.linphone.core.Call;

public class IncomingCallActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_call);

        // 显示来电者信息
        TextView callerInfo = findViewById(R.id.caller_info);
        String remoteAddress = getIntent().getStringExtra("remoteAddress");
        callerInfo.setText("Incoming call from: " + remoteAddress);

        // 接受和拒绝按钮
        Button acceptButton = findViewById(R.id.accept_call);
        Button declineButton = findViewById(R.id.decline_call);

        acceptButton.setOnClickListener(v -> {
            Call call = LinphoneManager.getInstance(getApplicationContext()).getCore().getCurrentCall();
            if (call != null) {
                try {
                    call.setCameraEnabled(true);
                    call.accept();
                    startActivity(new Intent(this, CallActivity.class));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        declineButton.setOnClickListener(v -> {
            Call call = LinphoneManager.getInstance(getApplicationContext()).getCore().getCurrentCall();
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

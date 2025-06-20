package com.example.text.sip.test;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.text.R;
import com.example.text.activities.loginActivities.RegisterActivity;
import com.example.text.apis.ApiService;
import com.example.text.retrofits.RetrofitClient;
import com.example.text.sip.LinphoneManager;

import org.linphone.core.TransportType;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SipMainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 100;

    EditText etUsername,etPassword;
    Button btnRegister;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sip_main);

        // 请求权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA
            }, PERMISSION_REQUEST_CODE);
        }// else {
//            initializeLinphone();
//        }

        // 拨号按钮
        EditText sipAddressInput = findViewById(R.id.sip_address_input);
        Button audioCallButton = findViewById(R.id.audio_call_button);
        Button videoCallButton = findViewById(R.id.video_call_button);

        etUsername = findViewById(R.id.sip_username);
        etPassword = findViewById(R.id.sip_password);
        btnRegister = findViewById(R.id.register_button);

        audioCallButton.setOnClickListener(v -> {
            String destination = sipAddressInput.getText().toString();
            if (!destination.isEmpty()) {
                LinphoneManager.getInstance(getApplicationContext()).makeCall(destination, false);
            }
        });

        videoCallButton.setOnClickListener(v -> {
            String destination = sipAddressInput.getText().toString();
            if (!destination.isEmpty()) {
                LinphoneManager.getInstance(getApplicationContext()).makeCall(destination, true);
            }
        });

        btnRegister.setOnClickListener(v->{
            initializeLinphone();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initializeLinphone();
        }
    }

    private void initializeLinphone() {
        // 初始化 Linphone 并注册
        LinphoneManager manager = LinphoneManager.getInstance(getApplicationContext());
        //manager.createProxyConfig("your_username", "your_password", "10.129.156.163:5060", TransportType.Udp);
        manager.createProxyConfig(etUsername.getText().toString(), etPassword.getText().toString(), "10.129.156.163", TransportType.Udp);
    }
}
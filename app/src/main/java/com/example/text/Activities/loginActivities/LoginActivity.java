package com.example.text.Activities.loginActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.text.Activities.MenuActivity;
import com.example.text.R;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnGoRegister = findViewById(R.id.btnGoRegister);


        findViewById(R.id.btnLogin).setOnClickListener(v -> handleLogin());
        findViewById(R.id.tvForgotPassword).setOnClickListener(v -> startActivity(new Intent(this, ForgotPasswordActivity.class)));
        findViewById(R.id.tvRegister).setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));

        btnLogin.setOnClickListener(v -> attemptLogin());
        btnGoRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void attemptLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
            return;
        }

        String savedPassword = sharedPreferences.getString(username, "");
        if (savedPassword.equals(password)) {
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
            // 跳转到主界面
        } else {
            Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
        }

//        OkHttpClient client = new OkHttpClient();
//        RequestBody requestBody = new FormBody.Builder()
//                .add("param1", "value1")
//                .add("param2", "value2")
//                .build();
//        Request request = new Request.Builder()
//                .url("http://10.29.61.159")
//                .post(requestBody) // 使用上面创建的formBody或jsonBody
//                .build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                // 请求失败处理
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    // 请求成功处理
//                    final String responseData = response.body().string();
//                    // 在主线程中更新UI
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            // 更新UI的操作
//                        }
//                    });
//                }
//            }
//        });




        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
//        intent.putExtra("name", clickedName);
//        intent.putExtra("lastChat", clickedLastChat);
        startActivity(intent);
    }
}
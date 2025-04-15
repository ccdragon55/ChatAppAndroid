package com.example.text.Activities.loginActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.text.R;
import com.example.text.utils.Constants;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {
    private EditText etRegUsername, etRegPassword, etConfirmPassword, etCheckCodeInput;
    private ImageView ivCheckCode;
    private SharedPreferences sharedPreferences;
    private String checkCodeKey = ""; // 用于存储验证码key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);

        etRegUsername = findViewById(R.id.etRegUsername);
        etRegPassword = findViewById(R.id.etRegPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etCheckCodeInput = findViewById(R.id.etCheckCodeInput);
        ivCheckCode = findViewById(R.id.ivCheckCode);
        Button btnRegister = findViewById(R.id.btnRegister);
        Button btnGoLogin = findViewById(R.id.btnGoLogin);

        btnRegister.setOnClickListener(v -> attemptRegister());
        btnGoLogin.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));

        ivCheckCode.setOnClickListener(v -> loadCaptchaImage()); // 点击验证码刷新
        loadCaptchaImage(); // 初始加载
    }

    private void attemptRegister() {
        String username = etRegUsername.getText().toString().trim();
        String password = etRegPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String checkCode = etCheckCodeInput.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty() || checkCode.isEmpty()) {
            Toast.makeText(this, "请填写完整信息", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }

        if (sharedPreferences.contains(username)) {
            Toast.makeText(this, "用户名已存在", Toast.LENGTH_SHORT).show();
            return;
        }

        // 验证验证码
        new Thread(() -> {
            try {
                String verifyUrl = Constants.BASE_URL + "/checkcode/verify?key=" + checkCodeKey + "&value=" + checkCode;
                HttpURLConnection conn = (HttpURLConnection) new URL(verifyUrl).openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);

                InputStream in = conn.getInputStream();
                StringBuilder response = new StringBuilder();
                int ch;
                while ((ch = in.read()) != -1) response.append((char) ch);

                JSONObject json = new JSONObject(response.toString());
                boolean success = json.getBoolean("success");

                runOnUiThread(() -> {
                    if (success) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(username, password);
                        editor.apply();

                        Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                        finish(); // 返回登录页面
                    } else {
                        Toast.makeText(this, "验证码错误", Toast.LENGTH_SHORT).show();
                        loadCaptchaImage(); // 刷新验证码
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "验证码验证失败", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void loadCaptchaImage() {
        new Thread(() -> {
            try {
                URL url = new URL(Constants.BASE_URL + "/account/checkcode");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                InputStream input = connection.getInputStream();
                StringBuilder response = new StringBuilder();
                int c;
                while ((c = input.read()) != -1) {
                    response.append((char) c);
                }

                JSONObject json = new JSONObject(response.toString());
                JSONObject data = json.getJSONObject("data");
                String base64Str = data.getString("checkCode").split(",")[1];
                checkCodeKey = data.getString("checkCodeKey");

                byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decodedBytes);
                Bitmap bitmap = BitmapFactory.decodeStream(byteArrayInputStream);

                runOnUiThread(() -> ivCheckCode.setImageBitmap(bitmap));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}

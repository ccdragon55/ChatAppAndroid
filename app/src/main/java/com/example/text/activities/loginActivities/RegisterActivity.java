package com.example.text.activities.loginActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Base64;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.text.R;
import com.example.text.apis.ApiService;
import com.example.text.dataModel.CheckCode;
import com.example.text.dataModel.request.RegisterRequest;
import com.example.text.dataModel.response.RegisterResponse;
import com.example.text.dataModel.response.SendCheckCodeResponse;
import com.example.text.database.AvatarModel;
import com.example.text.retrofits.RetrofitClient;

import java.io.ByteArrayInputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText etUsername, etEmail, etPassword, etVerificationCode;
    private ImageView ivCheckCode;
    private Button btnRegister;
    private CountDownTimer countDownTimer;
    private SharedPreferences sharedPreferences;
    private String checkCodeKey = ""; // 用于存储验证码key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        setupListeners();
        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
    }

    private void initViews() {
        etUsername = findViewById(R.id.etRegUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etVerificationCode = findViewById(R.id.etConfirmPassword);
        ivCheckCode = findViewById(R.id.ivCheckCode);
        btnRegister = findViewById(R.id.btnRegister);
    }

    private void setupListeners() {
        findViewById(R.id.btnGoLogin).setOnClickListener(v -> backToLogin());
        ivCheckCode.setOnClickListener(v -> sendVerificationCode());
        btnRegister.setOnClickListener(v -> handleRegistration());
    }

    private void sendVerificationCode() {
        // 调用发送验证码API
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<SendCheckCodeResponse> call = apiService.sendVerificationCode();
        call.enqueue(new Callback<SendCheckCodeResponse>() {
            @Override
            public void onResponse(@NonNull Call<SendCheckCodeResponse> call, @NonNull Response<SendCheckCodeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CheckCode checkCode=response.body().getData();
                    String base64Str=checkCode.getCheckCode().split(",")[1];
                    checkCodeKey=checkCode.getCheckCodeKey();

                    sharedPreferences.edit().putString("checkCodeKey", checkCodeKey).apply();
//
                    byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decodedBytes);
                    Bitmap bitmap = BitmapFactory.decodeStream(byteArrayInputStream);

                    startCountdown();

                    runOnUiThread(() -> ivCheckCode.setImageBitmap(bitmap));

//                    if (response.body().getCode() == 0) {
//                        startCountdown();
//                        Toast.makeText(RegisterActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SendCheckCodeResponse> call, @NonNull Throwable t) {
                Toast.makeText(RegisterActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startCountdown() {
        ivCheckCode.setEnabled(false);
        countDownTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                String message=millisUntilFinished/1000 + "秒后重试";
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }

            public void onFinish() {
                ivCheckCode.setEnabled(true);
            }
        }.start();
    }

    private void handleRegistration() {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String verificationCode = etVerificationCode.getText().toString().trim();

        if (!validateInput(username, email, password, verificationCode)) return;

        // 调用注册API
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<RegisterResponse> call = apiService.register(new RegisterRequest(
                sharedPreferences.getString("checkCodeKey", ""),
                email,
                password,
                username,
                verificationCode
        ));

        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(@NonNull Call<RegisterResponse> call, @NonNull Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    handleRegistrationSuccess(response.body());
                } else {
                    Toast.makeText(RegisterActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    etVerificationCode.setText("");
                }
            }

            @Override
            public void onFailure(@NonNull Call<RegisterResponse> call, @NonNull Throwable t) {
                Toast.makeText(RegisterActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleRegistrationSuccess(RegisterResponse response) {
        if (response.getData() == null) {
            // 保存用户数据
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("userId", response.getData().getUserId());
            editor.putString("avatarUrl", response.getData().getAvatarUrl());
            editor.apply();

            AvatarModel avatarModel=new AvatarModel(getApplicationContext());
            avatarModel.saveAvatar(response.getData().getUserId(),response.getData().getAvatarUrl());

            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            Toast.makeText(this, "网络错误", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInput(String username, String email, String password, String verificationCode) {
        if (username.equals("")) {
            Toast.makeText(RegisterActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (username.length() < 2) {
            Toast.makeText(RegisterActivity.this, "用户名不得小于2位！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (verificationCode.equals("")) {
            Toast.makeText(RegisterActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isValidPhone(String phone) {
        return Patterns.PHONE.matcher(phone).matches() && phone.length() == 11;
    }

    private void backToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        super.onDestroy();
    }
}

//public class RegisterActivity extends AppCompatActivity {
//    private EditText etRegUsername, etRegPassword, etConfirmPassword, etCheckCodeInput;
//    private ImageView ivCheckCode;
//    private SharedPreferences sharedPreferences;
//    private String checkCodeKey = ""; // 用于存储验证码key
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);
//
//        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
//
//        etRegUsername = findViewById(R.id.etRegUsername);
//        etRegPassword = findViewById(R.id.etRegPassword);
//        etConfirmPassword = findViewById(R.id.etConfirmPassword);
//        etCheckCodeInput = findViewById(R.id.etCheckCodeInput);
//        ivCheckCode = findViewById(R.id.ivCheckCode);
//        Button btnRegister = findViewById(R.id.btnRegister);
//        Button btnGoLogin = findViewById(R.id.btnGoLogin);
//
//        btnRegister.setOnClickListener(v -> attemptRegister());
//        btnGoLogin.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));
//
//        ivCheckCode.setOnClickListener(v -> loadCaptchaImage()); // 点击验证码刷新
//        loadCaptchaImage(); // 初始加载
//    }
//
//    private void attemptRegister() {
//        String username = etRegUsername.getText().toString().trim();
//        String password = etRegPassword.getText().toString().trim();
//        String confirmPassword = etConfirmPassword.getText().toString().trim();
//        String checkCode = etCheckCodeInput.getText().toString().trim();
//
//        if (username.isEmpty() || password.isEmpty() || checkCode.isEmpty()) {
//            Toast.makeText(this, "请填写完整信息", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if (!password.equals(confirmPassword)) {
//            Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if (sharedPreferences.contains(username)) {
//            Toast.makeText(this, "用户名已存在", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // 验证验证码
//        new Thread(() -> {
//            try {
//                String verifyUrl = Constants.BASE_URL + "/checkcode/verify?key=" + checkCodeKey + "&value=" + checkCode;
//                HttpURLConnection conn = (HttpURLConnection) new URL(verifyUrl).openConnection();
//                conn.setRequestMethod("GET");
//                conn.setConnectTimeout(5000);
//                conn.setReadTimeout(5000);
//
//                InputStream in = conn.getInputStream();
//                StringBuilder response = new StringBuilder();
//                int ch;
//                while ((ch = in.read()) != -1) response.append((char) ch);
//
//                JSONObject json = new JSONObject(response.toString());
//                boolean success = json.getBoolean("success");
//
//                runOnUiThread(() -> {
//                    if (success) {
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putString(username, password);
//                        editor.apply();
//
//                        Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
//                        finish(); // 返回登录页面
//                    } else {
//                        Toast.makeText(this, "验证码错误", Toast.LENGTH_SHORT).show();
//                        loadCaptchaImage(); // 刷新验证码
//                    }
//                });
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                runOnUiThread(() -> Toast.makeText(this, "验证码验证失败", Toast.LENGTH_SHORT).show());
//            }
//        }).start();
//    }
//
//    private void loadCaptchaImage() {
//        new Thread(() -> {
//            try {
//                URL url = new URL(Constants.BASE_URL + "/account/checkcode");
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("GET");
//                connection.setConnectTimeout(5000);
//                connection.setReadTimeout(5000);
//
//                InputStream input = connection.getInputStream();
//                StringBuilder response = new StringBuilder();
//                int c;
//                while ((c = input.read()) != -1) {
//                    response.append((char) c);
//                }
//
//                JSONObject json = new JSONObject(response.toString());
//                JSONObject data = json.getJSONObject("data");
//                String base64Str = data.getString("checkCode").split(",")[1];
//                checkCodeKey = data.getString("checkCodeKey");
//
//                byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);
//                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decodedBytes);
//                Bitmap bitmap = BitmapFactory.decodeStream(byteArrayInputStream);
//
//                runOnUiThread(() -> ivCheckCode.setImageBitmap(bitmap));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//    }
//}

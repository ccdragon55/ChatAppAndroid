package com.example.text.activities.loginActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.text.activities.MenuActivity;
import com.example.text.apis.ApiService;
import com.example.text.R;
import com.example.text.database.AvatarModel;
import com.example.text.retrofits.RetrofitClient;
import com.example.text.dataModel.request.UserIdRequest;
import com.example.text.dataModel.response.AvatarResponse;
import com.example.text.dataModel.request.LoginRequest;
import com.example.text.dataModel.response.LoginResponse;
import com.example.text.dataModel.UserInfo;
import com.example.text.services.WebSocketService;
import com.example.text.utils.Store;

import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnGoRegister = findViewById(R.id.btnGoRegister);

        btnLogin.setOnClickListener(v -> handleLogin());
        btnGoRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void handleLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "请输入邮箱和密码", Toast.LENGTH_SHORT).show();
            return;
        }

        String encryptedPassword = md5(password);

        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<LoginResponse> call = apiService.login(new LoginRequest(email, encryptedPassword));
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    handleLoginSuccess(response.body());
                } else {
                    handleError(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                Toast.makeText(LoginActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleLoginSuccess(LoginResponse response) {
        UserInfo userInfo = response.getData();
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Log.e("fk", userInfo.toString() );

        editor.putBoolean("admin", userInfo.getAdmin());
        editor.putInt("contactStatus", userInfo.getContactStatus()!=null?userInfo.getContactStatus():0);
        editor.putInt("joinType", userInfo.getJoinType()!=null?userInfo.getJoinType():0);
        editor.putString("nickName", userInfo.getNickName());
        editor.putString("personalSignature", userInfo.getPersonalSignature()!=null?userInfo.getPersonalSignature():"");
        editor.putInt("sex", userInfo.getSex()!=null?userInfo.getSex():0);
        editor.putString("token", userInfo.getToken());
        editor.putString("userId", userInfo.getUserId());
        editor.apply();

        Store.getInstance(getApplicationContext()).initUserId(userInfo.getUserId());
        Store.getInstance(getApplicationContext()).setData("token",userInfo.getToken());

        fetchAvatar(userInfo.getUserId());

        startWebSocketService();

        startActivity(new Intent(this, MenuActivity.class));
    }

    private void fetchAvatar(String userId) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<AvatarResponse> call = apiService.getAvatar(new UserIdRequest(userId));

//        Call<String> call = apiService.getAvatar(new AvatarRequest(userId));
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    String avatarUrl = response.body();
//                    Log.e("fk", response.body() );
////                    Log.e("fk", avatarUrl!=null?avatarUrl:"avatarNull" );
//                    sharedPreferences.edit().putString("selfAvatarUrl", avatarUrl).apply();
//                    saveAvatarLocally(userId, avatarUrl);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                // 处理错误
//            }
//        });

        call.enqueue(new Callback<AvatarResponse>() {
            @Override
            public void onResponse(@NonNull Call<AvatarResponse> call, @NonNull Response<AvatarResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String avatarUrl = response.body().getData();
                    Log.e("fk", response.body().toString() );
//                    Log.e("fk", avatarUrl!=null?avatarUrl:"avatarNull" );
                    sharedPreferences.edit().putString("selfAvatarUrl", avatarUrl).apply();
                    saveAvatarLocally(userId, avatarUrl);

                    AvatarModel avatarModel=new AvatarModel(getApplicationContext());
                    avatarModel.saveAvatar(Store.getInstance(getApplicationContext()).getUserId(),avatarUrl);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AvatarResponse> call, @NonNull Throwable t) {
                // 处理错误
                Toast.makeText(LoginActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveAvatarLocally(String userId, String url) {
        //TODO
        // 实现头像下载和本地存储逻辑
        // 可以使用Glide下载并缓存图片
    }

    private void handleError(Response<?> response) {
        try {
            if (response.errorBody() != null) {
                String errorBody = response.errorBody().string();
                JSONObject jsonObject = new JSONObject(errorBody);
                String errorMessage = jsonObject.getString("info");
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private void startWebSocketService() {
        Intent serviceIntent = new Intent(this, WebSocketService.class);
//        serviceIntent.putExtra("token", getAuthToken());

        // Android 8.0+ 必须使用 startForegroundService
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }

//    private void handleLogin() {
//        String username = etUsername.getText().toString().trim();
//        String password = etPassword.getText().toString().trim();
//
//        if (username.isEmpty() || password.isEmpty()) {
//            Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String savedPassword = sharedPreferences.getString(username, "");
//        if (savedPassword.equals(password)) {
//            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
//            // 跳转到主界面
//        } else {
//            Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
//        }
//
////        OkHttpClient client = new OkHttpClient();
////        RequestBody requestBody = new FormBody.Builder()
////                .add("param1", "value1")
////                .add("param2", "value2")
////                .build();
////        Request request = new Request.Builder()
////                .url("http://10.29.61.159")
////                .post(requestBody) // 使用上面创建的formBody或jsonBody
////                .build();
////        client.newCall(request).enqueue(new Callback() {
////            @Override
////            public void onFailure(Call call, IOException e) {
////                // 请求失败处理
////                e.printStackTrace();
////            }
////
////            @Override
////            public void onResponse(Call call, Response response) throws IOException {
////                if (response.isSuccessful()) {
////                    // 请求成功处理
////                    final String responseData = response.body().string();
////                    // 在主线程中更新UI
////                    runOnUiThread(new Runnable() {
////                        @Override
////                        public void run() {
////                            // 更新UI的操作
////                        }
////                    });
////                }
////            }
////        });
//
//
//
//
//        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
////        intent.putExtra("name", clickedName);
////        intent.putExtra("lastChat", clickedLastChat);
//        startActivity(intent);
//    }
}
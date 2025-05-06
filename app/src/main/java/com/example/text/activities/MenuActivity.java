package com.example.text.activities;

// MainActivity.java

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.text.activities.friendActivities.FriendsFragment;
import com.example.text.activities.notificationsActivity.NotificationsFragment;
import com.example.text.activities.profileActivities.ProfileActivity;
import com.example.text.R;
import com.example.text.activities.chatActivities.ChatListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {

//    int avater;
    ImageView avatar;
    TextView tvUserName;
    String avatarUrl;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        // 默认加载首页
        loadFragment(new ChatListFragment());

//        avater=R.mipmap.ic_launcher;
//        ImageView avaterImageView=findViewById(R.id.user_avatar);
//        avaterImageView.setImageResource(avater);

        avatar=findViewById(R.id.iv_avatar);
        tvUserName=findViewById(R.id.tv_username);
        SharedPreferences sharedPreferences=getSharedPreferences("userPrefs", MODE_PRIVATE);
        tvUserName.setText(sharedPreferences.getString("nickName",""));

        loadUserAvatar();
//        Log.e("abc", "onCreate: "+ avatar);
//        ImageView avaterImageView=findViewById(R.id.user_avatar);
//        Glide.with(this)
//                .load(avatar)
////                .placeholder(R.drawable.placeholder) // 加载中的占位图
////                .error(R.drawable.error)             // 加载失败的占位图
//                .into(avaterImageView);

        avatar.setOnClickListener((view) -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        });

        bottomNavigation.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    fragment = new ChatListFragment();
                } else if (itemId == R.id.nav_dashboard) {
                    fragment = new FriendsFragment();
                } else if (itemId == R.id.nav_notifications) {
                    fragment = new NotificationsFragment();
                }
                return loadFragment(fragment);
            }
        });
    }

    // 加载 Fragment 的通用方法
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;

//        // 在 loadFragment 方法中优化
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        if (fragment.isAdded()) {
//            transaction.show(fragment);
//        } else {
//            transaction.add(R.id.fragment_container, fragment);
//        }
//// 隐藏其他 Fragment
//        for (Fragment f : getSupportFragmentManager().getFragments()) {
//            if (f != fragment) {
//                transaction.hide(f);
//            }
//        }
//        transaction.commit();
    }

    private void loadUserAvatar(){
        Log.e("fk","loadUserAvatar");
        // 1. 获取 SharedPreferences（同存入时用的 name）
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("userPrefs", Context.MODE_PRIVATE);

        // 2. 取出字符串，第二个参数是默认值
        String avatarUrl = prefs.getString("selfAvatarUrl", null);

        Log.e("fk",avatarUrl!=null?avatarUrl:"avatar null");

        if (avatarUrl == null) {
            Log.d("Avatar", "No avatar URL found");
        } else {
            // 如果要更新 UI，要切到主线程
            runOnUiThread(() -> {
                Glide.with(MenuActivity.this)
                        .load(avatarUrl)
                        .apply(RequestOptions.circleCropTransform())
                        .into(avatar);
            });
        }

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                OkHttpClient client = new OkHttpClient();
//                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "{\"userId\":\"U26271656577\"}");
//
//                Request request = new Request.Builder()
//                        .url(Constants.BASE_URL + "/file/getAvatar")
//                        .post(requestBody)
//                        .build();
//
//                try {
//                    Response response = client.newCall(request).execute(); // 同步方法
//                    if (response != null && response.isSuccessful()) {
//                        try (ResponseBody body = response.body()) {
//                            if (body != null) {
//                                String responseBody = body.string();
//                                Log.e("123", responseBody);
//
//                                JSONObject json = new JSONObject(responseBody);
//                                avatarUrl = json.getString("data");
//
//                                Log.e("123", "avatar: " + avatar);
//
//                                // 如果要更新 UI，要切到主线程
//                                runOnUiThread(() -> {
//                                    Glide.with(MenuActivity.this)
//                                            .load(avatarUrl)
//                                            .apply(RequestOptions.circleCropTransform())
//                                            .into(avatar);
//                                });
//                            }
//                        }
//                    }
//                } catch (IOException | JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();

//        OkHttpClient client = new OkHttpClient();
//        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "{\"userId\":\"U26271656577\"}");
//
//        Request request = new Request.Builder()
//                .url(Constants.BASE_URL + "/file/getAvatar")
//                .post(requestBody)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace(); // 处理错误
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response != null && response.isSuccessful()) {
//                    try (ResponseBody body = response.body()) {
//                        if (body != null) {
//                            String responseBody = body.string();
//                            Log.e("123",responseBody);
//                            JSONObject json = new JSONObject(responseBody);
////                            JSONObject data = json.getJSONObject("data");
////                            avatar = data.getString("filePath");
//                            avatar=json.getString("data");
//
//                            Log.e("123","avatar:"+avatar);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });


//        new Thread(() -> {
//            HttpURLConnection connection = null;
//            try {
//                // 1. 准备参数
//                Map<String, String> m = new HashMap<>();
//                m.put("userId", "U26271656577");
//
//                // 2. 构造 URL
//                URL url = new URL(Constants.BASE_URL + "/file/getAvatar");
//                connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("POST");
//                connection.setConnectTimeout(5000);
//                connection.setReadTimeout(5000);
//
//                // 3. 将 Map 转换为 POST 请求体（格式：key1=value1&key2=value2）
//                StringBuilder postData = new StringBuilder();
//                for (Map.Entry<String, String> entry : m.entrySet()) {
//                    if (postData.length() != 0) postData.append('&');
//                    postData.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
//                    postData.append('=');
//                    postData.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
//                }
//
//                // 4. 设置请求头并写入请求体
//                connection.setDoOutput(true); // 允许写入输出流
//                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//                try (OutputStream output = connection.getOutputStream()) {
//                    output.write(postData.toString().getBytes("UTF-8"));
//                    output.flush();
//                }
//
//                // 5. 处理响应
//                int responseCode = connection.getResponseCode();
//                if (responseCode == HttpURLConnection.HTTP_OK) {
//                    InputStream input = connection.getInputStream();
//                    StringBuilder response = new StringBuilder();
//                    int c;
//                    while ((c = input.read()) != -1) {
//                        response.append((char) c);
//                    }
//
//                    // 6. 解析 JSON
//                    JSONObject json = new JSONObject(response.toString());
//                    JSONObject data = json.getJSONObject("data");
//                    avatar = data.getString("filePath");
//                    // TODO: 更新 UI（需要在主线程执行）
//                } else {
//                    Log.e("HTTP_ERROR", "响应码: " + responseCode);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                if (connection != null) {
//                    connection.disconnect(); // 断开连接
//                }
//            }
//        }).start();


//        new Thread(() -> {
//            try {
//                Map<String,String> m=new HashMap<>();
//                m.put("userId","U26271656577");
//
//                URL url = new URL(Constants.BASE_URL + "/file/getAvatar");
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
////                connection.setRequestMethod("GET");
//                connection.setRequestMethod("POST");
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
//                avatar = data.getString("filePath");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
    }
}

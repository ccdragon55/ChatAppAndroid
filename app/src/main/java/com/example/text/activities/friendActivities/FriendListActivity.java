package com.example.text.activities.friendActivities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.text.activities.loginActivities.LoginActivity;
import com.example.text.adapters.FriendListAdapter;
import com.example.text.apis.ApiService;
import com.example.text.R;
import com.example.text.retrofits.RetrofitClient;
import com.example.text.dataModel.FriendListItem;
import com.example.text.dataModel.request.LoginRequest;
import com.example.text.dataModel.response.LoginResponse;
import com.example.text.views.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendListActivity extends AppCompatActivity {

    private FriendListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private Map<String, Integer> mLetterPositions = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        // 初始化RecyclerView
        RecyclerView rvFriends = findViewById(R.id.rv_friends);
        mLayoutManager = new LinearLayoutManager(this);
        rvFriends.setLayoutManager(mLayoutManager);

        mAdapter = new FriendListAdapter(generateData());
        rvFriends.setAdapter(mAdapter);

        // 侧边栏事件监听
        @SuppressLint("WrongViewCast") SideBar sideBar = findViewById(R.id.side_bar);
        sideBar.setOnLetterChangedListener(new SideBar.OnLetterChangedListener() {
            @Override
            public void onLetterChanged(String letter) {
                // 显示中间提示字母
//                tvLetterHint.setVisibility(View.VISIBLE);
//                tvLetterHint.setText(letter);

                // 滚动到对应位置
                Integer position = mAdapter.getLetterPosition(letter);
                if (position != null) {
                    mLayoutManager.scrollToPositionWithOffset(position, 0);
                }
            }

            @Override
            public void onLetterReleased() {
                // 隐藏提示字母
//                tvLetterHint.setVisibility(View.GONE);
            }
        });
    }

//    private List<FriendListItem> generate() {
//        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
//        Call<LoginResponse> call = apiService.login(new LoginRequest(email, encryptedPassword));
//        call.enqueue(new Callback<LoginResponse>() {
//            @Override
//            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    handleLoginSuccess(response.body());
//                } else {
//                    handleError(response);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<LoginResponse> call, Throwable t) {
//                Toast.makeText(LoginActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
    private List<FriendListItem> generateData() {

        List<FriendListItem> list = new ArrayList<>();
        list.add(new FriendListItem("Alice"));
        list.add(new FriendListItem("Bob"));
        list.add(new FriendListItem("Charlie"));
        // 更多数据...

        // 按首字母排序
        Collections.sort(list, (f1, f2) ->
                f1.getFirstLetter().compareTo(f2.getFirstLetter()));
        return list;
    }
}

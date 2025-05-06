package com.example.text.activities.friendActivities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.text.activities.profileActivities.FriendProfileActivity;
import com.example.text.adapters.FriendListAdapter;
import com.example.text.R;
import com.example.text.apis.ApiService;
import com.example.text.dataModel.FriendListItem;
import com.example.text.dataModel.request.UserIdRequest;
import com.example.text.dataModel.response.FetchApplyInfoResponse;
import com.example.text.dataModel.response.FetchContactsResponse;
import com.example.text.retrofits.RetrofitClient;
import com.example.text.utils.Store;
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
    private List<FriendListItem> friendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        // 初始化RecyclerView
        RecyclerView rvFriends = findViewById(R.id.rv_friends);
        mLayoutManager = new LinearLayoutManager(this);
        rvFriends.setLayoutManager(mLayoutManager);

        friendList=new ArrayList<>();
        fetchContacts();

//        mAdapter = new FriendListAdapter(generateData());
        mAdapter = new FriendListAdapter(friendList);
        mAdapter.setOnItemClickListener((view, position) -> {
            // position 是被点击项的位置（从 0 开始）
            // 根据 position 获取数据
            FriendListItem friendListItem= friendList.get(position);
            Intent intent = new Intent(this, FriendProfileActivity.class);
            intent.putExtra("friendId", friendListItem.getContactId());
            startActivity(intent);
        });
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

    private void fetchContacts(){
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<FetchContactsResponse> call = apiService.getUserContact(Store.getInstance(getApplicationContext()).getData("token"));

        call.enqueue(new Callback<FetchContactsResponse>() {
            @Override
            public void onResponse(@NonNull Call<FetchContactsResponse> call, @NonNull Response<FetchContactsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    friendList = response.body().getData();
                    FriendListItem.sortByFiestLetter(friendList);
                } else {
                    Toast.makeText(FriendListActivity.this, "网络错误: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<FetchContactsResponse> call, @NonNull Throwable t) {
                Toast.makeText(FriendListActivity.this, "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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

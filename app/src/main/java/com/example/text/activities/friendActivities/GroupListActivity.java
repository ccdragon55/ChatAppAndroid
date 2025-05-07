package com.example.text.activities.friendActivities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.text.activities.profileActivities.FriendProfileActivity;
import com.example.text.adapters.FriendListAdapter;
import com.example.text.R;
import com.example.text.apis.ApiService;
import com.example.text.dataModel.FriendListItem;
import com.example.text.dataModel.response.FetchContactsResponse;
import com.example.text.retrofits.RetrofitClient;
import com.example.text.utils.JsonUtils;
import com.example.text.utils.Store;
import com.example.text.views.SideBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupListActivity extends AppCompatActivity {
    private ImageButton btnBack;
    private FriendListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private Map<String, Integer> mLetterPositions = new HashMap<>();
    private List<FriendListItem> friendList;

    private BroadcastReceiver wsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String json = intent.getStringExtra("dataJson"); // 公共字段

            try {
                // 根据 Action 分发不同逻辑
                if (action == null) return;

                switch (action) {
                    case "ACTION_WS_addNewGroup":
                        handleAddNewGroup(json);
                        break;
                    case "ACTION_WS_receiveChangeGroupNameMessage":
                        handleReceiveChangeGroupNameMessage(json);
                        break;
                    default:
                        break;
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        @SuppressLint("NotifyDataSetChanged")
        private void handleAddNewGroup(String json) throws JSONException {
            JSONObject jsonObject=new JSONObject(json);
            Map<String,Object> message= JsonUtils.jsonToStrObjMap(jsonObject);
            FriendListItem friendListItem=new FriendListItem(message);
            friendList.add(friendListItem);
            FriendListItem.sortByFirstLetter(friendList);
            mAdapter.processData(friendList);
            mAdapter.notifyDataSetChanged();
        }

        @SuppressLint("NotifyDataSetChanged")
        private void handleReceiveChangeGroupNameMessage(String json) throws JSONException {
            JSONObject jsonObject=new JSONObject(json);
            Map<String,Object> message= JsonUtils.jsonToStrObjMap(jsonObject);
            String contactId=(String)message.get("contactId");

            for (int i = 0; i < friendList.size(); ++i) {
                if (Objects.equals(friendList.get(i).getContactId(), contactId)) {
                    friendList.get(i).setContactName((String)message.get("contactName"));
                    friendList.get(i).generateFirstLetter();
                }
            }
            FriendListItem.sortByFirstLetter(friendList);
            mAdapter.processData(friendList);
            mAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("ACTION_WS_addNewGroup");
        filter.addAction("ACTION_WS_receiveChangeGroupNameMessage");
        LocalBroadcastManager.getInstance(this).registerReceiver(wsReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(wsReceiver);
    }

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        btnBack=findViewById(R.id.btn_back);

        // 初始化RecyclerView
        RecyclerView rvFriends = findViewById(R.id.rv_groupList);
        mLayoutManager = new LinearLayoutManager(this);
        rvFriends.setLayoutManager(mLayoutManager);

        friendList=new ArrayList<>();

//        mAdapter = new FriendListAdapter(generateData());
        mAdapter = new FriendListAdapter(friendList);
        mAdapter.setOnItemClickListener((view, position) -> {
            // position 是被点击项的位置（从 0 开始）
            // 根据 position 获取数据
            FriendListItem friendListItem= mAdapter.getFriendListItem(position);
            //TODO
            Intent intent = new Intent(this, FriendProfileActivity.class);
            intent.putExtra("friendName", friendListItem.getContactName());
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

        fetchContacts();

        btnBack.setOnClickListener(v->finish());
    }

    private void fetchContacts(){
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<FetchContactsResponse> call = apiService.getGroupContact(Store.getInstance(getApplicationContext()).getData("token"));

        call.enqueue(new Callback<FetchContactsResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<FetchContactsResponse> call, @NonNull Response<FetchContactsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    friendList.clear();
                    friendList.addAll(response.body().getData());
                    for(FriendListItem item:friendList){
                        item.generateFirstLetter();
                    }
                    FriendListItem.sortByFirstLetter(friendList);
                    mAdapter.processData(friendList);
                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(GroupListActivity.this, "网络错误: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<FetchContactsResponse> call, @NonNull Throwable t) {
                Toast.makeText(GroupListActivity.this, "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

package com.example.text.activities.friendActivities;

// HomeFragment.java

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.text.activities.profileActivities.FriendProfileActivity;
import com.example.text.adapters.FriendListAdapter;
import com.example.text.R;
import com.example.text.apis.ApiService;
import com.example.text.dataModel.CreateGroupResponseInfo;
import com.example.text.dataModel.FriendListItem;
import com.example.text.dataModel.request.AddFriendRequest;
import com.example.text.dataModel.request.CreateGroupRequest;
import com.example.text.dataModel.response.CreateGroupResponse;
import com.example.text.dataModel.response.FetchContactsResponse;
import com.example.text.database.AvatarModel;
import com.example.text.retrofits.RetrofitClient;
import com.example.text.utils.Store;
import com.example.text.views.SideBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendsFragment extends Fragment {
    private Button btnCreateGroup,btnAddFriend,btnGroupList;

    private FriendListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private Map<String, Integer> mLetterPositions = new HashMap<>();
    private List<FriendListItem> friendList;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);

        btnCreateGroup=rootView.findViewById(R.id.btn_createGroup);
        btnAddFriend=rootView.findViewById(R.id.btn_addFriend);
        btnGroupList=rootView.findViewById(R.id.btn_groupList);

        // 初始化RecyclerView
        RecyclerView rvFriends = rootView.findViewById(R.id.rv_friends);
        mLayoutManager = new LinearLayoutManager(getContext());
        rvFriends.setLayoutManager(mLayoutManager);

        friendList=new ArrayList<>();
        fetchContacts();

//        mAdapter = new FriendListAdapter(generateData());
        mAdapter = new FriendListAdapter(friendList);
        mAdapter.setOnItemClickListener((view, position) -> {
            // position 是被点击项的位置（从 0 开始）
            // 根据 position 获取数据
            FriendListItem friendListItem= mAdapter.getFriendListItem(position);
            Intent intent = new Intent(getActivity(), FriendProfileActivity.class);
            intent.putExtra("friendName", friendListItem.getContactName());
            startActivity(intent);
        });
        rvFriends.setAdapter(mAdapter);

        // 侧边栏事件监听
        @SuppressLint("WrongViewCast") SideBar sideBar = rootView.findViewById(R.id.side_bar);
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

        btnCreateGroup.setOnClickListener(v->showCreateGroupDialog());
        btnAddFriend.setOnClickListener(v->showAddContactDialog());

        return rootView;
    }

    private void fetchContacts(){
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        //TODO
        Call<FetchContactsResponse> call = apiService.getContact(Store.getInstance(requireActivity().getApplicationContext()).getData("token"));

        call.enqueue(new Callback<FetchContactsResponse>() {
            @Override
            public void onResponse(@NonNull Call<FetchContactsResponse> call, @NonNull Response<FetchContactsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    friendList = response.body().getData();
                    FriendListItem.sortByFiestLetter(friendList);
                } else {
                    Toast.makeText(requireActivity(), "网络错误: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<FetchContactsResponse> call, @NonNull Throwable t) {
                Toast.makeText(requireActivity(), "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<FriendListItem> generateData() {
        List<FriendListItem> list = new ArrayList<>();
        list.add(new FriendListItem("Alice","http://10.29.61.159:5050/images/2024-11-27/2.png"));
        list.add(new FriendListItem("Bob","http://10.29.61.159:5050/images/2024-11-27/2.png"));
        list.add(new FriendListItem("Charlie","http://10.29.61.159:5050/images/2024-11-27/2.png"));
        list.add(new FriendListItem("Alice"));
        list.add(new FriendListItem("Bob"));
        list.add(new FriendListItem("Charlie"));
        list.add(new FriendListItem("Alice"));
        list.add(new FriendListItem("Bob"));
        list.add(new FriendListItem("Charlie"));
        list.add(new FriendListItem("Alice"));
        list.add(new FriendListItem("Bob"));
        list.add(new FriendListItem("Charlie"));
        list.add(new FriendListItem("Alice"));
        list.add(new FriendListItem("Bob"));
        list.add(new FriendListItem("Charlie"));
        list.add(new FriendListItem("Alice"));
        list.add(new FriendListItem("Bob"));
        list.add(new FriendListItem("Charlie"));
        list.add(new FriendListItem("Alice"));
        list.add(new FriendListItem("Bob"));
        list.add(new FriendListItem("Charlie"));
        list.add(new FriendListItem("王嘉诚","http://10.29.61.159:5050/images/2024-11-27/2.png"));
        list.add(new FriendListItem("于洋","http://10.29.61.159:5050/images/2024-11-27/2.png"));
        list.add(new FriendListItem("谭杰","http://10.29.61.159:5050/images/2024-11-27/2.png"));
        list.add(new FriendListItem("111","http://10.29.61.159:5050/images/2024-11-27/2.png"));
        list.add(new FriendListItem("222"));
        // 更多数据...

        // 按首字母排序
//        Collections.sort(list, (f1, f2) ->
//                f1.getFirstLetter().compareTo(f2.getFirstLetter()));
        FriendListItem.sortByFiestLetter(list);
        return list;
    }

    private void showCreateGroupDialog() {
        CreateGroupDialogFragment dialog = new CreateGroupDialogFragment();
        dialog.setOnGroupCreateListener((groupName, groupNotice) -> {
            if(Objects.equals(groupName, "")){
                return;
            }
            Log.d("Group", "群名称: " + groupName + ", 群公告: " + groupNotice);

            ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
            Call<CreateGroupResponse> call = apiService.saveGroup(new CreateGroupRequest(groupName,groupNotice), Store.getInstance(requireActivity().getApplicationContext()).getData("token"));

            call.enqueue(new Callback<CreateGroupResponse>() {
                @Override
                public void onResponse(@NonNull Call<CreateGroupResponse> call, @NonNull Response<CreateGroupResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        CreateGroupResponseInfo info=response.body().getData();
                        AvatarModel avatarModel=new AvatarModel(requireActivity().getApplicationContext());
                        avatarModel.saveAvatar(info.getGroupId(),info.getAvatarUrl());
                        Toast.makeText(requireActivity(), "群聊创建成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "网络异常", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CreateGroupResponse> call, @NonNull Throwable t) {
                    Toast.makeText(requireContext(), "网络异常", Toast.LENGTH_SHORT).show();
                }
            });
        });
        dialog.show(requireActivity().getSupportFragmentManager(), "创建群聊");
    }

    private void showAddContactDialog() {
        AddContactDialogFragment dialog = new AddContactDialogFragment();
        dialog.setOnContactAddListener((type, id, message) -> {
            String typeName = type == AddContactDialogFragment.TYPE_FRIEND ? "好友" : "群组";
            Log.d("Contact", "类型: " + typeName + ", ID: " + id + ", 信息: " + message);
            ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
            Call<Void> call = apiService.applyAdd(new AddFriendRequest(id,message), Store.getInstance(requireActivity().getApplicationContext()).getData("token"));

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    Toast.makeText(requireActivity(), "申请已发送", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    Toast.makeText(requireActivity(), "网络异常", Toast.LENGTH_SHORT).show();
                }
            });
        });
        dialog.show(getParentFragmentManager(), "加好友/群");
    }
}

//public class DashboardFragment extends Fragment {
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_dashboard, container, false);
//    }
//}

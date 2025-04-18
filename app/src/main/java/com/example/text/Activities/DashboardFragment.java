package com.example.text.Activities;

// HomeFragment.java

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.text.Adapters.FriendListAdapter;
import com.example.text.R;
import com.example.text.dataModel.FriendListItem;
import com.example.text.views.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardFragment extends Fragment {

    private FriendListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private Map<String, Integer> mLetterPositions = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        // 初始化RecyclerView
        RecyclerView rvFriends = rootView.findViewById(R.id.rv_friends);
        mLayoutManager = new LinearLayoutManager(getContext());
        rvFriends.setLayoutManager(mLayoutManager);

        mAdapter = new FriendListAdapter(generateData());
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
        return rootView;
    }

    private List<FriendListItem> generateData() {
        List<FriendListItem> list = new ArrayList<>();
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
        list.add(new FriendListItem("Alice"));
        list.add(new FriendListItem("Bob"));
        list.add(new FriendListItem("Charlie"));
        list.add(new FriendListItem("王嘉诚"));
        list.add(new FriendListItem("于洋"));
        list.add(new FriendListItem("谭杰"));
        list.add(new FriendListItem("111"));
        list.add(new FriendListItem("222"));
        // 更多数据...

        // 按首字母排序
//        Collections.sort(list, (f1, f2) ->
//                f1.getFirstLetter().compareTo(f2.getFirstLetter()));
        FriendListItem.sortByFiestLetter(list);
        return list;
    }
}

//public class DashboardFragment extends Fragment {
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_dashboard, container, false);
//    }
//}

package com.example.text.activities.chatActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.text.adapters.SessionListAdapter;
import com.example.text.R;
import com.example.text.dataModel.SessionListItem;

import java.util.ArrayList;
import java.util.List;

public class ChatListFragment extends Fragment {
    private RecyclerView recyclerView;
    private SessionListAdapter adapter;
    private List<SessionListItem> sessionList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 1. 加载 Fragment 的布局
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // 初始化 RecyclerView
        recyclerView = rootView.findViewById(R.id.rv_sessionList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        sessionList=generateData();
        adapter = new SessionListAdapter(sessionList);
        adapter.setOnItemClickListener((view, position) -> {
            // position 是被点击项的位置（从 0 开始）
            // 根据 position 获取数据
            SessionListItem sessionListItem= sessionList.get(position);
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            intent.putExtra("friendName", sessionListItem.getName());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    private List<SessionListItem> generateData() {
        List<SessionListItem> list = new ArrayList<>();
        list.add(new SessionListItem("wjc","http://10.29.61.159:5050/images/2024-11-27/2.png","我是wjc",1354689131));
        list.add(new SessionListItem("yy","http://10.29.61.159:5050/images/2024-11-27/2.png","我是yy",648943));
        list.add(new SessionListItem("tj","http://10.29.61.159:5050/images/2024-11-27/2.png","我是tj",3156));
        list.add(new SessionListItem("wjc","http://10.29.61.159:5050/images/2024-11-27/2.png","我是wjc",1354689131));
        list.add(new SessionListItem("yy","http://10.29.61.159:5050/images/2024-11-27/2.png","我是yy",648943));
        list.add(new SessionListItem("tj","http://10.29.61.159:5050/images/2024-11-27/2.png","我是tj",3156));
        list.add(new SessionListItem("wjc","http://10.29.61.159:5050/images/2024-11-27/2.png","我是wjc",1354689131));
        list.add(new SessionListItem("yy","http://10.29.61.159:5050/images/2024-11-27/2.png","我是yy",648943));
        list.add(new SessionListItem("tj","http://10.29.61.159:5050/images/2024-11-27/2.png","我是tj",3156));
        list.add(new SessionListItem("wjc","http://10.29.61.159:5050/images/2024-11-27/2.png","我是wjc",1354689131));
        list.add(new SessionListItem("yy","http://10.29.61.159:5050/images/2024-11-27/2.png","我是yy",648943));
        list.add(new SessionListItem("tj","http://10.29.61.159:5050/images/2024-11-27/2.png","我是tj",3156));
        list.add(new SessionListItem("wjc","http://10.29.61.159:5050/images/2024-11-27/2.png","我是wjc",1354689131));
        list.add(new SessionListItem("yy","http://10.29.61.159:5050/images/2024-11-27/2.png","我是yy",648943));
        list.add(new SessionListItem("tj","http://10.29.61.159:5050/images/2024-11-27/2.png","我是tj",3156));
        list.add(new SessionListItem("wjc","http://10.29.61.159:5050/images/2024-11-27/2.png","我是wjc",1354689131));
        list.add(new SessionListItem("yy","http://10.29.61.159:5050/images/2024-11-27/2.png","我是yy",648943));
        list.add(new SessionListItem("tj","","我是tj",3156));

        SessionListItem.sortByLastReceiveTime(list);
        return list;
    }
}


//package com.example.text.Activities.chatActivities;
//
//// HomeFragment.java
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ListView;
//import android.widget.SimpleAdapter;
//
//import androidx.fragment.app.Fragment;
//
//import com.example.text.R;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
////public class HomeFragment extends Fragment {
////    private String[] names=new String[]{"wjc","yy","tj"};
////    private String[] lastChats=new String[]{"我是wjc","我是yy","我是tj"};
////    private int[] avatars=new int[]{R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher};
////
////    @Override
////    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
////        List<Map<String,Object>> chatListItem=new ArrayList<Map<String,Object>>();
////        for(int i=0;i< names.length;++i){
////            Map<String,Object> showItem=new HashMap<String,Object>();
////            showItem.put("avatar",avatars[i]);
////            showItem.put("name",names[i]);
////            showItem.put("lastChat",lastChats[i]);
////            chatListItem.add(showItem);
////        }
////        SimpleAdapter simpleAdapter=new SimpleAdapter(getActivity().getApplicationContext(), chatListItem,R.layout.chatlist_item,new String[]{"avatar","name","lastChat"},new int[]{R.id.chatList_avatar,R.id.chatList_name,R.id.chatList_lastChat});
////        ListView listView=(ListView) getActivity().findViewById(R.id.chatlist_test);
////        return inflater.inflate(R.layout.fragment_home, container, false);
////    }
////
////}
//
//public class ChatListFragment extends Fragment {
//    private String[] names = new String[]{"wjc", "yy", "tj","wjc", "yy", "tj","wjc", "yy", "tj","wjc", "yy", "tj"};
//    private String[] lastChats = new String[]{"我是wjc", "我是yy", "我是tj","我是wjc", "我是yy", "我是tj","我是wjc", "我是yy", "我是tj","我是wjc", "我是yy", "我是tj"};
//    private int[] avatars = new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // 1. 加载 Fragment 的布局
//        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
//
//        // 2. 初始化数据
//        List<Map<String, Object>> chatListItem = new ArrayList<>();
//        for (int i = 0; i < names.length; i++) {
//            Map<String, Object> showItem = new HashMap<>();
//            showItem.put("avatar", avatars[i]);
//            showItem.put("name", names[i]);
//            showItem.put("lastChat", lastChats[i]);
//            chatListItem.add(showItem);
//        }
//
//        // 3. 创建 Adapter 并绑定到 ListView
//        SimpleAdapter simpleAdapter = new SimpleAdapter(
//                getActivity(), // 直接使用 Activity 的 Context
//                chatListItem,
//                R.layout.item_session,
//                new String[]{"avatar", "name", "lastChat"},
//                new int[]{R.id.chatList_avatar, R.id.chatList_name, R.id.chatList_lastChat}
//        );
//
//        // 4. 从 Fragment 的布局中查找 ListView
//        ListView listView = rootView.findViewById(R.id.chatlist_test);
//        listView.setAdapter(simpleAdapter); // 关键步骤：设置 Adapter
//
////        listView.setOnItemClickListener((new AdapterView.OnItemClickListener()){
////            @Override
////            public void onItemClick(AdapterView<?> parent,View view,int position,long id){
////
////            }
////        });
//
//        // 在设置 Adapter 后添加以下代码
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // position 是被点击项的位置（从 0 开始）
//                // 根据 position 获取数据
//                String clickedName = names[position];
//                String clickedLastChat = lastChats[position];
//                int clickedAvatar = avatars[position];
//
////                // 根据需求跳转到不同页面
////                if (clickedName.equals("wjc")) {
////                    // 跳转到页面 A
////                    Intent intent = new Intent(getActivity(), MainActivity.class);
////                    intent.putExtra("name", clickedName);
////                    intent.putExtra("avatar", clickedAvatar);
////                    startActivity(intent);
////                } else if (clickedName.equals("yy")) {
////                    // 跳转到页面 B
////                    Intent intent = new Intent(getActivity(), GreetingActivity.class);
////                    intent.putExtra("name", clickedName);
////                    startActivity(intent);
////                } else {
////                    // 默认跳转（例如聊天页面）
////                    Intent intent = new Intent(getActivity(), RegisterActivity.class);
////                    intent.putExtra("name", clickedName);
////                    intent.putExtra("lastChat", clickedLastChat);
////                    startActivity(intent);
////                }
//
////                Intent intent = new Intent(getActivity(), LoginActivity.class);
//////                intent.putExtra("name", clickedName);
//////                intent.putExtra("lastChat", clickedLastChat);
////                startActivity(intent);
//
//                Intent intent = new Intent(getActivity(), ChatActivity.class);
//                intent.putExtra("friendName", clickedName);
////                intent.putExtra("lastChat", clickedLastChat);
//                startActivity(intent);
//            }
//        });
//
////        listView.setOnClickListener(new View.OnClickListener(){
////            @Override
////            public void onClick(View view) {
////                Map<String,Object> data=chatListItem.get()
////                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
////            }
////        });
//
//        return rootView;
//    }
//}
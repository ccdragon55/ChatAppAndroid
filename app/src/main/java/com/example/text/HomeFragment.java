package com.example.text;

// HomeFragment.java

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//public class HomeFragment extends Fragment {
//    private String[] names=new String[]{"wjc","yy","tj"};
//    private String[] lastChats=new String[]{"我是wjc","我是yy","我是tj"};
//    private int[] avatars=new int[]{R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher};
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        List<Map<String,Object>> chatListItem=new ArrayList<Map<String,Object>>();
//        for(int i=0;i< names.length;++i){
//            Map<String,Object> showItem=new HashMap<String,Object>();
//            showItem.put("avatar",avatars[i]);
//            showItem.put("name",names[i]);
//            showItem.put("lastChat",lastChats[i]);
//            chatListItem.add(showItem);
//        }
//        SimpleAdapter simpleAdapter=new SimpleAdapter(getActivity().getApplicationContext(), chatListItem,R.layout.chatlist_item,new String[]{"avatar","name","lastChat"},new int[]{R.id.chatList_avatar,R.id.chatList_name,R.id.chatList_lastChat});
//        ListView listView=(ListView) getActivity().findViewById(R.id.chatlist_test);
//        return inflater.inflate(R.layout.fragment_home, container, false);
//    }
//
//}

public class HomeFragment extends Fragment {
    private String[] names = new String[]{"wjc", "yy", "tj","wjc", "yy", "tj","wjc", "yy", "tj","wjc", "yy", "tj"};
    private String[] lastChats = new String[]{"我是wjc", "我是yy", "我是tj","我是wjc", "我是yy", "我是tj","我是wjc", "我是yy", "我是tj","我是wjc", "我是yy", "我是tj"};
    private int[] avatars = new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 1. 加载 Fragment 的布局
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // 2. 初始化数据
        List<Map<String, Object>> chatListItem = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            Map<String, Object> showItem = new HashMap<>();
            showItem.put("avatar", avatars[i]);
            showItem.put("name", names[i]);
            showItem.put("lastChat", lastChats[i]);
            chatListItem.add(showItem);
        }

        // 3. 创建 Adapter 并绑定到 ListView
        SimpleAdapter simpleAdapter = new SimpleAdapter(
                getActivity(), // 直接使用 Activity 的 Context
                chatListItem,
                R.layout.chatlist_item,
                new String[]{"avatar", "name", "lastChat"},
                new int[]{R.id.chatList_avatar, R.id.chatList_name, R.id.chatList_lastChat}
        );

        // 4. 从 Fragment 的布局中查找 ListView
        ListView listView = rootView.findViewById(R.id.chatlist_test);
        listView.setAdapter(simpleAdapter); // 关键步骤：设置 Adapter

//        listView.setOnItemClickListener((new AdapterView.OnItemClickListener()){
//            @Override
//            public void onItemClick(AdapterView<?> parent,View view,int position,long id){
//
//            }
//        });
//        listView.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                Map<String,Object> data=chatListItem.get()
//                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
//            }
//        });

        return rootView;
    }
}
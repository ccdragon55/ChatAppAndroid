//package com.example.text;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.Arrays;
//import java.util.List;
//
//public class ChatListActivity extends AppCompatActivity {
//    private RecyclerView groupList;
//    private RecyclerView friendList;
//    private boolean isGroupExpanded = false;
//    private boolean isFriendExpanded = false;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        // 初始化群聊列表
//        groupList = findViewById(R.id.group_list);
//        setupRecyclerView(groupList, getGroupData(), true);
//
//        // 初始化好友列表
//        friendList = findViewById(R.id.friend_list);
//        setupRecyclerView(friendList, getFriendData(), false);
//
//        // 设置点击监听
//        findViewById(R.id.group_header).setOnClickListener(v -> toggleList(groupList, isGroupExpanded));
//        findViewById(R.id.friend_header).setOnClickListener(v -> toggleList(friendList, isFriendExpanded));
//    }
//
//    private void setupRecyclerView(RecyclerView recyclerView, List<Contact> data, boolean isGroup) {
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        ContactAdapter adapter = new ContactAdapter(data, position -> {
//            // 点击处理
//            Contact contact = data.get(position);
//            Toast.makeText(this, "Clicked: " + contact.getName(), Toast.LENGTH_SHORT).show();
//        });
//        recyclerView.setAdapter(adapter);
//    }
//
//    private void toggleList(RecyclerView listView, boolean isExpanded) {
//        if (isExpanded) {
//            listView.setVisibility(View.GONE);
//            // 可以添加收缩动画
//        } else {
//            listView.setVisibility(View.VISIBLE);
//            // 可以添加展开动画
//        }
//        if (listView == groupList) isGroupExpanded = !isGroupExpanded;
//        else isFriendExpanded = !isFriendExpanded;
//    }
//
//    // 数据示例
//    private List<Contact> getGroupData() {
//        return Arrays.asList(
//                new Contact("工作群", R.drawable.group_avatar),
//                new Contact("同学群", R.drawable.group_avatar)
//        );
//    }
//
//    private List<Contact> getFriendData() {
//        return Arrays.asList(
//                new Contact("张三", R.drawable.user_avatar),
//                new Contact("李四", R.drawable.user_avatar)
//        );
//    }
//
//    // 数据模型
//    static class Contact {
//        private String name;
//        private int avatarRes;
//
//        // 构造方法、getter 略
//    }
//
//    // 适配器
//    static class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
//        private final List<Contact> data;
//        private final OnItemClickListener listener;
//
//        public ContactAdapter(List<Contact> data, OnItemClickListener listener) {
//            this.data = data;
//            this.listener = listener;
//        }
//
//        @NonNull
//        @Override
//        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.item_contact, parent, false);
//            return new ViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//            Contact contact = data.get(position);
//            holder.avatar.setImageResource(contact.getAvatarRes());
//            holder.name.setText(contact.getName());
//            holder.itemView.setOnClickListener(v -> listener.onItemClick(position));
//        }
//
//        @Override
//        public int getItemCount() {
//            return data.size();
//        }
//
//        static class ViewHolder extends RecyclerView.ViewHolder {
//            CircleImageView avatar;
//            TextView name;
//
//            public ViewHolder(@NonNull View itemView) {
//                super(itemView);
//                avatar = itemView.findViewById(R.id.iv_avatar);
//                name = itemView.findViewById(R.id.tv_name);
//            }
//        }
//
//        interface OnItemClickListener {
//            void onItemClick(int position);
//        }
//    }
//}

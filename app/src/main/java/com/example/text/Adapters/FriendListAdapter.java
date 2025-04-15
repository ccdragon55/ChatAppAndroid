package com.example.text.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.text.R;
import com.example.text.dataModel.FriendListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    // 带标题的数据集合（交替存储标题和项目）
    private List<Object> mData = new ArrayList<>();
    private Map<String, Integer> mLetterPositions = new HashMap<>();

    // 构造函数需要处理原始数据
    public FriendListAdapter(List<FriendListItem> friends) {
        processData(friends);
    }

    //================ 必须实现的方法 ================//
    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position) instanceof String ? TYPE_HEADER : TYPE_ITEM;
    }
    //=============================================//

    // 数据预处理方法（核心逻辑）
    private void processData(List<FriendListItem> originalList) {
        mData.clear();
        String currentLetter = "";

        for (FriendListItem friend : originalList) {
            String firstLetter = friend.getFirstLetter();
            if (!firstLetter.equals(currentLetter)) {
                mData.add(firstLetter); // 添加字母标题
                currentLetter = firstLetter;
            }
            mData.add(friend); // 添加好友数据
        }
    }

    // 根据字母获取位置
    public Integer getLetterPosition(String letter) {
        return mLetterPositions.get(letter);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_letter_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_friend, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            String letter = (String) mData.get(position);
            ((HeaderViewHolder) holder).bind(letter);
            mLetterPositions.put(letter, position); // 记录字母位置
        } else {
            FriendListItem friend = (FriendListItem) mData.get(position);
            ((ItemViewHolder) holder).bind(friend);
        }
    }

    //================ ViewHolder 定义 ================//
    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvLetter;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            tvLetter = itemView.findViewById(R.id.tv_letter);
        }

        void bind(String letter) {
            tvLetter.setText(letter);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
        }

        void bind(FriendListItem friend) {
            tvName.setText(friend.getName());
        }
    }
}

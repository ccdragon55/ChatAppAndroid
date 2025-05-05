package com.example.text.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.text.R;
import com.example.text.dataModel.ChatMessage;
import com.example.text.utils.Store;

import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ChatMessage> messageList;

    public ChatMessageAdapter(List<ChatMessage> messageList) {
        this.messageList = messageList;
    }

    // 根据消息类型返回不同的 ViewHolder
    @Override
    public int getItemViewType(int position) {
        return messageList.get(position).getType();
    }

    // 创建 ViewHolder
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ChatMessage.TYPE_RECEIVED) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_others, parent, false);
            return new LeftViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_self, parent, false);
            return new RightViewHolder(view);
        }
    }

    // 绑定数据
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messageList.get(position);
        if (holder instanceof LeftViewHolder) {
            ((LeftViewHolder) holder).tvContent.setText(message.getMessageContent());
            ((LeftViewHolder) holder).tvNickName.setText(message.getSendUserNickName());
        } else if (holder instanceof RightViewHolder) {
            ((RightViewHolder) holder).tvContent.setText(message.getMessageContent());
            ((RightViewHolder) holder).tvNickName.setText(message.getSendUserNickName());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    // 左侧消息 ViewHolder
    static class LeftViewHolder extends RecyclerView.ViewHolder {
        TextView tvNickName;
        TextView tvContent;

        LeftViewHolder(View itemView) {
            super(itemView);
            tvNickName = itemView.findViewById(R.id.tv_nickName_left);
            tvContent = itemView.findViewById(R.id.tv_content_left);
        }
    }

    // 右侧消息 ViewHolder
    static class RightViewHolder extends RecyclerView.ViewHolder {
        TextView tvNickName;
        TextView tvContent;

        RightViewHolder(View itemView) {
            super(itemView);
            tvNickName = itemView.findViewById(R.id.tv_nickName_right);
            tvContent = itemView.findViewById(R.id.tv_content_right);
        }
    }
}

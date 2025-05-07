package com.example.text.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.text.R;
import com.example.text.dataModel.ChatMessage;

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
        if (viewType == ChatMessage.TYPE_RECEIVED_TEXT) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_others_text, parent, false);
            return new LeftViewHolder(view);
        } else if (viewType == ChatMessage.TYPE_SENT_TEXT) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_self_text, parent, false);
            return new RightViewHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_system, parent, false);
            return new SystemViewHolder(view);
        }
    }

    // 绑定数据
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messageList.get(position);
        if (holder instanceof SystemViewHolder){
            ((SystemViewHolder) holder).tvDateHeader.setVisibility(message.isShowDate()?View.VISIBLE:View.GONE);
            ((SystemViewHolder) holder).tvDateHeader.setText(message.getDate());
            ((SystemViewHolder) holder).tvContent.setText(message.getMessageContent());
        }else if (holder instanceof LeftViewHolder) {
            ((LeftViewHolder) holder).tvDateHeader.setVisibility(message.isShowDate()?View.VISIBLE:View.GONE);
            ((LeftViewHolder) holder).tvDateHeader.setText(message.getDate());
            ((LeftViewHolder) holder).tvContent.setText(message.getMessageContent());
            ((LeftViewHolder) holder).tvNickName.setText(message.getSendUserNickName());
            Glide.with(((LeftViewHolder) holder).ivAvatar.getContext())
                    .load(message.getUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(((LeftViewHolder) holder).ivAvatar);
        } else if (holder instanceof RightViewHolder) {
            ((RightViewHolder) holder).tvDateHeader.setVisibility(message.isShowDate()?View.VISIBLE:View.GONE);
            ((RightViewHolder) holder).tvDateHeader.setText(message.getDate());
            ((RightViewHolder) holder).tvContent.setText(message.getMessageContent());
            ((RightViewHolder) holder).tvNickName.setText(message.getSendUserNickName());
            Glide.with(((RightViewHolder) holder).ivAvatar.getContext())
                    .load(message.getUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(((RightViewHolder) holder).ivAvatar);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    //系统消息 ViewHolder
    static class SystemViewHolder extends RecyclerView.ViewHolder {
        TextView tvDateHeader;
        TextView tvContent;

        SystemViewHolder(View itemView) {
            super(itemView);
            tvDateHeader = itemView.findViewById(R.id.tv_DateHeader);
            tvContent = itemView.findViewById(R.id.tv_content_system);
        }
    }

    // 左侧消息 ViewHolder
    static class LeftViewHolder extends RecyclerView.ViewHolder {
        TextView tvDateHeader;
        ImageView ivAvatar;
        TextView tvNickName;
        TextView tvContent;

        LeftViewHolder(View itemView) {
            super(itemView);
            tvDateHeader = itemView.findViewById(R.id.tv_DateHeader);
            ivAvatar = itemView.findViewById(R.id.iv_avatar_left);
            tvNickName = itemView.findViewById(R.id.tv_nickName_left);
            tvContent = itemView.findViewById(R.id.tv_content_left);
        }
    }

    // 右侧消息 ViewHolder
    static class RightViewHolder extends RecyclerView.ViewHolder {
        TextView tvDateHeader;
        ImageView ivAvatar;
        TextView tvNickName;
        TextView tvContent;

        RightViewHolder(View itemView) {
            super(itemView);
            tvDateHeader = itemView.findViewById(R.id.tv_DateHeader);
            ivAvatar = itemView.findViewById(R.id.iv_avatar_right);
            tvNickName = itemView.findViewById(R.id.tv_nickName_right);
            tvContent = itemView.findViewById(R.id.tv_content_right);
        }
    }
}

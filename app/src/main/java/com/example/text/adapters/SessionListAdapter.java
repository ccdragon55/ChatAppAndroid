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
import com.example.text.listeners.OnItemClickListener;
import com.example.text.R;
import com.example.text.dataModel.SessionListItem;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.List;

public class SessionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SessionListItem> sessionList;
    private OnItemClickListener mOnItemClickListener;

    public SessionListAdapter(List<SessionListItem> sessionList) {
        this.sessionList = sessionList;
    }

    // 创建 ViewHolder
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_session, parent, false);
        return new SessionViewHolder(view);
    }

    // 绑定数据
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SessionListItem session = sessionList.get(position);
        ((SessionViewHolder) holder).bind(session);

        // 👇 添加点击事件绑定
        holder.itemView.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return sessionList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    // 左侧消息 ViewHolder
    static class SessionViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        NotificationBadge badge;
        TextView tvName;
        TextView tvDate;
        TextView tvLastMessage;


        SessionViewHolder(View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.iv_sessionList_avatar);
            badge = itemView.findViewById(R.id.badge);
            tvName = itemView.findViewById(R.id.tv_sessionList_name);
            tvDate = itemView.findViewById(R.id.tv_sessionList_date);
            tvLastMessage = itemView.findViewById(R.id.tv_sessionList_lastMessage);
        }

        void bind(SessionListItem session) {
            Glide.with(ivAvatar.getContext())
                    .load(session.getAvatarUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(ivAvatar);
            badge.setNumber(session.getNoReadCount());
            tvName.setText(session.getContactName());
            tvDate.setText(session.getDate());
            tvLastMessage.setText(session.getLastMessage());
        }
    }
}
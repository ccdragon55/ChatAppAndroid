package com.example.text.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.text.R;
import com.example.text.dataModel.ApplyInfoItem;

import java.util.List;

public class ApplyInfoAdapter extends RecyclerView.Adapter<ApplyInfoAdapter.ViewHolder> {
    private List<ApplyInfoItem> requests;
    private OnActionClickListener actionListener;
    private OnItemClickListener itemClickListener;

    public ApplyInfoAdapter(List<ApplyInfoItem> requests) {
        this.requests = requests;
    }

    /**
     * 点击整个 item（除按钮区）回调接口
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    /**
     * 点击同意/拒绝按钮回调接口
     */
    public interface OnActionClickListener {
        void onAcceptClick(int position);
        void onRejectClick(int position);
    }

    public void setOnActionClickListener(OnActionClickListener listener) {
        this.actionListener = listener;
    }

    // 更新指定项的状态
    public void updateRequestStatus(int position, int status) {
        if (position >= 0 && position < requests.size()) {
            requests.get(position).setStatus(status);
            notifyItemChanged(position);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_apply_info, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ApplyInfoItem request = requests.get(position);

        // 加载头像
        Glide.with(holder.itemView)
                .load(request.getAvatarUrl())
                .circleCrop()
                .into(holder.ivAvatar);

        holder.tvNickname.setText(request.getApplyUserNickName());
        holder.tvMessage.setText(request.getApplyInfo());
        holder.tvMessage.setMaxLines(request.isExpanded() ? Integer.MAX_VALUE : 1);

        // 根据状态显示操作按钮或文本
        if (request.getStatus() == 0) {
            holder.btnAccept.setVisibility(View.VISIBLE);
            holder.btnReject.setVisibility(View.VISIBLE);
            holder.tvStatus.setVisibility(View.GONE);
        } else {
            holder.btnAccept.setVisibility(View.GONE);
            holder.btnReject.setVisibility(View.GONE);
            holder.tvStatus.setVisibility(View.VISIBLE);
            holder.tvStatus.setText(request.getStatus() == 1 ? "已添加" : "已拒绝");
        }

        // 点击展开/折叠消息
        holder.tvMessage.setOnClickListener(v -> {
            request.setExpanded(!request.isExpanded());
            notifyItemChanged(holder.getAdapterPosition());
        });

        // 整体 item（除按钮）点击
        holder.itemView.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION && itemClickListener != null) {
                itemClickListener.onItemClick(pos);
            }
        });

        // 同意按钮点击
        holder.btnAccept.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION && actionListener != null) {
                actionListener.onAcceptClick(pos);
            }
        });

        // 拒绝按钮点击
        holder.btnReject.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION && actionListener != null) {
                actionListener.onRejectClick(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return requests == null ? 0 : requests.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView tvNickname;
        TextView tvMessage;
        Button btnAccept;
        Button btnReject;
        TextView tvStatus;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            tvNickname = itemView.findViewById(R.id.tv_nickname);
            tvMessage = itemView.findViewById(R.id.tv_message);
            btnAccept = itemView.findViewById(R.id.btn_accept);
            btnReject = itemView.findViewById(R.id.btn_reject);
            tvStatus = itemView.findViewById(R.id.tv_status);
            // 不在这里设置点击，全部在 onBindViewHolder 处理
        }
    }
}


//public class ApplyInfoAdapter extends RecyclerView.Adapter<ApplyInfoAdapter.ViewHolder> {
//    private List<ApplyInfoItem> requests;
//    private OnActionClickListener listener;
//
//    public ApplyInfoAdapter(List<ApplyInfoItem> requests) {
//        this.requests = requests;
//    }
//
//    public interface OnItemClickListener {
//        void onItemClick(int position);
//    }
//
//    private OnItemClickListener itemClickListener;
//
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        this.itemClickListener = listener;
//    }
//
//    // 更新指定项的状态
//    public void updateRequestStatus(int position, int status) {
//        if (position >= 0 && position < requests.size()) {
//            requests.get(position).setStatus(status);
//            notifyItemChanged(position);
//        }
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_apply_info, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        ApplyInfoItem request = requests.get(position);
//
//        // 加载头像
//        Glide.with(holder.itemView)
//                .load(request.getAvatarUrl())
//                .circleCrop()
//                .into(holder.ivAvatar);
//
//        holder.tvNickname.setText(request.getApplyUserNickName());
//        holder.tvMessage.setText(request.getApplyInfo());
//        holder.tvMessage.setMaxLines(request.isExpanded() ? Integer.MAX_VALUE : 1);
//
//        // 根据状态显示操作按钮或文本
//        if (request.getStatus()==0) {
//            holder.btnAccept.setVisibility(View.VISIBLE);
//            holder.btnReject.setVisibility(View.VISIBLE);
//            holder.tvStatus.setVisibility(View.GONE);
//        } else {
//            holder.btnAccept.setVisibility(View.GONE);
//            holder.btnReject.setVisibility(View.GONE);
//            holder.tvStatus.setVisibility(View.VISIBLE);
//            holder.tvStatus.setText(request.getStatus()==1 ? "已添加" : "已拒绝");
//        }
//
//        // 点击展开/折叠消息
//        holder.tvMessage.setOnClickListener(v -> {
//            request.setExpanded(!request.isExpanded());
//            notifyItemChanged(position);
//        });
//
//        // 同意按钮点击
//        holder.btnAccept.setOnClickListener(v -> {
//            if (listener != null) {
//                listener.onAcceptClick(position);
//            }
//        });
//
//        // 拒绝按钮点击
//        holder.btnReject.setOnClickListener(v -> {
//            if (listener != null) {
//                listener.onRejectClick(position);
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return requests.size();
//    }
//
//    static class ViewHolder extends RecyclerView.ViewHolder {
//        ImageView ivAvatar;
//        TextView tvNickname;
//        TextView tvMessage;
//        Button btnAccept;
//        Button btnReject;
//        TextView tvStatus;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            ivAvatar = itemView.findViewById(R.id.iv_avatar);
//            tvNickname = itemView.findViewById(R.id.tv_nickname);
//            tvMessage = itemView.findViewById(R.id.tv_message);
//            btnAccept = itemView.findViewById(R.id.btn_accept);
//            btnReject = itemView.findViewById(R.id.btn_reject);
//            tvStatus = itemView.findViewById(R.id.tv_status);
//
//            itemView.setOnClickListener(v -> {
//                int position = getAdapterPosition();
//                //TODO
////                if (position != RecyclerView.NO_POSITION && itemClickListener != null) {
////                    itemClickListener.onItemClick(position);
////                }
//            });
//
//            // 禁止按钮区域触发父布局点击事件
//            btnAccept.setOnClickListener(v -> {}); // 原有逻辑已处理
//            btnReject.setOnClickListener(v -> {}); // 原有逻辑已处理
//        }
//    }
//
//    public interface OnActionClickListener {
//        void onAcceptClick(int position);
//        void onRejectClick(int position);
//    }
//
//    public void setOnActionClickListener(OnActionClickListener listener) {
//        this.listener = listener;
//    }
//}
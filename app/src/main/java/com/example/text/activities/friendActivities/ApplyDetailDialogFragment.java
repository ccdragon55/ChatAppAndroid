package com.example.text.activities.friendActivities;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.text.R;
import com.example.text.dataModel.ApplyInfoItem;

public class ApplyDetailDialogFragment extends DialogFragment {

    private static final String ARG_MESSAGE = "message";
    private Message message;
    private OnActionClickListener listener;

    // 定义消息实体类
    public static class Message {
        private String nickname;
        private String applyInfo;

        public Message(String nickname, String applyInfo) {
            this.nickname = nickname;
            this.applyInfo = applyInfo;
        }
    }

    // 创建实例的方法
    public static ApplyDetailDialogFragment newInstance(ApplyInfoItem message) {
        ApplyDetailDialogFragment fragment = new ApplyDetailDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_MESSAGE, (Parcelable) message);
        fragment.setArguments(args);
        return fragment;
    }

    // 回调接口
    public interface OnActionClickListener {
        void onAgree();
        void onReject();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            message = getArguments().getParcelable(ARG_MESSAGE);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_apply_detail, null);

        // 初始化视图
        TextView tvNickname = view.findViewById(R.id.tv_nickname);
        TextView tvApplyInfo = view.findViewById(R.id.tv_apply_info);
        Button btnAgree = view.findViewById(R.id.btn_agree);
        Button btnReject = view.findViewById(R.id.btn_reject);

        // 设置数据
        if (message != null) {
            tvNickname.setText(message.nickname);
            tvApplyInfo.setText(message.applyInfo);
        }

        // 设置按钮点击
        btnAgree.setOnClickListener(v -> {
            if (listener != null) listener.onAgree();
            dismiss();
        });

        btnReject.setOnClickListener(v -> {
            if (listener != null) listener.onReject();
            dismiss();
        });

        builder.setView(view)
                .setTitle("消息详情");

        return builder.create();
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        // 设置对话框尺寸
//        Dialog dialog = getDialog();
//        if (dialog != null) {
//            Window window = dialog.getWindow();
//            if (window != null) {
//                DisplayMetrics metrics = new DisplayMetrics();
//                requireActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
//
//                // 设置宽度为屏幕宽度的50%
//                int width = (int) (metrics.widthPixels * 0.8);
//
//                // 设置最大高度为屏幕高度的70%
//                int maxHeight = (int) (metrics.heightPixels * 0.7);
//
//                window.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
//                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            }
//        }
//    }

    public void setOnActionClickListener(OnActionClickListener listener) {
        this.listener = listener;
    }
}

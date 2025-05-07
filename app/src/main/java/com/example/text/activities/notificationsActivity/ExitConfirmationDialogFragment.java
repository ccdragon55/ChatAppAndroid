package com.example.text.activities.notificationsActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.text.R;
import com.example.text.activities.friendActivities.ApplyDetailDialogFragment;

public class ExitConfirmationDialogFragment extends DialogFragment {
    private ExitConfirmationDialogFragment.OnActionClickListener listener;

    // 创建实例的方法
    public static ExitConfirmationDialogFragment newInstance() {
        return new ExitConfirmationDialogFragment();
    }

    // 回调接口
    public interface OnActionClickListener {
        void onAgree();
        void onReject();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.diolog_exit_confirmation, null);

        // 初始化视图
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button btnAgree = view.findViewById(R.id.btn_agree);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button btnReject = view.findViewById(R.id.btn_reject);

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
                .setTitle("确定退出当前账号吗?");

        return builder.create();
    }

    public void setOnActionClickListener(ExitConfirmationDialogFragment.OnActionClickListener listener) {
        this.listener = listener;
    }
}

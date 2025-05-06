package com.example.text.activities.profileActivities;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.text.R;
import com.google.android.material.button.MaterialButton;

public class ChangePasswordDialogFragment extends DialogFragment {

    private EditText etPassword;
    private OnPasswordChangedListener listener;

    // 定义回调接口
    public interface OnPasswordChangedListener {
        void onPasswordChanged(String newPassword);
    }

    public void setOnPasswordChangedListener(OnPasswordChangedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_change_password, null);

        etPassword = view.findViewById(R.id.et_password);
        MaterialButton btnConfirm = view.findViewById(R.id.btn_confirm);
        MaterialButton btnCancel = view.findViewById(R.id.btn_cancel);

        // 确认按钮点击
        btnConfirm.setOnClickListener(v -> {
            String password = etPassword.getText().toString().trim();
            if (validatePassword(password)) {
                if (listener != null) {
                    listener.onPasswordChanged(password);
                }
                dismiss();
            }
        });

        // 取消按钮点击
        btnCancel.setOnClickListener(v -> dismiss());

        builder.setView(view)
                .setTitle("修改密码");

        return builder.create();
    }

    // 密码验证逻辑
    private boolean validatePassword(String password) {
        if (password.isEmpty()) {
            etPassword.setError("密码不能为空");
            return false;
        }
        if (password.length() < 6) {
            etPassword.setError("密码至少6位");
            return false;
        }
        return true;
    }

//    // 调整对话框宽度（50% 屏幕宽度）
//    @Override
//    public void onStart() {
//        super.onStart();
//        Dialog dialog = getDialog();
//        if (dialog != null) {
//            Window window = dialog.getWindow();
//            if (window != null) {
//                DisplayMetrics metrics = new DisplayMetrics();
//                requireActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
//                int width = (int) (metrics.widthPixels * 0.5);
//                window.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
//            }
//        }
//    }
}

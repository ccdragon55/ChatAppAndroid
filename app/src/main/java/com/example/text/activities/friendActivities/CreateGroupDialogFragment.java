package com.example.text.activities.friendActivities;

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

public class CreateGroupDialogFragment extends DialogFragment {

    private EditText etGroupName, etGroupNotice;
    private OnGroupCreateListener listener;

    // 定义回调接口
    public interface OnGroupCreateListener {
        void onCreateGroup(String groupName, String groupNotice);
    }

    public void setOnGroupCreateListener(OnGroupCreateListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_create_group, null);

        etGroupName = view.findViewById(R.id.et_group_name);
        etGroupNotice = view.findViewById(R.id.et_group_notice);
        MaterialButton btnCreate = view.findViewById(R.id.btn_create);
        MaterialButton btnCancel = view.findViewById(R.id.btn_cancel);

        // 创建按钮点击
        btnCreate.setOnClickListener(v -> {
            String groupName = etGroupName.getText().toString().trim();
            String groupNotice = etGroupNotice.getText().toString().trim();
            if (validateInput(groupName)) {
                if (listener != null) {
                    listener.onCreateGroup(groupName, groupNotice);
                }
                dismiss();
            }
        });

        // 取消按钮点击
        btnCancel.setOnClickListener(v -> dismiss());

        builder.setView(view)
                .setTitle("创建群聊");

        return builder.create();
    }

    // 输入验证
    private boolean validateInput(String groupName) {
        if (groupName.isEmpty()) {
            etGroupName.setError("群名称不能为空");
            return false;
        }
        return true;
    }

//    // 设置对话框宽度为 50%
//    @Override
//    public void onStart() {
//        super.onStart();
//        Dialog dialog = getDialog();
//        if (dialog != null) {
//            Window window = dialog.getWindow();
//            if (window != null) {
//                DisplayMetrics metrics = new DisplayMetrics();
//                requireActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
//                int width = (int) (metrics.widthPixels * 0.8);
//                window.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
//            }
//        }
//    }
}

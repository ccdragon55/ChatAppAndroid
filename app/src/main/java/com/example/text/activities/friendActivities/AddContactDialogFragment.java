package com.example.text.activities.friendActivities;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.text.R;
import com.google.android.material.button.MaterialButton;

public class AddContactDialogFragment extends DialogFragment {

    private RadioGroup rgContactType;
    private EditText etContactId, etApplyMessage;
    private OnContactAddListener listener;

    // 定义类型常量
    public static final int TYPE_FRIEND = 0;
    public static final int TYPE_GROUP = 1;

    // 回调接口
    public interface OnContactAddListener {
        void onContactAdd(int contactType, String contactId, String applyMessage);
    }

    public void setOnContactAddListener(OnContactAddListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_contact, null);

        rgContactType = view.findViewById(R.id.rg_contact_type);
        etContactId = view.findViewById(R.id.et_contact_id);
        etApplyMessage = view.findViewById(R.id.et_apply_message);
        MaterialButton btnSend = view.findViewById(R.id.btn_send);
        MaterialButton btnCancel = view.findViewById(R.id.btn_cancel);

        // 发送按钮点击
        btnSend.setOnClickListener(v -> {
            int selectedType = getSelectedContactType();
            String contactId = etContactId.getText().toString().trim();
            String applyMessage = etApplyMessage.getText().toString().trim();

            if (validateInput(selectedType, contactId, applyMessage)) {
                if (listener != null) {
                    listener.onContactAdd(selectedType, contactId, applyMessage);
                }
                dismiss();
            }
        });

        // 取消按钮点击
        btnCancel.setOnClickListener(v -> dismiss());

        builder.setView(view)
                .setTitle("加好友/群");

        return builder.create();
    }

    // 获取选择的类型
    private int getSelectedContactType() {
        int checkedId = rgContactType.getCheckedRadioButtonId();
        if (checkedId == R.id.rb_friend) {
            return TYPE_FRIEND;
        } else if (checkedId == R.id.rb_group) {
            return TYPE_GROUP;
        }
        return -1; // 未选择
    }

    // 输入验证
    private boolean validateInput(int type, String contactId, String applyMessage) {
        if (type == -1) {
            Toast.makeText(getContext(), "请选择类型", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (contactId.isEmpty()) {
            etContactId.setError("ID 不能为空");
            return false;
        }
//        if (applyMessage.isEmpty()) {
//            etApplyMessage.setError("申请信息不能为空");
//            return false;
//        }
        return true;
    }

    // 设置对话框宽度为 50%
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

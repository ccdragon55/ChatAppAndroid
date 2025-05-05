package com.example.text.activities.profileActivities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.Manifest;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;


import com.example.text.R;
import com.example.text.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;


public class ModifyProfileDialogFragment extends DialogFragment {

    private EditText etNickname, etEmail, etSignature;
    private RadioGroup rgGender;
    private ImageView ivAvatar;
    private OnProfileModifiedListener listener;
    private ActivityResultLauncher<Intent> filePickerLauncher;
    private File selectedFile;

    public interface OnProfileModifiedListener {
//        void onProfileModified(String nickname, int gender, String email, String signature, Bitmap avatar);
void onProfileModified(String nickname, int gender, String email, String signature, File avatar);
    }

    public void setOnProfileModifiedListener(OnProfileModifiedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_modify_profile, null);

        // 初始化视图
        etNickname = view.findViewById(R.id.et_nickname);
        etEmail = view.findViewById(R.id.et_email);
        etSignature = view.findViewById(R.id.et_signature);
        rgGender = view.findViewById(R.id.rg_gender);
        ivAvatar = view.findViewById(R.id.iv_avatar);
        Button btnUpload = view.findViewById(R.id.btn_upload);
        Button btnConfirm = view.findViewById(R.id.btn_confirm);
        Button btnCancel = view.findViewById(R.id.btn_cancel);

        // 注册文件选择器的结果回调
        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // 直接处理结果（无需检查 requestCode）
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        handleFile(uri); // 处理文件
                    }
                }
        );
//        filePickerLauncher = registerForActivityResult(
//                new ActivityResultContracts.RequestPermission(),
//                isGranted -> {
//                    if (isGranted) {
//                        openFilePicker();
//                    } else {
//                        Toast.makeText(this, "需要存储权限", Toast.LENGTH_SHORT).show();
//                    }
//                }
//        );


        // 头像上传
//        btnUpload.setOnClickListener(v -> openFilePicker());
        btnUpload.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                openFilePicker();
            } else {
                try {
                    filePickerLauncher.launch(Intent.getIntent(Manifest.permission.READ_EXTERNAL_STORAGE));
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // 确认按钮
        btnConfirm.setOnClickListener(v -> {
            if (validateInput()) {
                String nickname = etNickname.getText().toString();
                int gender = rgGender.getCheckedRadioButtonId() == R.id.rb_male ? 1 : 0;
                String email = etEmail.getText().toString();
                String signature = etSignature.getText().toString();
                if (listener != null) {
//                    listener.onProfileModified(nickname, gender, email, signature, ((BitmapDrawable) ivAvatar.getDrawable()).getBitmap());
                    listener.onProfileModified(nickname, gender, email, signature, selectedFile);
                }
                dismiss();
            }
        });

        // 取消按钮
        btnCancel.setOnClickListener(v -> dismiss());

        builder.setView(view)
                .setTitle("修改个人资料");

        return builder.create();
    }

    private boolean validateInput() {
        if (etNickname.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "请输入昵称", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

//    private void openImagePicker() {
////        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
////        intent.addCategory(Intent.CATEGORY_OPENABLE);
////        intent.setType("image/*");
////        startActivityForResult(intent, 1);
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("image/*"); // 限制为图片类型
//        startActivityForResult(intent, FILE_PICK_REQUEST_CODE);
//    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        filePickerLauncher.launch(intent); // 关键变化：使用 launcher
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
////        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
////            Uri uri = data.getData();
////            try {
////                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
////                ivAvatar.setImageBitmap(bitmap);
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
////        }
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == FILE_PICK_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
//            Uri uri = data.getData();
//            Bitmap bitmap = null;
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
//                ivAvatar.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//
//            selectedFile = FileUtils.getFileFromUri(requireContext(), uri); // 需要工具类将 Uri 转 File
//            if (selectedFile != null) {
//                uploadAvatar(); // 开始上传
//            }
//        }
//    }

    private void handleFile(Uri uri){
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
            ivAvatar.setImageBitmap(bitmap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        selectedFile = FileUtils.getFileFromUri(requireContext(), uri); // 需要工具类将 Uri 转 File
    }
}
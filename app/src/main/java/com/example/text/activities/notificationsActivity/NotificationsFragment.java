package com.example.text.activities.notificationsActivity;

// HomeFragment.java

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.text.R;
import com.example.text.activities.friendActivities.ApplyDetailDialogFragment;
import com.example.text.activities.friendActivities.ApplyInfoActivity;
import com.example.text.apis.ApiService;
import com.example.text.dataModel.ApplyInfoItem;
import com.example.text.dataModel.request.DealWithApplyRequest;
import com.example.text.retrofits.RetrofitClient;
import com.example.text.services.WebSocketService;
import com.example.text.utils.Store;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends Fragment {
    private MaterialButton btnNotifications,btnPassword,btnPrivacy,btnLogout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);

        btnNotifications=rootView.findViewById(R.id.btn_notifications);
        btnPassword=rootView.findViewById(R.id.btn_password);
        btnPrivacy=rootView.findViewById(R.id.btn_privacy);
        btnLogout=rootView.findViewById(R.id.btn_logout);

        btnLogout.setOnClickListener(v->showLogoutDialog());

        return rootView;
    }

    private void showLogoutDialog(){
        ExitConfirmationDialogFragment dialog = ExitConfirmationDialogFragment.newInstance();
        dialog.setOnActionClickListener(new ExitConfirmationDialogFragment.OnActionClickListener() {
            @Override
            public void onAgree() {
                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("userPrefs", MODE_PRIVATE);
                sharedPreferences.edit().clear().apply();
                Store.getInstance(requireActivity().getApplicationContext()).clear();
                requireActivity().stopService(new Intent(requireContext(), WebSocketService.class));
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }

            @Override
            public void onReject() {

            }
        });
        dialog.show(getParentFragmentManager(), "确定退出当前账号吗?");
    }
}

package com.example.text.activities.notificationsActivity;

// HomeFragment.java

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.text.R;
import com.google.android.material.button.MaterialButton;

public class NotificationsFragment extends Fragment {
    private MaterialButton btnNotifications,btnPassword,btnPrivacy,btnLogout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);

        btnNotifications=rootView.findViewById(R.id.btn_notifications);
        btnPassword=rootView.findViewById(R.id.btn_password);
        btnPrivacy=rootView.findViewById(R.id.btn_privacy);
        btnLogout=rootView.findViewById(R.id.btn_logout);

        btnLogout.setOnClickListener(v->logout());

        return rootView;
    }

    private void logout(){
        //TODO
    }
}

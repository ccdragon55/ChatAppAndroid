package com.example.text;

// MainActivity.java

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.view.MenuItem;

public class MenuTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menutest);

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        // 默认加载首页
        loadFragment(new HomeFragment());

        bottomNavigation.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    fragment = new HomeFragment();
                } else if (itemId == R.id.nav_dashboard) {
                    fragment = new DashboardFragment();
                } else if (itemId == R.id.nav_notifications) {
                    fragment = new NotificationsFragment();
                }
                return loadFragment(fragment);
            }
        });
    }

    // 加载 Fragment 的通用方法
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;

//        // 在 loadFragment 方法中优化
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        if (fragment.isAdded()) {
//            transaction.show(fragment);
//        } else {
//            transaction.add(R.id.fragment_container, fragment);
//        }
//// 隐藏其他 Fragment
//        for (Fragment f : getSupportFragmentManager().getFragments()) {
//            if (f != fragment) {
//                transaction.hide(f);
//            }
//        }
//        transaction.commit();
    }
}

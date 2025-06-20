package com.example.text.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.text.R;
import com.example.text.activities.loginActivities.LoginActivity;
import com.example.text.sip.test.SipMainActivity;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 100;
//    private Button gotoGreetingPageBtn;
//    private Button gotoMenuTestBtn;
//    private Button loginBtn;
//    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA
            }, PERMISSION_REQUEST_CODE);
        }

        //原版
        Intent intent=new Intent();
        intent.setClass(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        //sip测试版
//        Intent intent=new Intent();
//        intent.setClass(MainActivity.this, SipMainActivity.class);
//        startActivity(intent);

//        gotoGreetingPageBtn=(Button)findViewById(R.id.gotoGreetingPageBtn);
//        gotoMenuTestBtn=(Button)findViewById(R.id.gotoMenuTestBtn);
//        loginBtn=(Button)findViewById(R.id.loginBtn);
//        registerBtn=(Button)findViewById(R.id.registerBtn);


//        Toast.makeText(this, "start", Toast.LENGTH_SHORT).show();
//        Log.i(TAG,"Enter MainActivity");

//        //匿名内部类
//        loginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
//            }
//        });

//        gotoGreetingPageBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent();
////                intent.setClass(MainActivity.this, GreetingActivity.class);
//                intent.setClass(MainActivity.this, LoginActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        gotoMenuTestBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent();
////                intent.setClass(MainActivity.this, GreetingActivity.class);
//                intent.setClass(MainActivity.this, MenuTestActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        //内部类
//        loginBtn.setOnClickListener(new BtnClickListener(getString(R.string.login_success)));
//        registerBtn.setOnClickListener(new BtnClickListener(getString(R.string.register_success)));
    }

    //定义一个内部类，实现View.OnClickListener接口，并重写onClick()方法
//    class BtnClickListener implements View.OnClickListener{
//        private String message;
//
//        BtnClickListener(String message){
//            this.message=message;
//        }
//
//        @Override
//        public void onClick(View v){
//            Log.i(TAG,"MainActivity button click"+message);
//            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//        }
//    }
}
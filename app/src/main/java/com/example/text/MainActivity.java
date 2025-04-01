package com.example.text;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button gotoGreetingPageBtn;
    private Button gotoMenuTestBtn;
    private Button loginBtn;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gotoGreetingPageBtn=(Button)findViewById(R.id.gotoGreetingPageBtn);
        gotoMenuTestBtn=(Button)findViewById(R.id.gotoMenuTestBtn);
        loginBtn=(Button)findViewById(R.id.loginBtn);
        registerBtn=(Button)findViewById(R.id.registerBtn);


//        Toast.makeText(this, "start", Toast.LENGTH_SHORT).show();
//        Log.i(TAG,"Enter MainActivity");

//        //匿名内部类
//        loginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
//            }
//        });

        gotoGreetingPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
//                intent.setClass(MainActivity.this, GreetingActivity.class);
                intent.setClass(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        gotoMenuTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
//                intent.setClass(MainActivity.this, GreetingActivity.class);
                intent.setClass(MainActivity.this, MenuTestActivity.class);
                startActivity(intent);
            }
        });

        //内部类
        loginBtn.setOnClickListener(new BtnClickListener(getString(R.string.login_success)));
        registerBtn.setOnClickListener(new BtnClickListener(getString(R.string.register_success)));
    }

    //定义一个内部类，实现View.OnClickListener接口，并重写onClick()方法
    class BtnClickListener implements View.OnClickListener{
        private String message;

        BtnClickListener(String message){
            this.message=message;
        }

        @Override
        public void onClick(View v){
            Log.i(TAG,"MainActivity button click"+message);
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
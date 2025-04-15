package com.example.text;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GreetingActivity extends AppCompatActivity {
    private Button gotoLoginPageBtn;
    private EditText greetingEditText;
    private Button greetingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.greeting_main);
        gotoLoginPageBtn=(Button)findViewById(R.id.gotoLoginPageBtn);
        greetingBtn=(Button)findViewById(R.id.greetingBtn);
        greetingEditText=(EditText)findViewById(R.id.greetingEditText);

        gotoLoginPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(GreetingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        greetingBtn.setOnClickListener(new BtnClickListener(this, getString(R.string.user_greeting_message, greetingEditText.getText().toString())));
    }

    static class BtnClickListener implements View.OnClickListener{
        private Context context;
        private String message;

        BtnClickListener(Context context,String message){
            this.context=context;
            this.message=message;
        }
        @Override
        public void onClick(View view) {
            Log.i(TAG,"GreetingActivity button click"+message);
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}

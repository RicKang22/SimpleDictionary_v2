package com.android2016ncu.simpledictionary_v2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.android2016ncu.simpledictionary_v2.R;

public class Splash extends AppCompatActivity {

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    startMain();
            }
            return true;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐藏标题栏
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_aplash);
        //延迟3秒启动主界面
        handler.sendEmptyMessageDelayed(1,3000);

    }

    public void startMain(){
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}

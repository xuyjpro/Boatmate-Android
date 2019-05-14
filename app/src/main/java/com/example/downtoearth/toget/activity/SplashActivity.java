package com.example.downtoearth.toget.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.example.downtoearth.toget.R;
import com.example.downtoearth.toget.bean.Area;
import com.example.downtoearth.toget.bean.Province;
import com.example.downtoearth.toget.utils.ToolUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by DownToEarth on 2018/5/8.
 */

public class SplashActivity extends Activity {
    private RelativeLayout layout_bg;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏


        setContentView(R.layout.activity_splash);

        layout_bg=findViewById(R.id.layout_bg);
        AlphaAnimation alphaAnimation=new AlphaAnimation(0.1f,1.0f);
        alphaAnimation.setDuration(50);
        layout_bg.setAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                String token= ToolUtils.getString(SplashActivity.this,"token");
                if(TextUtils.isEmpty(token)){
                    Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                }else{
                    String username=ToolUtils.getString(SplashActivity.this,"username");
                    String password=ToolUtils.getString(SplashActivity.this,"password");
                    if(TextUtils.isEmpty(username)||TextUtils.isEmpty(password)){
                        Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
                        startActivity(intent);

                    }else{
                        JMessageClient.login(username, password, new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {
                            }
                        });
                        Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                        startActivity(intent);
                    }

                }
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

}

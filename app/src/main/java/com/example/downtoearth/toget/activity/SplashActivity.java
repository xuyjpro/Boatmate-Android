package com.example.downtoearth.toget.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.example.downtoearth.toget.R;
import com.example.downtoearth.toget.utils.ToolUtils;


/**
 * Created by DownToEarth on 2018/5/8.
 */

public class SplashActivity extends BaseActivity {
    private RelativeLayout layout_bg;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏


        setContentView(R.layout.activity_splash);

        ToolUtils.setContext(this);
        layout_bg=findViewById(R.id.layout_bg);
        AlphaAnimation alphaAnimation=new AlphaAnimation(0.1f,1.0f);
        alphaAnimation.setDuration(1000);
        layout_bg.setAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                String token= ToolUtils.getString("token");
                if(token==null||token.isEmpty()){
                    Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                }else{

                    Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                    Log.e(TAG,token);
                    startActivity(intent);
                }
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}

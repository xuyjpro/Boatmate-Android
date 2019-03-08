package com.example.downtoearth.toget.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.downtoearth.toget.utils.ActivityCollector;

/**
 * Created by DownToEarth on 2018/5/8.
 */

public class BaseActivity extends AppCompatActivity {
    public String TAG;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG=getClass().getSimpleName();

        ActivityCollector.addActivity(this);
    }
    public void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
    public void log(String msg){
        Log.e(TAG,msg);
    }
}

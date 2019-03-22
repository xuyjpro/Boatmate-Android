package com.example.downtoearth.toget.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.MessageEvent;

public abstract class BaseFragment extends Fragment {

    public String TAG;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TAG=getClass().getSimpleName();
        //订阅接收消息,子类只要重写onEvent就能收到消息
        JMessageClient.registerEventReceiver(this);
        View view=initView();
        initData();
        return view;
    }

    public abstract View initView();
    public abstract void initData();
    public void showToast(String msg){
        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
    }
    public void log(String msg){
        Log.e(TAG,msg);
    }

    @Override
    public void onDestroy() {
        JMessageClient.unRegisterEventReceiver(this);

        super.onDestroy();
    }
    public void onEvent(MessageEvent event) {

    }
}

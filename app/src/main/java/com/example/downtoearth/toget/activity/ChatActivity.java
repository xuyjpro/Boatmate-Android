package com.example.downtoearth.toget.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.downtoearth.toget.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import cn.jpush.im.android.api.JMessageClient;

public class ChatActivity extends BaseActivity implements View.OnClickListener {


    private EditText et_text;
    private Button btn_send;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();

        initEvent();

    }
    public void initView(){
        et_text=findViewById(R.id.et_text);
        btn_send=findViewById(R.id.btn_send);
    }
    public void initEvent(){
        btn_send.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_send:
                if(TextUtils.isEmpty(et_text.getText())){
                    showToast("消息不能为空");
                }else{
                    JMessageClient.createSingleTextMessage(getIntent().getStringExtra("username"), et_text.getText().toString());
                }
                break;
        }
    }
}

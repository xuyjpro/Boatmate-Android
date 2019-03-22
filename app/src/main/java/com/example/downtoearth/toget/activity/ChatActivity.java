package com.example.downtoearth.toget.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.downtoearth.toget.R;
import com.example.downtoearth.toget.adapter.MessageAdapter;
import com.example.downtoearth.toget.utils.KeybordS;
import com.example.downtoearth.toget.utils.ToolUtils;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;

public class ChatActivity extends BaseActivity implements View.OnClickListener {


    private EditText et_text;
    private TextView tv_nickname;
    private Button btn_send;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private Conversation mConv;
    private String mTargetId;
    private UserInfo mMyInfo;

    private MessageAdapter mAdapter;
    private List mDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        JMessageClient.registerEventReceiver(this);


        initView();
        initData();
        initEvent();

    }

    public void initView() {

        recyclerView = findViewById(R.id.recycler_view);

        et_text = findViewById(R.id.et_text);
        btn_send = findViewById(R.id.btn_send);
        tv_nickname=findViewById(R.id.tv_nickname);
    }

    public void initData() {

        mTargetId = getIntent().getStringExtra("username");

        mDataList = new ArrayList();
        mConv = JMessageClient.getSingleConversation(mTargetId, ToolUtils.getAppKey());
        if (mConv == null) {
            mConv = Conversation.createSingleConversation(mTargetId, ToolUtils.getAppKey());
        }
        mDataList = mConv.getAllMessage();
        mAdapter = new MessageAdapter(mDataList);

        LinearLayoutManager linearLayoutManager;
        recyclerView.setLayoutManager(linearLayoutManager=new LinearLayoutManager(this));

        recyclerView.setAdapter(mAdapter);

        if(mDataList.size()!=0){
            recyclerView.scrollToPosition(mDataList.size()-1);
        }
        mMyInfo = JMessageClient.getMyInfo();
        JMessageClient.getUserInfo(mTargetId, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                tv_nickname.setText(userInfo.getNickname());
            }
        });
    }

    public void initEvent() {

        btn_send.setOnClickListener(this);


        findViewById(R.id.layout_back).setOnClickListener(this);
    }

    public void postMessage() {

        if (TextUtils.isEmpty(et_text.getText())) {
            showToast("消息不能为空");

        } else {
            Message message = JMessageClient.createSingleTextMessage(mTargetId, ToolUtils.getAppKey(), et_text.getText().toString());
            JMessageClient.sendMessage(message);


            mDataList.add(message);
            mAdapter.notifyItemInserted(mDataList.size()-1);

            if (KeybordS.isSoftInputShow(this)) {
                KeybordS.closeKeybord(et_text, this);
            }
            recyclerView.smoothScrollToPosition(mDataList.size() - 1);
            et_text.setText("");

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                postMessage();
                break;
            case R.id.layout_back:
                onBackPressed();
                break;
        }
    }

    /**
     * 接收在线消息
     **/
    public void onEvent(MessageEvent event) {
        //showToast("hello");
        //获取事件发生的会话对象
        final Message message = event.getMessage();//获取此次离线期间会话收到的新消息列表

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (message.getTargetType() == ConversationType.single) {
                    UserInfo userInfo = (UserInfo) message.getTargetInfo();
                    String targetId = userInfo.getUserName();

                    TextContent textContent= (TextContent) message.getContent();

                    if (targetId.equals(mTargetId)) {

                        mDataList.add(message);
                        mAdapter.notifyItemInserted(mDataList.size()-1);

                        recyclerView.smoothScrollToPosition(mDataList.size()-1);
                        mConv.setUnReadMessageCnt(0);
                    }
                }
            }
        });
    }

    /**
     * 接收离线消息。
     * 类似MessageEvent事件的接收，上层在需要的地方增加OfflineMessageEvent事件的接收
     * 即可实现离线消息的接收。
     **/
    public void onEvent(OfflineMessageEvent event) {
        //获取事件发生的会话对象
        Conversation conv = event.getConversation();
        UserInfo userInfo = (UserInfo) conv.getTargetInfo();
        String targetId = userInfo.getUserName();
        String appKey = userInfo.getAppKey();
        if (targetId.equals(mTargetId)) {
            List<Message> singleOfflineMsgList = event.getOfflineMessageList();
            if (singleOfflineMsgList != null && singleOfflineMsgList.size() > 0) {
                mDataList.addAll(singleOfflineMsgList);
                mAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(mDataList.size()-1);
            }
        }
    }

    @Override
    protected void onDestroy() {
                JMessageClient.unRegisterEventReceiver(this);

        super.onDestroy();

    }
}

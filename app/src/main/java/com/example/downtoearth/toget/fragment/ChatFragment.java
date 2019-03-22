package com.example.downtoearth.toget.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.downtoearth.toget.R;
import com.example.downtoearth.toget.activity.ChatActivity;
import com.example.downtoearth.toget.activity.DynamicDetailActivity;
import com.example.downtoearth.toget.activity.PublishCommentActivity;
import com.example.downtoearth.toget.adapter.ChatAdapter;
import com.example.downtoearth.toget.adapter.DynamicAdapter;
import com.example.downtoearth.toget.bean.DynamicListBean;
import com.example.downtoearth.toget.bean.UserInfo;
import com.example.downtoearth.toget.utils.HttpUtils;
import com.example.downtoearth.toget.utils.ToolUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import okhttp3.Call;
import okhttp3.Response;

public class ChatFragment extends BaseFragment {
    private RecyclerView recyclerView;

    private ChatAdapter mAdapter;
    private List mDynamicList;
    private RefreshLayout smartRefreshLayout;

    private OnGetUnReadCount getUnReadCount;
    @Override
    public View initView() {
        View view=LayoutInflater.from(getContext()).inflate(R.layout.fragment_chat,null);
        recyclerView = view.findViewById(R.id.recycler_view);
        smartRefreshLayout=view.findViewById(R.id.smart_refresh);
        JMessageClient.registerEventReceiver(this);

        return view;
    }

    @Override
    public void initData() {
        mDynamicList = new ArrayList();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter = new ChatAdapter(mDynamicList));
        mAdapter.setOnItemClickListener(new ChatAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) { Conversation conversation= (Conversation) mDynamicList.get(position);
                Intent intent=new Intent(getContext(),ChatActivity.class);
                intent.putExtra("username",((cn.jpush.im.android.api.model.UserInfo)conversation.getTargetInfo()).getUserName());

                startActivityForResult(intent,1000);


                mAdapter.setUnReadCount(mAdapter.getUnReadCount()-conversation.getUnReadMsgCnt());

                conversation.setUnReadMessageCnt(0);
                mAdapter.notifyItemChanged(position);

            }

            @Override
            public void onHeadClick(int position) {

            }
        });
        smartRefreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {

            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshConversations();
                refreshlayout.finishRefresh(1000);
            }
        });
//        refreshConversations();

        smartRefreshLayout.autoRefresh();
    }
    public void refreshConversations(){

        mDynamicList=JMessageClient.getConversationList();
        if(mDynamicList!=null&&mDynamicList.size()!=0){
//            mAdapter=new ChatAdapter(mDynamicList);


            mAdapter.setDataList(mDynamicList, new OnGetUnReadCount() {
                @Override
                public void getUnReadCount(int unReadCount) {
                    if(getUnReadCount!=null){
                        showToast(mAdapter.getUnReadCount()+"");
                        getUnReadCount.getUnReadCount(mAdapter.getUnReadCount());
                    }
                }
            });

//            mAdapter.notifyItemRangeChanged(0,mDynamicList.size());
        }else{

        }

    }

    @Override
    public void onEvent(MessageEvent event) {
        super.onEvent(event);

        handler.sendEmptyMessage(0);

    }

    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case 0:
                    refreshConversations();
                    break;

            }
            return false;
        }
    });
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
            refreshConversations();
        }
    }


    public void setGetUnReadCount(OnGetUnReadCount getUnReadCount) {
        this.getUnReadCount = getUnReadCount;
    }

    public  interface  OnGetUnReadCount{
        void getUnReadCount(int unReadCount);
    }
    @Override
    public void onDestroy() {
        JMessageClient.unRegisterEventReceiver(this);

        super.onDestroy();

    }
}

package com.example.downtoearth.toget.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.downtoearth.toget.R;
import com.example.downtoearth.toget.activity.DynamicDetailActivity;
import com.example.downtoearth.toget.activity.PublishCommentActivity;
import com.example.downtoearth.toget.adapter.ChatAdapter;
import com.example.downtoearth.toget.adapter.DynamicAdapter;
import com.example.downtoearth.toget.bean.DynamicListBean;
import com.example.downtoearth.toget.utils.HttpUtils;
import com.example.downtoearth.toget.utils.ToolUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;
import okhttp3.Call;
import okhttp3.Response;

public class ChatFragment extends BaseFragment {
    private RecyclerView recyclerView;

    private ChatAdapter mAdapter;
    private List mDynamicList;
    private RefreshLayout smartRefreshLayout;

    private int mNextPage = 1;
    @Override
    public View initView() {
        View view=LayoutInflater.from(getContext()).inflate(R.layout.fragment_chat,null);
        recyclerView = view.findViewById(R.id.recycler_view);
        smartRefreshLayout=view.findViewById(R.id.smart_refresh);
        return view;
    }

    @Override
    public void initData() {
        mDynamicList = new ArrayList();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter = new ChatAdapter(mDynamicList));
        smartRefreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
             //   getNewData(false);
                refreshlayout.finishLoadmore(1000);
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
              //  getNewData(true);
                refreshlayout.finishRefresh(1000);
            }
        });

        mAdapter.setOnItemClickListener(new ChatAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {


            }

            @Override
            public void onHeadClick(int position) {

            }
        });
        refreshConversations();
        //smartRefreshLayout.autoRefresh();
    }
    public void refreshConversations(){

        mDynamicList=JMessageClient.getConversationList();
        mAdapter.notifyDataSetChanged();

    }

}

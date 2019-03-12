package com.example.downtoearth.toget.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.example.downtoearth.toget.R;
import com.example.downtoearth.toget.activity.SchoolHelpDetailActivity;
import com.example.downtoearth.toget.adapter.StuffLossAdapter;
import com.example.downtoearth.toget.bean.SchoolHelp;
import com.example.downtoearth.toget.bean.Stuff;
import com.example.downtoearth.toget.utils.HttpUtils;
import com.example.downtoearth.toget.utils.ToolUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.example.downtoearth.toget.adapter.StuffLossAdapter.onItemClickListener;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class StuffLossFragment extends BaseFragment {
    private RecyclerView recyclerView;

    private StuffLossAdapter mAdapter;
    private List mDataList;
    private RefreshLayout smartRefreshLayout;

    private int mNextPage = 1;
    public static StuffLossFragment newInstance(int category) {
        StuffLossFragment fragment = new StuffLossFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("category", category);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public View initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_dynamic, null);
        recyclerView = view.findViewById(R.id.recycler_view);
        smartRefreshLayout = view.findViewById(R.id.smart_refresh);
        return view;
    }

    @Override
    public void initData() {
        mDataList = new ArrayList();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter = new StuffLossAdapter(mDataList));
        mAdapter.setOnItemListener(new onItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onMoreClick(int position) {
                showToast("暂未开发");
            }
        });
        smartRefreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getNewData(false);
                refreshlayout.finishLoadmore(1000);
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getNewData(true);
                refreshlayout.finishRefresh(1000);
            }
        });
        smartRefreshLayout.autoRefresh();
    }
    public void getNewData(final boolean isRefresh) {
        if (isRefresh) {
            mNextPage = 1;
        } else {
            mNextPage++;
        }

        OkGo.post(HttpUtils.GET_STUFFS)
                .tag(this)
                .isMultipart(true)
                .params("currentPage", mNextPage)
                .params("pageSize", 20)
                .params("category",getArguments().getInt("category"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        log(s);
                        parseData(s,isRefresh);
                    }
                });
    }
    public void parseData(String s,boolean isRefresh){
        Stuff stuff=new Gson().fromJson(s,Stuff.class);
        if(stuff.getData()!=null&&stuff.getData().size()!=0){
            if(isRefresh){
                mDataList.clear();
            }
            mDataList.addAll(stuff.getData());
            mAdapter.notifyDataSetChanged();
        }else{
            showToast("暂无更多数据");
        }
    }

}

package com.example.downtoearth.toget.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.example.downtoearth.toget.R;
import com.example.downtoearth.toget.activity.StuffDetailActivity;
import com.example.downtoearth.toget.adapter.CollegeMarketAdapter;
import com.example.downtoearth.toget.adapter.CollegeMarketAdapter2;
import com.example.downtoearth.toget.bean.Stuff;
import com.example.downtoearth.toget.utils.HttpUtils;
import com.example.downtoearth.toget.utils.ToolUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import okhttp3.Call;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class CollegeMarketFragment extends BaseFragment {
    private RecyclerView recyclerView;

    private CollegeMarketAdapter2 mAdapter;

    private RefreshLayout smartRefreshLayout;

    private int mNextPage = 1;
    public static CollegeMarketFragment newInstance(int category, boolean isMy) {
        CollegeMarketFragment fragment = new CollegeMarketFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("category", category);
        bundle.putBoolean("isMy", isMy);
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

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(mAdapter = new CollegeMarketAdapter2());
        mAdapter.setOnItemListener(new CollegeMarketAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Intent intent=new Intent(getContext(),StuffDetailActivity.class);
                intent.putExtra("id",((Stuff.DataBean)mAdapter.getList().get(position)).getStuff().getId());
                intent.putExtra("position",position);

                intent.putExtra("isMarket",true);
                startActivityForResult(intent,1000);
            }

            @Override
            public void onMoreClick(int position) {
                showToast("暂未开发");
            }

            @Override
            public void onPictureClick(int position) {


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

        HttpParams httpParams=new HttpParams();
        httpParams.put("currentPage",mNextPage);
        httpParams.put("category",getArguments().getInt("category"));

        if(getArguments().getBoolean("isMy")){
            httpParams.put("token",ToolUtils.getString(getContext(),"token"));
        }
        OkGo.post(HttpUtils.GET_STUFFS)
                .tag(this)
                .isMultipart(true)
                .params("pageSize",20)
                .params(httpParams)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        parseData(s,isRefresh);
                    }
                });
    }
    public void parseData(String s,boolean isRefresh){
        Stuff stuff=new Gson().fromJson(s,Stuff.class);
        if(stuff.getData()!=null&&stuff.getData().size()!=0){
            mAdapter.addAll(stuff.getData(),isRefresh);
        }else{
            showToast("暂无更多数据");
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
            if(resultCode==RESULT_OK&&data!=null){
                int position=data.getIntExtra("position",0);
                mAdapter.remove(position);
            }
        }
    }
}

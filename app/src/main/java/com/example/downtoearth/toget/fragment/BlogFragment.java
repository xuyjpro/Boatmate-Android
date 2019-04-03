package com.example.downtoearth.toget.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.downtoearth.toget.R;
import com.example.downtoearth.toget.activity.BlogDetailActivity;
import com.example.downtoearth.toget.adapter.BlogAdapter;
import com.example.downtoearth.toget.bean.Blog;
import com.example.downtoearth.toget.impl.OnItemClickListener;
import com.example.downtoearth.toget.utils.HttpUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

public class BlogFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private SmartRefreshLayout refreshLayout;
    private ViewGroup layout_nothing;
    private BlogAdapter mAdapter;

    private int currentPage=1;
    @Override
    public View initView() {
        View view=LayoutInflater.from(getContext()).inflate(R.layout.fragment_chat,null);
        recyclerView=view.findViewById(R.id.recycler_view);
        refreshLayout=view.findViewById(R.id.smart_refresh);
        layout_nothing=view.findViewById(R.id.layout_nothing);

        TextView tv_title=view.findViewById(R.id.tv_title);
        tv_title.setText("我的博客");
        return view;
    }

    @Override
    public void initData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter=new BlogAdapter());
        mAdapter.setListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Blog.DataBean dataBean= (Blog.DataBean) mAdapter.getList().get(position);
                if(dataBean.getId()==1){
                    showToast("该条博客需要权限，禁止访问");
                }else{
                    Intent intent=new Intent(getContext(),BlogDetailActivity.class);
                    intent.putExtra("url",dataBean.getUrl());
                    startActivity(intent);
                }

            }
        });
        refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getBlogs(false);
                refreshlayout.finishLoadmore(1000);
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getBlogs(true);
                refreshlayout.finishRefresh(1000);

            }

        });
        refreshLayout.autoRefresh();

    }
    public void getBlogs(final boolean isRefresh){
        if(isRefresh){
            currentPage=1;
        }else{
            currentPage++;
        }
        OkGo.post(HttpUtils.GET_BLOGS)
                .tag(this)
                .isMultipart(true)
                .params("currentPage",currentPage)
                .params("pageSize",10)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        log(s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            if(jsonObject.getInt("code")==200){
                                parseResult(s,isRefresh);
                            }else{
                                showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    public void parseResult(String s,boolean isRefresh){

        Blog blog=new Gson().fromJson(s,Blog.class);
        if(blog.getData()==null&&blog.getData().size()==0){
            if(isRefresh){
                layout_nothing.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }else{
                showToast("暂无更多数据");
                currentPage--;
            }
        }else{
            if(isRefresh){
                layout_nothing.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
            mAdapter.addAll(blog.getData(),isRefresh);
        }
    }
}

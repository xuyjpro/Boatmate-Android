package com.example.downtoearth.toget.fragment;

import android.content.Intent;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.downtoearth.toget.R;
import com.example.downtoearth.toget.activity.ChatActivity;
import com.example.downtoearth.toget.activity.DynamicDetailActivity;
import com.example.downtoearth.toget.activity.PublishCommentActivity;
import com.example.downtoearth.toget.adapter.DynamicAdapter;
import com.example.downtoearth.toget.bean.DynamicListBean;
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
import cn.jpush.im.android.api.model.Conversation;
import okhttp3.Call;
import okhttp3.Response;

public class DynamicFragment extends BaseFragment {
    private RecyclerView recyclerView;

    private DynamicAdapter mAdapter;
    private List mDynamicList;
    private RefreshLayout smartRefreshLayout;

    private int mNextPage = 1;

    public static DynamicFragment newInstance(int category) {
        DynamicFragment dynamicFragment = new DynamicFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("category", category);
        dynamicFragment.setArguments(bundle);
        return dynamicFragment;
    }

    @Override
    public View initView() {

        View view=LayoutInflater.from(getContext()).inflate(R.layout.fragment_dynamic,null);
        recyclerView = view.findViewById(R.id.recycler_view);
        smartRefreshLayout=view.findViewById(R.id.smart_refresh);

        return view;
    }

    @Override
    public void initData() {

        mDynamicList = new ArrayList();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter = new DynamicAdapter(mDynamicList));
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
        mAdapter.setOnItemClikcListener(new DynamicAdapter.OnItemClickListener() {
            @Override
            public void onLikeClick(int position) {

                postLike(position);

            }

            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getContext(), DynamicDetailActivity.class);
                intent.putExtra("id", ((DynamicListBean.DataBean) mDynamicList.get(position)).getId());
                intent.putExtra("position",position);
                startActivityForResult(intent,1000);
            }

            @Override
            public void onCommentClick(int position) {

                DynamicListBean.DataBean dataBean= (DynamicListBean.DataBean) mDynamicList.get(position);

                PublishCommentActivity.startActivityForComment(getActivity(),dataBean.getId(),dataBean.getNickname(),1000);
            }

            @Override
            public void onHeadClick(int position) {
                if(position==0){
                    Conversation.createSingleConversation("15259900001");

                }else if(position==1){
                    Conversation.createSingleConversation("15259900002");
                    Intent intent=new Intent(getContext(),ChatActivity.class);
                    intent.putExtra("username","15259900002");
                    startActivity(intent);
                }
            }
        });

        smartRefreshLayout.autoRefresh();
    }


    public void autoRefresh(){
        smartRefreshLayout.autoRefresh();

    }
    public void getNewData(final boolean isRefresh) {
        if (isRefresh) {
            mNextPage = 1;
        } else {
            mNextPage++;
        }
        OkGo.post(HttpUtils.DYNAMIC_LIST)
                .tag(this)
                .params("token", ToolUtils.getString("token"))
                .params("currentPage", mNextPage)
                .params("category", getArguments().getInt("category"))
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        ToolUtils.doHttpError(getContext());
                    }
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("result", s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getInt("code") == 200) {
                                parseData(s, isRefresh);
                            } else {
                                Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

    }

    public void postLike(final int position) {
        final DynamicListBean.DataBean dataBean = (DynamicListBean.DataBean) mDynamicList.get(position);
        OkGo.post(HttpUtils.LIKE_CLICK)
                .tag(this)
                .isMultipart(true)
                .params("token", ToolUtils.getString("token"))
                .params("id", dataBean.getId())
                .params("isLike", dataBean.isLike() ? 1 : 0)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getInt("code") == 200) {
                                showToast(dataBean.isLike()?"取消点赞成功":"点赞成功");

                                dataBean.setLike(!dataBean.isLike());
                                if(dataBean.isLike()){
                                    dataBean.setAwesome(dataBean.getAwesome()+1);
                                }else{
                                    dataBean.setAwesome(dataBean.getAwesome()-1);

                                }
                            } else {
                                showToast(jsonObject.getString("message"));
                            }
                            mAdapter.notifyItemChanged(position);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    public void parseData(String s, boolean isRefresh) {
        DynamicListBean dlb = new Gson().fromJson(s, DynamicListBean.class);
        if (dlb.getData() != null&&dlb.getData().size()!=0) {
            if (isRefresh) {
                mDynamicList.clear();
            }
            mDynamicList.addAll(dlb.getData());

            mAdapter.notifyDataSetChanged();
        } else {
            mNextPage--;
            showToast("没有更多数据了");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000) {  //动态详情
            if(data!=null){
                int position=data.getIntExtra("position",0);
                if (resultCode == 2000) {   //修改
                    DynamicListBean.DataBean dataBean= (DynamicListBean.DataBean) mDynamicList.get(position);
                    dataBean.setAwesome(data.getIntExtra("awesome",0));
                    dataBean.setComment(data.getIntExtra("comment",0));
                    dataBean.setLike(data.getBooleanExtra("isLike",true));
                    mAdapter.notifyItemChanged(position);
                } else if (resultCode == 2001) { //删除
                    mDynamicList.remove(position);
                    mAdapter.notifyItemRemoved(position);
                }
            }

        }
    }
}

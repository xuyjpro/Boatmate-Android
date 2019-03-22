package com.example.downtoearth.toget.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.downtoearth.toget.R;
import com.example.downtoearth.toget.adapter.DynamicAdapter;
import com.example.downtoearth.toget.bean.DynamicListBean;
import com.example.downtoearth.toget.bean.UserInfo;
import com.example.downtoearth.toget.utils.HttpUtils;
import com.example.downtoearth.toget.utils.SecretUtils;
import com.example.downtoearth.toget.utils.ToolUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;
import okhttp3.Response;

public class MainPagerActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private TextView tv_nickname;
    private TextView tv_phone;
    private TextView tv_gender;
    private TextView tv_birthday;
    private TextView tv_heart_word;
    private ImageView iv_picture;
    private TextView tv_chat;
    private TextView tv_report;
    private int uid;
    private int mNextPage=1;
    private List mDataList;
    private DynamicAdapter mAdapter;

    private PromptDialog promptDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pager);
        initView();
        initData();
    }

    public void initView() {
        tv_birthday = findViewById(R.id.tv_birthday);
        tv_nickname = findViewById(R.id.tv_nickname);
        tv_phone = findViewById(R.id.tv_phone);
        tv_gender = findViewById(R.id.tv_gender);
        tv_heart_word = findViewById(R.id.tv_heart_word);
        tv_chat = findViewById(R.id.tv_chat);
        tv_report = findViewById(R.id.tv_report);

        iv_picture = findViewById(R.id.iv_picture);

        recyclerView = findViewById(R.id.recycler_view);

        promptDialog=new PromptDialog(this);
        promptDialog.showLoading("加载中");
    }

    public void initData() {
        if(getIntent().getIntExtra("id",0)!=0){
            uid=getIntent().getIntExtra("id",0);

            if(uid==ToolUtils.getInt(this,"uid")){
                tv_chat.setVisibility(View.GONE);
            }else{
                tv_report.setVisibility(View.GONE);
                tv_chat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(MainPagerActivity.this,ChatActivity.class);
                        intent.putExtra("username",tv_phone.getText().toString());
                        intent.putExtra("nickname",tv_nickname.getText().toString());
                        startActivity(intent);
                    }
                });
            }
        }else{

        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle(getIntent().getStringExtra("nickname"));
//        collapsingToolbar.setext
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mDataList=new ArrayList();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter=new DynamicAdapter(mDataList));
        mAdapter.setOnItemClikcListener(new DynamicAdapter.OnItemClickListener() {
            @Override
            public void onLikeClick(int position) {

                postLike(position);

            }

            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MainPagerActivity.this, DynamicDetailActivity.class);
                intent.putExtra("id", ((DynamicListBean.DataBean) mDataList.get(position)).getId());
                intent.putExtra("position",position);
                startActivityForResult(intent,1000);
            }

            @Override
            public void onCommentClick(int position) {

                DynamicListBean.DataBean dataBean= (DynamicListBean.DataBean) mDataList.get(position);

                PublishCommentActivity.startActivityForComment(MainPagerActivity.this,dataBean.getId(),dataBean.getNickname(),1000);
            }

            @Override
            public void onHeadClick(int position) {
                DynamicListBean.DataBean dataBean= (DynamicListBean.DataBean) mDataList.get(position);
                Intent intent=new Intent(MainPagerActivity.this,MainPagerActivity.class);
                intent.putExtra("id",dataBean.getUid());
                intent.putExtra("nickname",dataBean.getNickname());
                startActivity(intent);
            }
        });

        recyclerView.setNestedScrollingEnabled(false);
        SmartRefreshLayout refreshLayout=findViewById(R.id.smart_refresh);
        refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mNextPage++;
                getNewData(false);
                refreshlayout.finishLoadmore(1000);
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

            }
        });

        getUserInfo();
        getNewData(false);
    }

    public void getUserInfo() {
        OkGo.post(HttpUtils.GET_USER_INFO)
                .tag(this)
                .isMultipart(true)
                .params("token", uid==0?ToolUtils.getString(this,"token"):SecretUtils.encode(uid+""))
                .headers("Content-Type", "application/json")
                .execute(new StringCallback() {

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                          Log.e(TAG, s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getInt("code") == 200) {
                                updateUI(jsonObject.getString("data"));
                                promptDialog.showSuccess("加载成功");

                            }else{
                                promptDialog.dismiss();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }
    public void getNewData(final boolean isRefresh) {
        if (isRefresh) {
            mNextPage = 1;
        }
        HttpParams httpParams=new HttpParams();
        httpParams.put("token",ToolUtils.getString(this,"token"));
        httpParams.put("currentPage",mNextPage);
        httpParams.put("category",0);

        httpParams.put("to_uid",uid);
        httpParams.put("isAll",1);
        OkGo.post(HttpUtils.DYNAMIC_LIST)
                .tag(this)
                .isMultipart(true)
                .params(httpParams)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        ToolUtils.doHttpError(MainPagerActivity.this);
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e(TAG, s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getInt("code") == 200) {
                                parseData(s, isRefresh);
                            } else {
                                Toast.makeText(MainPagerActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
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
                mDataList.clear();
            }
            mDataList.addAll(dlb.getData());
            showToast(mDataList.size()+"");
            mAdapter.notifyDataSetChanged();
        } else {
            mNextPage--;
            showToast("没有更多数据了");
        }
    }
    public void postLike(final int position) {
        final DynamicListBean.DataBean dataBean = (DynamicListBean.DataBean) mDataList.get(position);
        OkGo.post(HttpUtils.LIKE_CLICK)
                .tag(this)
                .isMultipart(true)
                .params("token", ToolUtils.getString(this,"token"))
                .params("id", dataBean.getId())
                .params("isLike", dataBean.isLike() ? 1 : 0)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getInt("code") == 200) {
                                showToast(dataBean.isLike() ? "取消点赞成功" : "点赞成功");

                                dataBean.setLike(!dataBean.isLike());
                                if (dataBean.isLike()) {
                                    dataBean.setAwesome(dataBean.getAwesome() + 1);
                                } else {
                                    dataBean.setAwesome(dataBean.getAwesome() - 1);

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
    public void updateUI(String dataStr) {
        UserInfo.DataBean.UserInfoBean uib = (new Gson()).fromJson(dataStr, UserInfo.DataBean.UserInfoBean.class);
        if (uib.getHeadPic() != null) {
            Glide.with(this).load(HttpUtils.DOWNLOAD_URL + uib.getHeadPic()).into(iv_picture);
        }else{
            showToast("pic null");
        }
        tv_nickname.setText(uib.getNickname());
        tv_birthday.setText(uib.getBirthday());
        tv_gender.setText(uib.isGender()?"男":"女");
        tv_heart_word.setText(uib.getHeartWord());
        tv_phone.setText(uib.getPhone());

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000) {  //动态详情
            if(data!=null){
                int position=data.getIntExtra("position",0);
                if (resultCode == 2000) {   //修改
                    DynamicListBean.DataBean dataBean= (DynamicListBean.DataBean) mDataList.get(position);
                    dataBean.setAwesome(data.getIntExtra("awesome",0));
                    dataBean.setComment(data.getIntExtra("comment",0));
                    dataBean.setLike(data.getBooleanExtra("isLike",true));
                    mAdapter.notifyItemChanged(position);
                } else if (resultCode == 2001) { //删除
                    mDataList.remove(position);
                    mAdapter.notifyItemRemoved(position);
                }
            }

        }
    }
}

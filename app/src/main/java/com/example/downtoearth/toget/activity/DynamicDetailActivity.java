package com.example.downtoearth.toget.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.downtoearth.toget.MainActivity;
import com.example.downtoearth.toget.R;
import com.example.downtoearth.toget.adapter.CommentAdapter;
import com.example.downtoearth.toget.bean.Comment;
import com.example.downtoearth.toget.bean.DynamicListBean;
import com.example.downtoearth.toget.utils.HttpUtils;
import com.example.downtoearth.toget.utils.ToolUtils;
import com.example.downtoearth.toget.view.CustomDialog;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.mylhyl.circledialog.CircleDialog;
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

public class DynamicDetailActivity extends BaseActivity implements View.OnClickListener {
    private RecyclerView rv_comment;
    private SmartRefreshLayout refreshLayout;

    private ImageView iv_head;
    private TextView tv_name;
    private TextView tv_content;
    private TextView tv_time;
    private TextView tv_comment;
    private TextView tv_like1;
    private TextView tv_comment1;

    private ViewGroup layout_comment;

    private CommentAdapter mAdapter;
    private List mDataList;
    private int mNextPage=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_detail);

        initView();
        initData();
        initClickEvent();

    }
    public void initView(){
        rv_comment=findViewById(R.id.rv_comment);
        refreshLayout=findViewById(R.id.smart_refresh);

        iv_head=findViewById(R.id.iv_head);
        tv_name=findViewById(R.id.tv_name);
        tv_content=findViewById(R.id.tv_content);
        tv_time=findViewById(R.id.tv_time);
        tv_comment=findViewById(R.id.tv_comment);
        tv_comment1=findViewById(R.id.tv_commment1);

        tv_like1=findViewById(R.id.tv_like1);
        layout_comment=findViewById(R.id.layout_comment);

    }
    public void initData(){
        rv_comment.setLayoutManager(new LinearLayoutManager(this));
        mDataList=new ArrayList();
        rv_comment.setAdapter(mAdapter=new CommentAdapter(mDataList));
        refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mNextPage++;
                getCommentList(false);
                refreshlayout.finishLoadmore(1000);

            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mNextPage=1;
                getDetail();
                getCommentList(true);
                refreshlayout.finishRefresh(1000);
            }
        });
        mAdapter.setOnItemClikcListener(new CommentAdapter.OnItemClickListener() {
            @Override
            public void onLikeClick(int position) {

            }

            @Override
            public void onItemClick(int position) {
                CustomDialog customDialog=new CustomDialog(DynamicDetailActivity.this);
                customDialog.show();
            }

            @Override
            public void onCommentClick(int position) {

            }

            @Override
            public void onDetailClick(int position) {
                Comment.DataBean dataBean= (Comment.DataBean) mDataList.get(position);

                Intent intent= new Intent(DynamicDetailActivity.this,CommentDetailActivity.class);
                intent.putExtra("id",dataBean.getId());
                startActivity(intent);
            }
        });
        getDetail();
        getCommentList(true);
    }

    public void initClickEvent(){
        layout_comment.setOnClickListener(this);
        ViewGroup layout_delete=findViewById(R.id.layout_delete);
        layout_delete.setOnClickListener(this);
    }
    public void getDetail(){
        OkGo.post(HttpUtils.DYNAMIC_DETAIL)
                .tag(this)
                .isMultipart(true)
                .params("token",ToolUtils.getString("token"))
                .params("id",getIntent().getIntExtra("id",0))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e(TAG,s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            if(jsonObject.getInt("code")==200){
                              parseDetail(jsonObject.getString("data"));
                            }else{
                                showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    public void getCommentList(final boolean isRefresh){
        OkGo.post(HttpUtils.GET_COMMENTS)
                .tag(this)
                .isMultipart(true)
                .params("token",ToolUtils.getString("token"))
                .params("curentPage",mNextPage)
                .params("parent_id",getIntent().getIntExtra("id",0))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e(TAG,s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            if(jsonObject.getInt("code")==200){
                                parseData(s,isRefresh);
                            }else{
                                showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });

    }
    public void postDelete(){
        OkGo.post(HttpUtils.DYNAMIC_DELETE)
                .tag(this)
                .isMultipart(true)
                .params("token",ToolUtils.getString("token"))
                .params("id",getIntent().getIntExtra("id",0))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            if(jsonObject.getInt("code")==200){
                                showToast("删除成功");
                                finish();
                            }else{
                                showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    public void parseDetail(String s){
        DynamicListBean.DataBean dataBean=new Gson().fromJson(s,DynamicListBean.DataBean.class);
        tv_name.setText(dataBean.getNickname());
        tv_content.setText(dataBean.getContent());
        tv_time.setText(ToolUtils.getTime(dataBean.getTime()));
        Glide.with(this)
                .load(HttpUtils.DOWNLOAD_URL+dataBean.getHeadPic())
                .into(iv_head);
        tv_comment.setText(dataBean.getComment()+"");
        tv_comment1.setText(dataBean.getComment()+"");

        tv_like1.setText(dataBean.getAwesome()+"");
    }
    public void parseData(String s,boolean isRefresh){
        Comment comment=new Gson().fromJson(s,Comment.class);
        if(comment.getData()!=null){
            if(isRefresh){
                mDataList.clear();
            }
            mDataList.addAll(comment.getData());
            mAdapter.notifyDataSetChanged();
        }else {
            mNextPage--;

            showToast("没有更多数据了");
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layout_comment:

                PublishCommentActivity.startActivityForComment(this,getIntent().getIntExtra("id",0),tv_name.getText().toString(),1000);
                break;
            case R.id.layout_delete:

                new CircleDialog.Builder()
                        .setCanceledOnTouchOutside(false)
                        .setCancelable(false)

                        .setTitle("删除")
                        .setText("是否确定删除该动态？")

                        .setNegative("取消", null)
                        .setPositive("确定", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        postDelete();
                                    }
                                }
                        )
                        .show(getSupportFragmentManager());
                break;
        }
    }
}

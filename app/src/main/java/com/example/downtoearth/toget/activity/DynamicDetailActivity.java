package com.example.downtoearth.toget.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.downtoearth.toget.R;
import com.example.downtoearth.toget.adapter.CommentAdapter;
import com.example.downtoearth.toget.bean.Comment;
import com.example.downtoearth.toget.bean.DynamicListBean;
import com.example.downtoearth.toget.impl.onDialogItemClickListener;
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
    private TextView tv_like;
    private TextView tv_comment1;
    private CheckBox cb_like;
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

        tv_like=findViewById(R.id.tv_like);
        cb_like=findViewById(R.id.cb_like);
        layout_comment=findViewById(R.id.layout_comment);

        Drawable[] drawables=cb_like.getCompoundDrawables();
        drawables[0].setBounds(0,0,60,60);
        cb_like.setCompoundDrawables(drawables[0],null,null,null);

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
                postLike(position);
            }

            @Override
            public void onItemClick(final int position) {
                CustomDialog customDialog=new CustomDialog(DynamicDetailActivity.this);
                customDialog.setOnItemClick(new onDialogItemClickListener() {
                    @Override
                    public void onDelete() {
                        postDelete(position);
                    }

                    @Override
                    public void onDetail() {
                        Comment.DataBean dataBean= (Comment.DataBean) mDataList.get(position);

                        Intent intent= new Intent(DynamicDetailActivity.this,CommentDetailActivity.class);
                        intent.putExtra("id",dataBean.getId());
                        intent.putExtra("position",position);
                        startActivityForResult(intent,1002);

                    }

                    @Override
                    public void onCopy() {

                    }
                });
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
                intent.putExtra("position",position);

                startActivityForResult(intent,1002);
            }
        });
        getDetail();
        getCommentList(true);
    }

    public void initClickEvent(){
        layout_comment.setOnClickListener(this);
        ViewGroup layout_delete=findViewById(R.id.layout_delete);
        layout_delete.setOnClickListener(this);
        findViewById(R.id.layout_back).setOnClickListener(this);
        cb_like.setOnClickListener(this);
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
                .params("currentPage",mNextPage)
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
                                Intent intent=new Intent();
                                intent.putExtra("position",getIntent().getIntExtra("position",0));
                                setResult(2001,intent);
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
    public void postDelete(final int position){
        Comment.DataBean dataBean= (Comment.DataBean) mDataList.get(position);
        OkGo.post(HttpUtils.DELETE_COMMENT)
                .tag(this)
                .isMultipart(true)
                .params("token",ToolUtils.getString("token"))
                .params("id",dataBean.getId())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            if(jsonObject.getInt("code")==200){
                                showToast("删除成功");
                                mDataList.remove(position);
                                mAdapter.notifyItemRemoved(position);
                                int comment=Integer.parseInt(tv_comment.getText().toString());
                                tv_comment.setText(comment-1);
                                tv_comment1.setText(comment-1);

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

        tv_like.setText(dataBean.getAwesome()+"");
        cb_like.setText(dataBean.getAwesome()+"");
        cb_like.setChecked(dataBean.isLike());

    }
    public void parseData(String s,boolean isRefresh){
        Comment comment=new Gson().fromJson(s,Comment.class);
        if(comment.getData()!=null&&comment.getData().size()!=0){
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
            case R.id.layout_back:
                Intent intent=new Intent();
                intent.putExtra("position",getIntent().getIntExtra("position",0));
                intent.putExtra("awesome",Integer.parseInt(tv_like.getText().toString()));
                intent.putExtra("comment",Integer.parseInt(tv_comment1.getText().toString()));
                intent.putExtra("isLike",cb_like.isChecked());
                setResult(2000,intent);
                finish();
                break;

            case R.id.cb_like:
                postLike();
                break;
        }
    }

    public void postLike() {
        OkGo.post(HttpUtils.LIKE_CLICK)
                .tag(this)
                .isMultipart(true)
                .params("token", ToolUtils.getString("token"))
                .params("id", getIntent().getIntExtra("id",0))
                .params("isLike", cb_like.isChecked()? 0 : 1)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getInt("code") == 200) {
                                showToast(cb_like.isChecked()?"点赞成功":"取消点赞成功");

                                int awesome=Integer.parseInt(tv_like.getText().toString());
                                if(cb_like.isChecked()){//点赞
                                   cb_like.setText(awesome+1+"");
                                    tv_like.setText(awesome+1+"");

                                }else{
                                    cb_like.setText(awesome-1+"");
                                    tv_like.setText(awesome-1+"");


                                }
                            } else {
                                showToast(jsonObject.getString("message"));
                                cb_like.setChecked(!cb_like.isChecked());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    public void postLike(final int position) {
        final Comment.DataBean dataBean = (Comment.DataBean) mDataList.get(position);
        OkGo.post(HttpUtils.AWESOME_COMMENT)
                .tag(this)
                .isMultipart(true)
                .params("token", ToolUtils.getString("token"))
                .params("id", dataBean.getId())
                .params("isLike", dataBean.isLike() ? 1 : 0)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        log("Error");
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        log(s);
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
                            mDataList.set(position,dataBean);
                            mAdapter.notifyItemChanged(position);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }
    @Override
    public void onBackPressed() {

        Intent intent=new Intent();
        intent.putExtra("position",getIntent().getIntExtra("position",0));
        intent.putExtra("awesome",Integer.parseInt(tv_like.getText().toString()));
        intent.putExtra("comment",Integer.parseInt(tv_comment1.getText().toString()));
        intent.putExtra("isLike",cb_like.isChecked());
        setResult(2000,intent);
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
            if(resultCode==RESULT_OK){  //评论成功
                int comment=Integer.parseInt(tv_comment.getText().toString());
                comment++;
                tv_comment.setText(comment+"");
                tv_comment1.setText(comment+"");
                refreshLayout.autoRefresh();
            }
        }else if(requestCode==1001){//添加子评论

        }else if(requestCode==1002){//评论详情
            if(data!=null){
                int position=data.getIntExtra("postion",0);
                if (resultCode == 2000) {   //修改
                    Comment.DataBean dataBean= (Comment.DataBean) mDataList.get(position);
                    dataBean.setAwesome(data.getIntExtra("awesome",0));
                    dataBean.setComment(data.getIntExtra("comment",0));
                    dataBean.setLike(data.getBooleanExtra("isLike",true));
                    mAdapter.notifyItemChanged(position);
                } else if (resultCode == 2001) { //删除
                    mDataList.remove(position);
                    int comment=Integer.parseInt(tv_comment.getText().toString());
                    tv_comment1.setText(comment-1+"");
                    tv_comment.setText(comment-1+"");
                    mAdapter.notifyItemRemoved(position);
                }
            }
        }
    }
}

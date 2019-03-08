package com.example.downtoearth.toget.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.downtoearth.toget.R;
import com.example.downtoearth.toget.adapter.CommentAdapter;
import com.example.downtoearth.toget.bean.Comment;
import com.example.downtoearth.toget.bean.DynamicListBean;
import com.example.downtoearth.toget.utils.HttpUtils;
import com.example.downtoearth.toget.utils.ToolUtils;
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

public class CommentDetailActivity extends BaseActivity implements View.OnClickListener {
    private RecyclerView rv_comment;
    private SmartRefreshLayout refreshLayout;

    private ImageView iv_head;
    private TextView tv_name;
    private TextView tv_content;
    private TextView tv_time;
    private TextView tv_comment;
    private LinearLayout layout_comment;
    private CheckBox cb_like;

    private CommentAdapter mAdapter;
    private List mDataList;
    private int mNextPage = 1;

    private int uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_detail);

        initView();
        initData();
        initListener();
    }

    public void initView() {
        rv_comment = findViewById(R.id.rv_comment);
        refreshLayout = findViewById(R.id.smart_refresh);

        iv_head = findViewById(R.id.iv_head);
        tv_name = findViewById(R.id.tv_name);
        tv_content = findViewById(R.id.tv_content);
        tv_time = findViewById(R.id.tv_time);
        tv_comment = findViewById(R.id.tv_comment);
        cb_like = findViewById(R.id.cb_like);

        Drawable[] drawables = cb_like.getCompoundDrawables();
        drawables[0].setBounds(0, 0, 60, 60);
        cb_like.setCompoundDrawables(drawables[0], null, null, null);
    }

    public void initData() {
        rv_comment.setLayoutManager(new LinearLayoutManager(this));
        mDataList = new ArrayList();
        rv_comment.setAdapter(mAdapter = new CommentAdapter(mDataList));
        refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mNextPage++;
                getCommentList(false);
                refreshlayout.finishLoadmore(1000);

            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mNextPage = 1;
                getCommentList(true);
                refreshlayout.finishRefresh(1000);
            }
        });
        getDetail();
        getCommentList(true);
    }

    public void initListener() {
        findViewById(R.id.layout_back).setOnClickListener(this);
        findViewById(R.id.layout_comment).setOnClickListener(this);
        findViewById(R.id.layout_delete).setOnClickListener(this);
        cb_like.setOnClickListener(this);
    }

    public void getDetail() {
        OkGo.post(HttpUtils.COMMENT_DETAIL)
                .tag(this)
                .isMultipart(true)
                .params("token", ToolUtils.getString("token"))
                .params("id", getIntent().getIntExtra("id", 0))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e(TAG, s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getInt("code") == 200) {
                                parseDetail(jsonObject.getString("data"));
                            } else {
                                showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void getCommentList(final boolean isRefresh) {
        OkGo.post(HttpUtils.GET_SUB_COMMENTS)
                .tag(this)
                .isMultipart(true)
                .params("token", ToolUtils.getString("token"))
                .params("currentPage", mNextPage)
                .params("parent_id", getIntent().getIntExtra("id", 0))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e(TAG, s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getInt("code") == 200) {
                                parseData(s, isRefresh);
                            } else {
                                showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });


    }

    public void parseDetail(String s) {
        Comment.DataBean dataBean = new Gson().fromJson(s, Comment.DataBean.class);
        tv_name.setText(dataBean.getNickname());
        tv_content.setText(dataBean.getContent());
        tv_time.setText(ToolUtils.getTime(dataBean.getTime()));
        Glide.with(this)
                .load(HttpUtils.DOWNLOAD_URL + dataBean.getHeadPic())
                .into(iv_head);
        tv_comment.setText(dataBean.getComment() + "");

        cb_like.setText(dataBean.getAwesome() + "");
        cb_like.setChecked(dataBean.isLike());

        uid=dataBean.getUid();
    }

    public void parseData(String s, boolean isRefresh) {
        Comment comment = new Gson().fromJson(s, Comment.class);
        if(comment.getData()!=null&&comment.getData().size()!=0){
            if (isRefresh) {
                mDataList.clear();
            }
            mDataList.addAll(comment.getData());
            mAdapter.notifyDataSetChanged();
        } else {
            mNextPage--;

            showToast("没有更多数据了");
        }
    }

    public void postDelete() {
        OkGo.post(HttpUtils.DELETE_COMMENT)
                .tag(this)
                .isMultipart(true)
                .params("token", ToolUtils.getString("token"))
                .params("id", getIntent().getIntExtra("id", 0))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getInt("code") == 200) {
                                showToast("删除成功");
                                Intent intent = new Intent();
                                intent.putExtra("position", getIntent().getIntExtra("position", 0));
                                setResult(2001, intent);
                                finish();
                            } else {
                                showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent();
        intent.putExtra("position", getIntent().getIntExtra("position", 0));
        intent.putExtra("awesome", Integer.parseInt(cb_like.getText().toString()));
        intent.putExtra("comment", Integer.parseInt(tv_comment.getText().toString()));
        intent.putExtra("isLike", cb_like.isChecked());
        setResult(2000, intent);
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                Intent intent = new Intent();
                intent.putExtra("position", getIntent().getIntExtra("position", 0));
                intent.putExtra("awesome", Integer.parseInt(cb_like.getText().toString()));
                intent.putExtra("comment", Integer.parseInt(tv_comment.getText().toString()));
                intent.putExtra("isLike", cb_like.isChecked());
                setResult(2000, intent);
                finish();
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
            case R.id.layout_comment:
                PublishCommentActivity.startActivityForSubComment(this,getIntent().getIntExtra("id",0),uid,1000);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == RESULT_OK) {  //评论成功
                int comment = Integer.parseInt(tv_comment.getText().toString());
                comment++;
                tv_comment.setText(comment + "");
                refreshLayout.autoRefresh();
            }
        }
    }
}
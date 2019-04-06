package com.example.downtoearth.toget.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
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
import com.example.downtoearth.toget.impl.onDialogItemClickListener;
import com.example.downtoearth.toget.utils.HttpUtils;
import com.example.downtoearth.toget.utils.ToolUtils;
import com.example.downtoearth.toget.view.CustomDialog;
import com.example.downtoearth.toget.view.SelectDialog;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;
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

    private PromptDialog promptDialog;
    private ClipboardManager myClipboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_detail);
        promptDialog=new PromptDialog(this);
        promptDialog.showLoading("加载中");
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
        drawables[0].setBounds(0,0,ToolUtils.dip2px(this,24),ToolUtils.dip2px(this,24));
        cb_like.setCompoundDrawables(drawables[0], null, null, null);
        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

    }

    public void initData() {

        rv_comment.setLayoutManager(new LinearLayoutManager(this));
        mDataList = new ArrayList();
        rv_comment.setAdapter(mAdapter = new CommentAdapter(mDataList,true));
        mAdapter.setOnItemClikcListener(new CommentAdapter.OnItemClickListener() {
            @Override
            public void onLikeClick(int position) {

            }

            @Override
            public void onItemClick(final int position) {

               SelectDialog selectDialog=new SelectDialog(CommentDetailActivity.this);

               selectDialog.showSelectDialog(new SelectDialog.SelectItem("回复评论", new SelectDialog.OnSelectItemClickListener() {
                   @Override
                   public void onItemClick() {
                       Comment.DataBean dataBean= (Comment.DataBean) mDataList.get(position);
                       PublishCommentActivity.startActivityForSubComment(CommentDetailActivity.this,getIntent().getIntExtra("id",0),dataBean.getUid(),dataBean.getNickname(),1000);

                   }
               }),new SelectDialog.SelectItem("删除评论", new SelectDialog.OnSelectItemClickListener() {
                   @Override
                   public void onItemClick() {
                       Comment.DataBean dataBean= (Comment.DataBean) mDataList.get(position);

                       if(uid==ToolUtils.getInt(CommentDetailActivity.this,"uid")||dataBean.getUid()==ToolUtils.getInt(CommentDetailActivity.this,"uid")){
                           postDelete(position);

                       }else{
                           showToast("非本人无权删除");
                       }
                   }
               }),new SelectDialog.SelectItem("复制评论", new SelectDialog.OnSelectItemClickListener() {
                   @Override
                   public void onItemClick() {
                       Comment.DataBean dataBean= (Comment.DataBean) mDataList.get(position);

                       ClipData myClip;
                       String text = dataBean.getContent();
                       myClip = ClipData.newPlainText("text", text);
                       myClipboard.setPrimaryClip(myClip);
                       showToast("复制成功");
                   }
               }));

            }

            @Override
            public void onCommentClick(int position) {

            }

            @Override
            public void onDetailClick(int position) {

            }

            @Override
            public void onHead(int position) {
                Comment.DataBean dataBean= (Comment.DataBean) mDataList.get(position);
                MainPagerActivity.start(CommentDetailActivity.this,dataBean.getUid(),dataBean.getNickname());
            }
        });
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
        iv_head.setOnClickListener(this);
    }

    public void getDetail() {
        OkGo.post(HttpUtils.COMMENT_DETAIL)
                .tag(this)
                .isMultipart(true)
                .params("token", ToolUtils.getString(this,"token"))
                .params("id", getIntent().getIntExtra("id", 0))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e(TAG, s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getInt("code") == 200) {
                                parseDetail(jsonObject.getString("data"));
                                promptDialog.showSuccess("加载成功");

                            } else {
                                promptDialog.showError(jsonObject.getString("message"));
                                finish();
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
                .params("token", ToolUtils.getString(this,"token"))
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

        this.uid=dataBean.getUid();
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
                .params("token", ToolUtils.getString(this,"token"))
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
    public void postDelete(final int position){
        Comment.DataBean dataBean= (Comment.DataBean) mDataList.get(position);
        OkGo.post(HttpUtils.DELETE_SUB_COMMENT)
                .tag(this)
                .isMultipart(true)
                .params("token",ToolUtils.getString(this,"token"))
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
                                tv_comment.setText(comment-1+"");

                            }else{
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
            case R.id.iv_head:
                MainPagerActivity.start(this,uid,tv_name.getText().toString());
                break;
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
                if(uid!=ToolUtils.getInt(this,"uid")){
                    showToast("非本人无权删除");
                    return;
                }

                PromptButton confirm = new PromptButton("确定", new PromptButtonListener() {
                    @Override
                    public void onClick(PromptButton button) {
                        postDelete();

                    }
                });

                promptDialog.showWarnAlert("确认是否删除", new PromptButton("取消", new PromptButtonListener() {
                    @Override
                    public void onClick(PromptButton button) {
                        promptDialog.dismiss();
                    }
                }), confirm);

                break;
            case R.id.layout_comment:
                PublishCommentActivity.startActivityForSubComment(this,getIntent().getIntExtra("id",0),uid,tv_name.getText().toString(),1000);
                break;

            case R.id.cb_like:
                postLike();
                break;
        }

    }

    public void postLike() {
        OkGo.post(HttpUtils.AWESOME_COMMENT)
                .tag(this)
                .isMultipart(true)
                .params("token", ToolUtils.getString(this,"token"))
                .params("id", getIntent().getIntExtra("id",0))
                .params("isLike", cb_like.isChecked()? 0 : 1)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getInt("code") == 200) {
                                showToast(cb_like.isChecked()?"点赞成功":"取消点赞成功");

                                int awesome=Integer.parseInt(cb_like.getText().toString());
                                if(cb_like.isChecked()){//点赞
                                    cb_like.setText(awesome+1+"");

                                }else{
                                    cb_like.setText(awesome-1+"");


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

package com.example.downtoearth.toget.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.downtoearth.toget.R;
import com.example.downtoearth.toget.bean.SchoolHelp;
import com.example.downtoearth.toget.utils.HttpUtils;
import com.example.downtoearth.toget.utils.ToolUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;

import org.json.JSONException;
import org.json.JSONObject;

import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;
import okhttp3.Response;

public class SchoolHelpDetailActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_title;
    private TextView tv_content;
    private TextView tv_nickname;
    private TextView tv_time;
    private TextView tv_heart_word;
    private ImageView iv_head;
    private Button btn_post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_help_detail);

        initView();
        initData();
    }
    public void initView(){
        tv_content=findViewById(R.id.tv_content);
        tv_title=findViewById(R.id.tv_title);

        tv_nickname=findViewById(R.id.tv_nickname);
        tv_time=findViewById(R.id.tv_time);
        tv_heart_word=findViewById(R.id.tv_heart_word);
        iv_head=findViewById(R.id.iv_head);
        btn_post=findViewById(R.id.btn_post);

    }
    public void initData(){
        initListener();
        getDetail();
    }
    public void initListener(){
        findViewById(R.id.layout_back);
    }
    public void getDetail(){
        final PromptDialog promptDialog=new PromptDialog(this);
        promptDialog.showLoading("加载中");

        OkGo.post(HttpUtils.SCHOOL_HELPS_DETAIL)
                .tag(this)
                .isMultipart(true)
                .params("id",getIntent().getIntExtra("id",0))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            if(jsonObject.getInt("code")==200){
                                parseData(jsonObject.getString("data"));
                                promptDialog.showSuccess("加载成功");
                            }else{
                                promptDialog.showError("异常，请刷新重试");
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }
    public void parseData(String s){
        final SchoolHelp.DataBean dataBean=new Gson().fromJson(s,SchoolHelp.DataBean.class);
        tv_title.setText(dataBean.getTitle());
        tv_nickname.setText(dataBean.getNickname());
        tv_content.setText(dataBean.getContent());
        tv_heart_word.setText(dataBean.getHeartWord());
        tv_time.setText("发布于 "+ToolUtils.getDate(dataBean.getTime(),"yyyy-MM-dd HH:mm:ss")+" 解释权归作者所有");
        Glide.with(this).load(HttpUtils.DOWNLOAD_URL+dataBean.getHeadPic()).into(iv_head);
        View btn_no=findViewById(R.id.btn_no);
        btn_no.setVisibility(View.GONE);
        if(dataBean.getStatus()==0){
            btn_post.setClickable(true);
            btn_post.setText("提供帮助");

            btn_post.setBackgroundResource(R.drawable.btn_post_add_corner);
            btn_post.setTextColor(Color.WHITE);

            btn_post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(dataBean.getUid()==ToolUtils.getInt("uid")){
                        showToast("您不能提交自己的帮帮");
                    }else{
                        modifyStatus(1);

                    }
                }
            });

        }else if(dataBean.getStatus()==1){
            btn_post.setClickable(false);

            if(dataBean.getPostUid()==ToolUtils.getInt("uid")){
                btn_post.setBackgroundResource(R.drawable.btn_post_add_corner);

                btn_post.setText("等待发布者确认你的帮助...");
            }else if(dataBean.getUid()==ToolUtils.getInt("uid")){
                btn_post.setText("接收帮助");
                btn_post.setClickable(true);

                btn_post.setBackgroundResource(R.drawable.btn_post_add_corner);
                btn_post.setTextColor(Color.WHITE);
                btn_post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modifyStatus(2);
                    }
                });
                btn_no.setVisibility(View.VISIBLE);
                btn_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modifyStatus(3);
                    }
                });

            }else{
                btn_post.setText("发布者确认他人帮助中~");
            }
        }else if(dataBean.getStatus()==2){
            btn_post.setClickable(false);
            btn_post.setBackgroundResource(R.drawable.btn_post_unclick);
            btn_post.setTextColor(Color.RED);

            if(dataBean.getPostUid()==ToolUtils.getInt("uid")){
                btn_post.setText("发布者已确认你的帮助");
            }else if(dataBean.getUid()==ToolUtils.getInt("uid")){
                btn_post.setText("已确认接收帮助");
                btn_post.setClickable(true);
            }else{
                btn_post.setText("帮助已经在交易中");
            }
            btn_post.setBackgroundResource(R.drawable.btn_post_unclick);
            btn_post.setTextColor(Color.RED);
        }
    }

    public void modifyStatus(int status){
        final PromptDialog promptDialog=new PromptDialog(this);
        promptDialog.showLoading("提交中");

        OkGo.post(HttpUtils.MODIFY_STATUS_HELP)
                .tag(this)
                .isMultipart(true)
                .params("token",ToolUtils.getString("token"))
                .params("id",getIntent().getIntExtra("id",0))
                .params("status",status)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            if(jsonObject.getInt("code")==200){
                                getDetail();
                                promptDialog.showSuccessDelay("提交成功",500);

                            }else{
                                promptDialog.showError(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layout_back:
                finish();
                break;


        }
    }
}

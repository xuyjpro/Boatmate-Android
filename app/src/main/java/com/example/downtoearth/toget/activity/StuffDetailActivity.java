package com.example.downtoearth.toget.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.downtoearth.toget.R;
import com.example.downtoearth.toget.bean.Stuff;
import com.example.downtoearth.toget.utils.GlideImageLoader;
import com.example.downtoearth.toget.utils.HttpUtils;
import com.example.downtoearth.toget.utils.ToolUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;
import okhttp3.Response;

public class StuffDetailActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_nickname;
    private TextView tv_time;
    private TextView tv_price;
    private TextView tv_content;
    private TextView tv_heart_word;

    private ImageView iv_head;

    private Button btn_delete;
    private Banner banner;
    private int id;
    private int uid;
    private  PromptDialog promptDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stuff_detail);
        initView();
        initData();
        initEvent();
    }
    public void initView(){
        //data
        id=getIntent().getIntExtra("id",0);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle("物品详情");

        //view
        banner=findViewById(R.id.banner);
        tv_content=findViewById(R.id.tv_content);
        tv_nickname=findViewById(R.id.tv_nickname);
        tv_price=findViewById(R.id.tv_price);
        tv_time=findViewById(R.id.tv_time);
        iv_head=findViewById(R.id.iv_head);
        btn_delete=findViewById(R.id.btn_delete);
        tv_heart_word=findViewById(R.id.tv_heart_word);

        promptDialog=new PromptDialog(this);
        promptDialog.showLoading("加载中");

    }
    public void initData(){
        getDetail();

    }
    public void initEvent(){
        btn_delete.setOnClickListener(this);

    }


    public void getDetail(){
        OkGo.post(HttpUtils.GET_STUFF_DETAIL)
                .tag(this)
                .isMultipart(true)
                .params("id",id)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        ToolUtils.doHttpError(StuffDetailActivity.this);
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        log(s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            if(jsonObject.getInt("code")==200){
                                parseDetail(jsonObject.getString("data"));
                                promptDialog.showSuccess("加载成功");
                            }else{
                                promptDialog.showError(jsonObject.getString("message"));
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }
    public void parseDetail(String s){
        Stuff.DataBean dataBean=new Gson().fromJson(s,Stuff.DataBean.class);

        uid=dataBean.getStuff().getUid();
        tv_nickname.setText(dataBean.getNickname());
        tv_time.setText("发布于 "+ToolUtils.getDate(dataBean.getStuff().getTime(),"yyyy-MM-dd HH:mm:ss")+" 解释权归作者所有");
        tv_content.setText(dataBean.getStuff().getContent());
        tv_heart_word.setText(dataBean.getHeartWord());
        Glide.with(this).load(HttpUtils.DOWNLOAD_URL+dataBean.getHeadPic()).into(iv_head);

        if(getIntent().getBooleanExtra("isMarket",false)){
            tv_price.setText("￥："+dataBean.getStuff().getPrice());
            ViewGroup layout_title=findViewById(R.id.layout_title);
            layout_title.setVisibility(View.VISIBLE);
            TextView tv_title=findViewById(R.id.tv_title);
            tv_title.setText("标题："+dataBean.getStuff().getTitle());

        }


        ArrayList images=new ArrayList();
        if(dataBean.getStuff().getPicture1()!=null){
            images.add(HttpUtils.DOWNLOAD_URL+dataBean.getStuff().getPicture1());
            if(dataBean.getStuff().getPicture2()!=null){
                images.add(HttpUtils.DOWNLOAD_URL+dataBean.getStuff().getPicture2());
            }
        }
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setImageLoader(new GlideImageLoader());
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        //banner.setBannerTitles(titles);
        banner.setImages(images);
        banner.setDelayTime(1500);

        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.start();

        if(uid!=ToolUtils.getInt(this,"uid")){
            btn_delete.setVisibility(View.GONE);
        }

    }

    public void postDelete(){
        OkGo.post(HttpUtils.STUFF_DELETE)
                .tag(this)

                .isMultipart(true)
                .params("token",ToolUtils.getString(this,"token"))
                .params("id",id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            if(jsonObject.getInt("code")==200){
                                showToast("删除成功");
                                setResult(RESULT_OK,getIntent());
                                finish();
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
            case R.id.btn_delete:
                if(ToolUtils.getInt(this,"uid")!=uid){
                    promptDialog.showError("非本人无权限删除");
                    return;
                }
                promptDialog=new PromptDialog(this);

                PromptButton comfirm=new PromptButton("确定", new PromptButtonListener() {
                    @Override
                    public void onClick(PromptButton promptButton) {
                        postDelete();
                    }
                });
                promptDialog.showWarnAlert("确定是否删除",new PromptButton("取消", new PromptButtonListener() {
                    @Override
                    public void onClick(PromptButton promptButton) {
                        promptDialog.dismiss();
                    }
                }),comfirm);

                break;
        }
    }
}

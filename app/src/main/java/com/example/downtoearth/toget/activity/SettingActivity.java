package com.example.downtoearth.toget.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.downtoearth.toget.R;
import com.example.downtoearth.toget.bean.AppVersion;
import com.example.downtoearth.toget.utils.ActivityCollector;
import com.example.downtoearth.toget.utils.DataCleanManager;
import com.example.downtoearth.toget.utils.DownloadIntentService;
import com.example.downtoearth.toget.utils.HttpUtils;
import com.example.downtoearth.toget.utils.ToolUtils;
import com.example.downtoearth.toget.view.SelectDialog;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.im.android.api.JMessageClient;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;
import okhttp3.Response;

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private ViewGroup layout_contact;
    private ViewGroup layout_about_us;
    private ViewGroup layout_password,layout_check;
    private ViewGroup layout_cache;
    private TextView tv_cache;
    private Button btn_exit;
    private  PromptDialog promptDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initView();
        initData();
        initEvent();
    }
    public void initView(){
        layout_password=findViewById(R.id.layout_cache);
        layout_about_us=findViewById(R.id.layout_about_us);
        layout_contact=findViewById(R.id.layout_contact);
        layout_cache=findViewById(R.id.layout_cache);
        layout_check=findViewById(R.id.layout_check);

        btn_exit=findViewById(R.id.bt_exit);
        tv_cache=findViewById(R.id.tv_cache);
        promptDialog=new PromptDialog(this);

    }
    public void initData(){
        try {
            String cacheSize=DataCleanManager.getTotalCacheSize(this);
            tv_cache.setText(cacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void initEvent(){
        layout_password.setOnClickListener(this);
        layout_about_us.setOnClickListener(this);
        layout_contact.setOnClickListener(this);
        layout_cache.setOnClickListener(this);
        layout_check.setOnClickListener(this);

        btn_exit.setOnClickListener(this);

    }

    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }

    public void sendEmail(String address){
        Uri uri = Uri.parse("mailto:"+address);
        String[] email = {address};
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra(Intent.EXTRA_CC, email); // 抄送人
        intent.putExtra(Intent.EXTRA_SUBJECT, "这是邮件的主题部分"); // 主题
        intent.putExtra(Intent.EXTRA_TEXT, "这是邮件的正文部分"); // 正文
        startActivity(Intent.createChooser(intent, "请选择邮件类应用"));
    }

    public void checkUpdate() {
        OkGo.post(HttpUtils.GET_APP_VERSION)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        //  log(s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getInt("code") == 200) {
                                parseVersionData(s);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void parseVersionData(String s) {
        AppVersion av = new Gson().fromJson(s, AppVersion.class);
        if (av.getData() != null) {
            if (av.getData().size() != 0) {

                final AppVersion.DataBean dataBean = av.getData().get(0);
                if (dataBean.getVersionCode() > ToolUtils.getVersionCode(this)) {

                    promptDialog.getDefaultBuilder().icon(R.mipmap.logo_app);

                    promptDialog.getAlertDefaultBuilder().icon(R.mipmap.logo_app);
                    PromptButton confirm = new PromptButton("确定", new PromptButtonListener() {
                        @Override
                        public void onClick(PromptButton button) {
                            Intent intent = new Intent(SettingActivity.this, DownloadIntentService.class);
                            intent.putExtra(DownloadIntentService.INTENT_VERSION_NAME, dataBean.getVersionName());
                            intent.putExtra(DownloadIntentService.INTENT_DOWNLOAD_URL, HttpUtils.DOWNLOAD_URL + dataBean.getDownloadUrl());
                            startService(intent);
                        }
                    });

                    promptDialog.showWarnAlert("有更新，是否更新", new PromptButton("取消", new PromptButtonListener() {
                        @Override
                        public void onClick(PromptButton button) {
                            promptDialog.dismiss();
                        }
                    }), confirm);
                }else{
                    promptDialog.showInfo("当前已是最新版本");
                }
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layout_password:

                break;
            case R.id.layout_about_us:
                break;
            case R.id.layout_contact:

                SelectDialog selectDialog=new SelectDialog(this);
                selectDialog.showSelectDialog(new SelectDialog.SelectItem("发送邮件", new SelectDialog.OnSelectItemClickListener() {
                    @Override
                    public void onItemClick() {
                        promptDialog.showWarnAlert("确认是否发送邮件",new PromptButton("取消", new PromptButtonListener() {
                            @Override
                            public void onClick(PromptButton promptButton) {
                                promptDialog.dismiss();
                            }
                        }),new PromptButton("确定", new PromptButtonListener() {
                            @Override
                            public void onClick(PromptButton promptButton) {
                                sendEmail("892575153@qq.com");

                            }
                        }));

                    }
                }),new SelectDialog.SelectItem("拨打电话", new SelectDialog.OnSelectItemClickListener() {
                    @Override
                    public void onItemClick() {
                        promptDialog.showWarnAlert("确认是否拨打电话给15252478436",new PromptButton("取消", new PromptButtonListener() {
                            @Override
                            public void onClick(PromptButton promptButton) {
                                promptDialog.dismiss();
                            }
                        }),new PromptButton("确定", new PromptButtonListener() {
                            @Override
                            public void onClick(PromptButton promptButton) {
                                callPhone("15252478436");

                            }
                        }));
                    }
                }));
                break;
            case R.id.layout_cache:
                promptDialog.showWarnAlert("确认是否清除缓存",new PromptButton("取消", new PromptButtonListener() {
                    @Override
                    public void onClick(PromptButton promptButton) {
                        promptDialog.dismiss();
                    }
                }),new PromptButton("确定", new PromptButtonListener() {
                    @Override
                    public void onClick(PromptButton promptButton) {
                        DataCleanManager.clearAllCache(SettingActivity.this);
                        promptDialog.showSuccess("清除完成");
                    }
                }));
                break;
            case R.id.bt_exit:
                JMessageClient.logout();
                ActivityCollector.finishAll();
                break;
            case R.id.layout_check:
                checkUpdate();
                break;

        }
    }
}

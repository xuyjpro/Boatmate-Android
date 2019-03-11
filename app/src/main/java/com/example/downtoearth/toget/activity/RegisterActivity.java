package com.example.downtoearth.toget.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.downtoearth.toget.MainActivity;
import com.example.downtoearth.toget.R;
import com.example.downtoearth.toget.bean.UserInfo;
import com.example.downtoearth.toget.utils.HttpUtils;
import com.example.downtoearth.toget.utils.ToolUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by DownToEarth on 2018/10/18.
 */

public class RegisterActivity extends BaseActivity {
    private Button btn_register;
    private EditText et_account;
    private EditText et_password;
    private RelativeLayout layout_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        initView();
        initData();
    }

    public void initView() {
        findViewById(R.id.layout_fet_rgt).setVisibility(View.GONE);
        findViewById(R.id.layout_service).setVisibility(View.GONE);
        findViewById(R.id.layout_root).setBackgroundResource(R.mipmap.bg_register);
        btn_register = findViewById(R.id.btn_login);
        et_account = findViewById(R.id.et_account);
        et_password = findViewById(R.id.et_password);
        layout_back = findViewById(R.id.layout_back);
    }

    public void initData() {
        btn_register.setText("SIGN IN");
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postRegister();
            }
        });
        layout_back.setVisibility(View.VISIBLE);
        layout_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void postRegister() {

        String account = et_account.getText().toString();
        if (account.isEmpty()) {
            Snackbar.make(et_account, "请输入账号", Snackbar.LENGTH_SHORT)
                    .show();
            return;
        }
        String password = et_password.getText().toString();
        if (password.isEmpty()) {
            Snackbar.make(et_password, "请输入密码", Snackbar.LENGTH_SHORT)
                    .show();
            return;
        }
        final PromptDialog promptDialog = new PromptDialog(this);
        promptDialog.showLoading("注册中...");
        OkGo.post(HttpUtils.REGISTER)
                .tag(this)
                .params("phone", account)
                .params("password", password)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        promptDialog.showError("服务器异常");
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        showToast(s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getInt("code") == 200) {

                                promptDialog.showSuccess("注册成功");
                                parseData(s);

                            } else {
                                promptDialog.showError(jsonObject.getString("message"));
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
    }

    public void parseData(String s) {

        UserInfo userInfo = new Gson().fromJson(s, UserInfo.class);


        UserInfo.DataBean.UserInfoBean uib = userInfo.getData().getUserInfo();


        Intent intent = new Intent(this, FullInfoActivity
                .class);
        intent.putExtra("token",userInfo.getData().getToken());

        ToolUtils.putInt("uid", uib.getId());

        startActivity(intent);


    }

}

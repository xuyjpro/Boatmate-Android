package com.example.downtoearth.toget.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.downtoearth.toget.R;
import com.example.downtoearth.toget.bean.UserInfo;
import com.example.downtoearth.toget.utils.ActivityCollector;
import com.example.downtoearth.toget.utils.HttpUtils;
import com.example.downtoearth.toget.utils.ToolUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONObject;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by DownToEarth on 2018/10/18.
 */

public class LoginActivity extends BaseActivity {
    private Button btn_register;
    private EditText et_account;
    private EditText et_password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        TextView tv_register = findViewById(R.id.tv_register);
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btn_register = findViewById(R.id.btn_login);
        et_account = findViewById(R.id.et_account);
        et_password = findViewById(R.id.et_password);


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postLogin();
            }
        });

        String username=ToolUtils.getString(this,"username");
        String password=ToolUtils.getString(this,"password");
        if(TextUtils.isEmpty(username)||TextUtils.isEmpty(password)) {
            et_account.setText("15259900001");
            et_password.setText("123456");
        }else{
            et_account.setText(username);
            et_password.setText(password);
        }



    }

    @Override
    protected void onStart() {
        super.onStart();
        ActivityCollector.finishOther(this);
    }

    public void postLogin() {

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
        promptDialog.showLoading("登录中...");
        OkGo.post(HttpUtils.LOGIN)
                .tag(this)
                .isMultipart(true)
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
                        Log.e("result", s);
                        try {

                            JSONObject jsonObject=new JSONObject(s);
                            if (jsonObject.getInt("code")==200) {

                                promptDialog.showSuccess("登录成功");

                                parseData(s);


                            } else {
                                promptDialog.showError(jsonObject.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            promptDialog.dismiss();
                        }


                    }
                });
    }

    public void parseData(String s){

        UserInfo userInfo=new Gson().fromJson(s,UserInfo.class);

        UserInfo.DataBean.UserInfoBean uib=userInfo.getData().getUserInfo();

        ToolUtils.putString(this,"username",uib.getPhone());
        ToolUtils.putString(this,"password",uib.getPassword());

        JMessageClient.login(uib.getPhone(), uib.getPassword(), new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                showToast(s);
            }
        });

        //资料不完整
        if(uib.getNickname()==null||uib.getNickname().isEmpty()||uib.getHeadPic()==null||uib.getHeadPic().isEmpty()){
            Intent intent = new Intent(this, FullInfoActivity
                    .class);
            intent.putExtra("token",userInfo.getData().getToken());

            ToolUtils.putInt(this,"uid",userInfo.getData().getUserInfo().getId());

            startActivity(intent);
        }else{
            ToolUtils.putString(this,"token",userInfo.getData().getToken());
            ToolUtils.putInt(this,"uid",userInfo.getData().getUserInfo().getId());
            Intent intent = new Intent(this, MainActivity
                    .class);

            intent.putExtra("user_info",new Gson().toJson(uib));

            startActivity(intent);
        }

    }
}

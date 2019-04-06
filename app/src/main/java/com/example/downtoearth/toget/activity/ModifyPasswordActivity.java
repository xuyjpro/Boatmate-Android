package com.example.downtoearth.toget.activity;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.downtoearth.toget.R;
import com.example.downtoearth.toget.utils.HttpUtils;
import com.example.downtoearth.toget.utils.ToolUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;
import okhttp3.Response;

public class ModifyPasswordActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_old;
    private TextView tv_new;
    private TextView tv_new_confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);

        tv_old=findViewById(R.id.tv_old);
        tv_new=findViewById(R.id.tv_new);
        tv_new_confirm=findViewById(R.id.tv_new_confirm);

        findViewById(R.id.layout_back).setOnClickListener(this);
        findViewById(R.id.layout_old_password).setOnClickListener(this);
        findViewById(R.id.layout_new_password).setOnClickListener(this);
        findViewById(R.id.layout_new_password_confirm).setOnClickListener(this);

        findViewById(R.id.btn_post).setOnClickListener(this);

    }

    public void modifyPassword() {
        if (TextUtils.isEmpty(tv_old.getText().toString())) {

            showToast("请输入旧密码");
            return;
        }
        if (TextUtils.isEmpty(tv_new.getText().toString())) {
            showToast("请输入新密码");
            return;
        }
        if (TextUtils.isEmpty(tv_new_confirm.getText().toString())) {
            showToast("请确认新密码");
            return;
        }
        if (!tv_new.getText().toString().equals(tv_new_confirm.getText().toString())){
            showToast("两次密码输入不一样");
            return;
        }
        final PromptDialog promptDialog=new PromptDialog(this);
        HttpParams httpParams=new HttpParams();
        httpParams.put("oldPassword",tv_old.getText().toString());
        httpParams.put("newPassword",tv_new.getText().toString());

        promptDialog.showLoading("修改密码中");
        OkGo.post(HttpUtils.MODIFY_PASSWORD)
                .tag(this)
                .isMultipart(true)
                .params("token",ToolUtils.getString(this,"token"))
                .params(httpParams)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            if(jsonObject.getInt("code")==200){
                              JMessageClient.updateUserPassword(tv_old.getText().toString(), tv_new.getText().toString(), new BasicCallback() {
                                  @Override
                                  public void gotResult(int i, String s) {
                                      showToast(i+s);
                                  }
                              });
                                promptDialog.showSuccess("修改成功");

                                finish();
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
        switch (view.getId())
        {
            case R.id.layout_back:
                finish();
            case R.id.layout_old_password:
                MadeItemActivity.startAction(this,"旧密码",tv_old.getText().toString(),tv_old);
                break;
            case R.id.layout_new_password:
                MadeItemActivity.startAction(this,"新密码",tv_new.getText().toString(),tv_new);
                break;
            case R.id.layout_new_password_confirm:
                MadeItemActivity.startAction(this,"确认新密码",tv_new_confirm.getText().toString(),tv_new_confirm);
                break;
            case R.id.btn_post:
                modifyPassword();
                break;

        }
    }
}

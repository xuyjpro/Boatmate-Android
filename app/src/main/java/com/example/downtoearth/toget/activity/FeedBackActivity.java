package com.example.downtoearth.toget.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.downtoearth.toget.R;
import com.example.downtoearth.toget.utils.HttpUtils;
import com.example.downtoearth.toget.utils.ToolUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;
import okhttp3.Response;

public class FeedBackActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_content;
    private Button btn_submit;
    private PromptDialog promptDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        initView();
        initEvent();
      promptDialog=new PromptDialog(this);

    }
    public void initView(){
        et_content=findViewById(R.id.et_content);
        btn_submit=findViewById(R.id.bt_submit);
    }
    public void initEvent(){
        btn_submit.setOnClickListener(this);
        findViewById(R.id.layout_back).setOnClickListener(this);
    }

    public void post(){
        String feedback=et_content.getText().toString();

        if(TextUtils.isEmpty(feedback)){
            showToast("反馈意见不能为空");
            return;
        }
//
//        final PromptDialog promptDialog1=new PromptDialog(this);
//        promptDialog1.showLoading("提交中");
        OkGo.post(HttpUtils.PUBLISH_FEEDBACK)
                .tag(this)
                .isMultipart(true)
                .params("token",ToolUtils.getString(this,"token"))
                .params("feedback",feedback)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            if(jsonObject.getInt("code")==200){
//                                promptDialog1.showSuccess("提交成功");
                                showToast("提交成功");
                                finish();
                            }else{
                                showToast(jsonObject.getString("message"));
//                                promptDialog1.showError(jsonObject.getString("message"));
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
            case R.id.bt_submit:
                promptDialog.showWarnAlert("确认是否提交反馈",new PromptButton("取消", new PromptButtonListener() {
                    @Override
                    public void onClick(PromptButton promptButton) {
                        promptDialog.dismiss();
                    }
                }),new PromptButton("确定", new PromptButtonListener() {
                    @Override
                    public void onClick(PromptButton promptButton) {
                        post();
                    }
                }));
                break;
        }
    }
}

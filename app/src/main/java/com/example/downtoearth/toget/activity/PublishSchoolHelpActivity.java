package com.example.downtoearth.toget.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.downtoearth.toget.R;
import com.example.downtoearth.toget.utils.HttpUtils;
import com.example.downtoearth.toget.utils.ToolUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;
import okhttp3.Response;

public class PublishSchoolHelpActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title;
    private EditText et_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_publish_school_help);

        initView();
        initData();
        initEvent();
    }
    public void initView(){
        tv_title=findViewById(R.id.tv_title);
        et_content=findViewById(R.id.et_content);
    }
    public void initData(){

    }
    public void initEvent(){
        tv_title.setOnClickListener(this);
        findViewById(R.id.layout_back).setOnClickListener(this);
        findViewById(R.id.layout_set).setOnClickListener(this);
    }

    public void postPublish(){

        if(tv_title.getText().toString()==null||tv_title.getText().toString().equals("")){
            showToast("标题不能为空");
            return;
        }
        if(et_content.getText().toString()==null||tv_title.getText().toString().equals("")){
            showToast("内容不能为空");
            return;
        }
        final PromptDialog promptDialog=new PromptDialog(this);
        promptDialog.showLoading("发布中");
        OkGo.post(HttpUtils.PUBLISH_SCHOOL_HELP)
                .tag(this)
                .isMultipart(true)
                .params("token",ToolUtils.getString(this,"token"))
                .params("title",tv_title.getText().toString())
                .params("content",et_content.getText().toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        showToast(s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            if(jsonObject.getInt("code")==200){
                                promptDialog.showSuccess("发布成功");
                                setResult(RESULT_OK);
                                finish();

                            }else{
                                promptDialog.showError(jsonObject.getString("message"));finish();
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
            case R.id.tv_title:
                MadeItemActivity.startAction(this,"标题",tv_title.getText().toString(),tv_title);
                break;
            case R.id.layout_set:

                postPublish();

                break;

        }
    }
}

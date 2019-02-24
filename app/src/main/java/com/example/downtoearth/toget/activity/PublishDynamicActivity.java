package com.example.downtoearth.toget.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
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


/**
 * Created by DownToEarth on 2018/10/19.
 */

public class PublishDynamicActivity extends BaseActivity {
    private EditText mEtContent;

    public static final int ADD_SUCCESS=1000;
    public static final int ADD_CANCELED=1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dynamic);
        RelativeLayout layoutSet=findViewById(R.id.layout_set);
        mEtContent=findViewById(R.id.et_content);
        RelativeLayout layoutBack=findViewById(R.id.layout_back);

        layoutSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postDynamic();
            }
        });
        layoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(ADD_CANCELED);
                finish();
            }
        });
    }
    public void postDynamic(){
        String content=mEtContent.getText().toString();
        if(content==null||content.isEmpty()){
            Snackbar.make(mEtContent,"内容不能为空",Snackbar.LENGTH_SHORT)
                    .show();
            return;
        }
        final PromptDialog promptDialog=new PromptDialog(this);
        promptDialog.showLoading("发布中...");
        OkGo.post(HttpUtils.PUBLISH_DYNAMIC)
                .tag(this)
                .isMultipart(true)
                .params("content",content)
                .params("token",ToolUtils.getString("token"))
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        promptDialog.showError("Error");

                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            if(jsonObject.getInt("code")==200){
                                promptDialog.showSuccess("发布成功");
                                setResult(ADD_SUCCESS);
                                finish();
                            }else{
                                promptDialog.showSuccess(jsonObject.getString("message"));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }
}

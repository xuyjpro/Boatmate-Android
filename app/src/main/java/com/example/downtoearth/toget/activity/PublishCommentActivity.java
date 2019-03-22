package com.example.downtoearth.toget.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.lzy.okgo.model.HttpParams;

import org.json.JSONException;
import org.json.JSONObject;

import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by DownToEarth on 2018/10/20.
 *
 */

public class PublishCommentActivity extends BaseActivity {

    private EditText mEtContent;

    public static final int ADD_SUCCESS = 1000;
    public static final int ADD_CANCELED = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dynamic);
        findViewById(R.id.layout_send).setVisibility(View.VISIBLE);
        TextView tvName = findViewById(R.id.tv_name);
        tvName.setText("@" + getIntent().getStringExtra("nickname"));
        mEtContent = findViewById(R.id.et_content);
        mEtContent.setHint("添加评论...");
        RelativeLayout layoutBack = findViewById(R.id.layout_back);
        RelativeLayout layoutSet = findViewById(R.id.layout_set);

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

    public void postDynamic() {
        String content = mEtContent.getText().toString();
        if (content == null || content.isEmpty()) {
            Snackbar.make(mEtContent, "内容不能为空", Snackbar.LENGTH_SHORT)
                    .show();
            return;
        }

        final PromptDialog promptDialog = new PromptDialog(this);
        promptDialog.showLoading("发布中...");
        HttpParams httpParams = new HttpParams();
        httpParams.put("content", content);
        httpParams.put("parent_id",getIntent().getIntExtra("parent_id",0));
        String url;
        if(getIntent().getBooleanExtra("comment",true)){
            url=HttpUtils.PUBLISH_COMMENT;
        }else{
            url=HttpUtils.PUBLISH_SUB_COMMENT;
            httpParams.put("to_uid",getIntent().getIntExtra("to_uid",0));
        }
        OkGo.post(url)
                .tag(this)
                .isMultipart(true)
                .params("token",ToolUtils.getString(this,"token"))
                .params(httpParams)
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
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getInt("code")==200) {
                                promptDialog.showSuccess("发布成功");
                                Intent data = new Intent();
                                data.putExtra("position", getIntent().getIntExtra("position", 0));
                                setResult(RESULT_OK, data);
                                finish();
                            } else {
                                promptDialog.showSuccess("发布失败");

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    public static void startActivityForComment(Activity activity, int parentId,String  nickname,int requestCode){
        Intent intent=new Intent(activity,PublishCommentActivity.class);
        intent.putExtra("parent_id",parentId);
        intent.putExtra("nickname",nickname);
        intent.putExtra("comment",true);
        activity.startActivityForResult(intent,requestCode);
    }
    public static void startActivityForSubComment(Activity activity, int parentId,int toUid,String nickname,int requestCode){
        Intent intent=new Intent(activity,PublishCommentActivity.class);
        intent.putExtra("parent_id",parentId);
        intent.putExtra("to_uid",toUid);
        intent.putExtra("nickname",nickname);
        intent.putExtra("comment",false);

        activity.startActivityForResult(intent,requestCode);
    }
}

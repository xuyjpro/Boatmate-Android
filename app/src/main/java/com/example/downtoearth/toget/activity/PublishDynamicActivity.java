package com.example.downtoearth.toget.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.downtoearth.toget.R;
import com.example.downtoearth.toget.utils.HttpUtils;
import com.example.downtoearth.toget.utils.ToolUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by DownToEarth on 2018/10/19.
 */

public class PublishDynamicActivity extends BaseActivity {
    private EditText mEtContent;

    public static final int ADD_SUCCESS = 1000;
    public static final int ADD_CANCELED = 1001;

    private ImageView picture;
    private String picPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dynamic);
        RelativeLayout layoutSet = findViewById(R.id.layout_set);
        mEtContent = findViewById(R.id.et_content);
        picture = findViewById(R.id.picture1);
        RelativeLayout layoutBack = findViewById(R.id.layout_back);

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
        findViewById(R.id.layout_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PictureSelector.create(PublishDynamicActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .selectionMode(PictureConfig.SINGLE)
                        .imageSpanCount(3)
                        .compress(true)//是否压缩
                        .freeStyleCropEnabled(false)
                        .withAspectRatio(1, 1)
//                .scaleEnabled(true)//是否可缩放
                        .rotateEnabled(false)//是否可旋转图片
                        .minimumCompressSize(100)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
            }
        });
    }

    public void postDynamic() {
        String content = mEtContent.getText().toString();
        HttpParams httpParams=new HttpParams();
        if (content == null || content.isEmpty()) {
            Snackbar.make(mEtContent, "内容不能为空", Snackbar.LENGTH_SHORT)
                    .show();
            return;
        }
        httpParams.put("content",content);
        if(picPath!=null){
            httpParams.put("picture",new File(picPath));
        }
        final PromptDialog promptDialog = new PromptDialog(this);
        promptDialog.showLoading("发布中...");
        OkGo.post(HttpUtils.PUBLISH_DYNAMIC)
                .tag(this)
                .isMultipart(true)
                .params(httpParams)
                .params("token", ToolUtils.getString(this, "token"))
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        promptDialog.showError("Error");

                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getInt("code") == 200) {
                                promptDialog.showSuccess("发布成功");
                                setResult(ADD_SUCCESS);
                                finish();
                            } else {
                                promptDialog.showSuccess(jsonObject.getString("message"));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case PictureConfig.CHOOSE_REQUEST:
                if (resultCode == RESULT_OK) {
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    if (selectList.size() != 0) {
                        picPath = selectList.get(0).getCompressPath();
                        Glide.with(this).load(picPath).into(picture);
                    }
                }
        }
    }
}

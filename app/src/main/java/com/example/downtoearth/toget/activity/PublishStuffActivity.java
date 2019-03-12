package com.example.downtoearth.toget.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;
import okhttp3.Response;

public class PublishStuffActivity extends BaseActivity implements View.OnClickListener {


    private TextView tv_stuff;
    private EditText et_content;
    private ImageView picture1;
    private ImageView picture2;
    private List<File> pictures;

    private int category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dynamic);
        initView();
        initEvent();
    }

    public void initView() {

        tv_stuff=findViewById(R.id.tv_stuff);
        et_content=findViewById(R.id.et_content);
        picture2=findViewById(R.id.picture2);
        picture1=findViewById(R.id.picture1);

    }


    public void initEvent() {
        findViewById(R.id.layout_stuff).setVisibility(View.VISIBLE);

        findViewById(R.id.layout_set).setOnClickListener(this);
        findViewById(R.id.layout_photo).setOnClickListener(this);
        findViewById(R.id.layout_stuff).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.layout_stuff:
                final List<String> stuffs=new ArrayList<>();
                stuffs.add("寻物启事");
                stuffs.add("失物招领");
                stuffs.add("校园跳蚤");

                OptionsPickerView pvOptions = new  OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                        tv_stuff.setText(stuffs.get(options1));
                        category=options1;
                    }
                }).setTitleText("身份选择")
                        .setTitleColor(Color.parseColor("#3b4b57"))
                        .setTitleBgColor(Color.parseColor("#eeeeee"))
                        .setContentTextSize(16)
                        .setSubmitColor(Color.parseColor("#89a1b3"))
                        .setCancelColor(Color.parseColor("#89a1b3"))
                        .build();
                pvOptions.setPicker(stuffs);
                pvOptions.show();
                break;
            case R.id.layout_photo:
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())
                        .maxSelectNum(2)
                        .imageSpanCount(4)
                        .compress(true)//是否压缩
                        .freeStyleCropEnabled(false)
                        .withAspectRatio(1,1)
//                .scaleEnabled(true)//是否可缩放
                        .rotateEnabled(false)//是否可旋转图片
                        .minimumCompressSize(100)
                        .forResult(PictureConfig.CHOOSE_REQUEST);

                break;
            case R.id.layout_set:
                postPublish();
                break;
        }
    }
    public void postPublish(){
        HttpParams httpParams=new HttpParams();
        if(et_content.getText().toString()!=null){
            httpParams.put("content",et_content.getText().toString());
        }else{
            showToast("内容不能为空");
            return;
        }
        if(pictures!=null&&pictures.size()!=0){
            httpParams.putFileParams("pictures",pictures);
        }
        final PromptDialog promptDialog=new PromptDialog(this);

        promptDialog.showLoading("发布中");
        httpParams.put("token",ToolUtils.getString("token"));
        httpParams.put("category",category);
        OkGo.post(HttpUtils.PUBLISH_STUFF)
                .tag(this)
                .isMultipart(true)
                .params(httpParams)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            if(jsonObject.getInt("code")==200){
                                promptDialog.showSuccess("发布成功");
                                setResult(RESULT_OK);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            case PictureConfig.CHOOSE_REQUEST:
                if(resultCode==RESULT_OK){
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    if (selectList.size() != 0) {

                        if(pictures==null){
                            pictures=new ArrayList<>();
                        }else{

                            pictures.clear();

                        }
                        if(selectList.size()==1){
                            String picPath1=selectList.get(0).getCompressPath();
                            Glide.with(this).load(picPath1).into(picture1);
                            pictures.add(new File(picPath1));


                            picture2.setImageDrawable(null);
                        }
                        if(selectList.size()==2){

                            String picPath1=selectList.get(0).getCompressPath();
                            Glide.with(this).load(picPath1).into(picture1);
                            pictures.add(new File(picPath1));

                            String picPath2=selectList.get(1).getCompressPath();
                            Glide.with(this).load(picPath2).into(picture2);
                            pictures.add(new File(picPath2));

                        }

                    }
                }
        }
    }
}

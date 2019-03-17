package com.example.downtoearth.toget.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.ArrayList;
import java.util.List;

import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;
import okhttp3.Response;

public class PublishStuffActivity extends BaseActivity implements View.OnClickListener {


    private TextView tv_stuff;
    private EditText et_content;
    private ImageView picture1;
    private ImageView picture2;
    private List<File> pictures;
    private TextView tv_title;
    private TextView tv_price;
    private PromptDialog promptDialog;

    private int category;

    private ViewGroup layout_title;
    private ViewGroup layout_price;

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
        layout_title=findViewById(R.id.layout_title);
        tv_title=findViewById(R.id.tv_title);
        tv_price=findViewById(R.id.tv_price);
        layout_price=findViewById(R.id.layout_price);

        promptDialog=new PromptDialog(this);
    }


    public void initEvent() {
        findViewById(R.id.layout_stuff).setVisibility(View.VISIBLE);

        findViewById(R.id.layout_set).setOnClickListener(this);
        findViewById(R.id.layout_photo).setOnClickListener(this);
        findViewById(R.id.layout_stuff).setOnClickListener(this);
        layout_title.setOnClickListener(this);
        layout_price.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.layout_stuff:
                final List<String> stuffs=new ArrayList<>();
                stuffs.add("寻物启事");
                stuffs.add("失物招领");
                stuffs.add("校园跳蚤");

                PromptButton cancle = new PromptButton("取消", null);
                cancle.setTextColor(Color.parseColor("#0076ff"));
                //设置显示的文字大小及颜色
                promptDialog.getAlertDefaultBuilder().textSize(16).textColor(Color.GRAY);

                //默认两个按钮为Alert对话框，大于三个按钮的为底部SHeet形式展现


                promptDialog.showAlertSheet("选择类型", true, cancle,
                        new PromptButton("寻物启事", new PromptButtonListener() {
                            @Override
                            public void onClick(PromptButton promptButton) {
                                tv_stuff.setText("寻物启事");
                                layout_title.setVisibility(View.GONE);
                                layout_price.setVisibility(View.GONE);
                                category=0;
                            }
                        }), new PromptButton("失物招领", new PromptButtonListener() {
                            @Override
                            public void onClick(PromptButton promptButton) {
                                tv_stuff.setText("失物招领");
                                layout_title.setVisibility(View.GONE);
                                layout_price.setVisibility(View.GONE);
                                category=1;
                            }
                        }),
                        new PromptButton("校园跳蚤", new PromptButtonListener() {
                            @Override
                            public void onClick(PromptButton promptButton) {
                                tv_stuff.setText("校园跳蚤");
                                layout_title.setVisibility(View.VISIBLE);
                                layout_price.setVisibility(View.VISIBLE);
                                category=2;
                            }
                        }));


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

                 PromptButton confirm = new PromptButton("确定", new PromptButtonListener() {
                    @Override
                    public void onClick(PromptButton button) {
                        postPublish();

                    }
                });
                promptDialog.showWarnAlert("是否确定发布", new PromptButton("取消", new PromptButtonListener() {
                    @Override
                    public void onClick(PromptButton button) {
                        promptDialog.dismiss();
                    }
                }), confirm);
                break;

            case R.id.layout_title:
                MadeItemActivity.startAction(this,"标题",tv_title.getText().toString(),tv_title);
                break;
            case R.id.layout_price:
                MadeItemActivity.startAction(this,"价格(元)",tv_price.getText().toString(),tv_price);
                break;

        }
    }
    public void postPublish(){
        HttpParams httpParams=new HttpParams();
        if(!TextUtils.isEmpty(et_content.getText())){
            httpParams.put("content",et_content.getText().toString());
        }else{
            showToast("内容不能为空");
            return;
        }
        if(category==2){
            if(!TextUtils.isEmpty(tv_title.getText())) {
                showToast("校园跳蚤标题不能为空");
                return;
            }else{
                httpParams.put("title",tv_title.getText().toString());

            }
            if(!TextUtils.isEmpty(tv_price.getText())) {
                showToast("校园跳蚤价格不能为空");
                return;
            }else{
                httpParams.put("price",tv_price.getText().toString());
            }

        }

        if(pictures!=null&&pictures.size()!=0){
            httpParams.putFileParams("pictures",pictures);
        }else if(category==2){
            showToast("校园跳蚤图片不能为空");
            return;
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

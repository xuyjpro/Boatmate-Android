package com.example.downtoearth.toget.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.downtoearth.toget.MainActivity;
import com.example.downtoearth.toget.R;
import com.example.downtoearth.toget.bean.UserInfo;
import com.example.downtoearth.toget.utils.HttpUtils;
import com.example.downtoearth.toget.utils.ToolUtils;
import com.google.gson.Gson;
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

import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.util.ConvertUtils;
import cn.qqtheme.framework.widget.WheelView;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;
import okhttp3.Response;

public class EditInfoActivity extends BaseActivity implements View.OnClickListener {

    public static final int EDIT_INFO=1000;

    private ViewGroup layout_nickname;
    private ViewGroup layout_gender;
    private ViewGroup layout_birthday;
    private ViewGroup layout_heart_word;

    private TextView tv_nickname;
    private TextView tv_gender;
    private TextView tv_birthday;
    private TextView tv_heart_word;

    private Button btn_commit;
    private ImageView iv_head;

    private String headPicPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        layout_nickname = findViewById(R.id.layout_nickname);
        layout_nickname.setOnClickListener(this);

        layout_gender = findViewById(R.id.layout_gender);
        layout_gender.setOnClickListener(this);
        layout_birthday = findViewById(R.id.layout_birthday);
        layout_birthday.setOnClickListener(this);
        layout_heart_word = findViewById(R.id.layout_heart_word);
        layout_heart_word.setOnClickListener(this);

        tv_nickname = findViewById(R.id.tv_nickname);
        tv_gender = findViewById(R.id.tv_gender);
        tv_birthday = findViewById(R.id.tv_birthday);
        tv_heart_word = findViewById(R.id.tv_heart_word);

        btn_commit = findViewById(R.id.btn_commit);
        btn_commit.setOnClickListener(this);

        iv_head=findViewById(R.id.iv_head);
        findViewById(R.id.layout_head).setOnClickListener(this);

        getUserInfo();
    }

    public void getUserInfo(){
        OkGo.post(HttpUtils.GET_USER_INFO)
                .tag(this)
                .isMultipart(true)
                .params("token",ToolUtils.getString("token"))
                .headers("Content-Type","application/json")
                .execute(new StringCallback() {

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e(TAG,s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            if(jsonObject.getInt("code")==200){
                                parseData(jsonObject.getString("data"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }
    public void parseData(String dataStr){
        UserInfo.DataBean.UserInfoBean uib=(new Gson()).fromJson(dataStr,UserInfo.DataBean.UserInfoBean.class);
        if(uib.getHeadPic()!=null){
            Glide.with(this).load(HttpUtils.DOWNLOAD_URL+uib.getHeadPic()).into(iv_head);
        }
        tv_nickname.setText(uib.getNickname());
        if(uib.isGender()){
            tv_gender.setText("男");
        }else{
            tv_gender.setText("女");

        }
        tv_heart_word.setText(uib.getHeartWord());
        tv_birthday.setText(uib.getBirthday());
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_head:
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())
                        .selectionMode(PictureConfig.SINGLE)
                        .imageSpanCount(4)
                        .compress(true)//是否压缩
                        .enableCrop(true)//是否裁剪
                        .circleDimmedLayer(true)//是否是圆形裁剪
                        .showCropFrame(false)//是否显示裁剪边框
                        .showCropGrid(false)//是否显示裁剪网格
                        .freeStyleCropEnabled(false)
                        .withAspectRatio(1,1)
//                .scaleEnabled(true)//是否可缩放
                        .rotateEnabled(false)//是否可旋转图片
                        .minimumCompressSize(100)
                        .forResult(PictureConfig.CHOOSE_REQUEST);

                break;
            case R.id.layout_nickname:

                MadeItemActivity.tv_static = tv_nickname;
                MadeItemActivity.startAction(this,"昵称",tv_nickname.getText().toString());
                break;
            case R.id.layout_gender:

                OptionPicker picker = new OptionPicker(this, new String[]{
                        "男", "女"
                });
                picker.setCanceledOnTouchOutside(true);
                picker.setDividerRatio(WheelView.DividerConfig.FILL);
                picker.setShadowColor(Color.GRAY, 40);
                if(tv_gender.getText().toString().equals("男")){
                    picker.setSelectedIndex(0);

                }else{
                    picker.setSelectedIndex(1);

                }
                picker.setCycleDisable(true);
                picker.setTextSize(11);
                picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(int index, String item) {
                        tv_gender.setText(item);

                    }
                });
                picker.show();

                break;
            case R.id.layout_birthday:
                final DatePicker picker1 = new DatePicker(this);
                picker1.setCanceledOnTouchOutside(true);
                picker1.setUseWeight(true);
                picker1.setTopPadding(ConvertUtils.toPx(this, 10));
                picker1.setRangeEnd(2018, 12, 30);
                picker1.setRangeStart(1949, 8, 00);
                String[] birthday=tv_birthday.getText().toString().trim().split("-");

                picker1.setSelectedItem(Integer.parseInt(birthday[0]), Integer.parseInt(birthday[1]),  Integer.parseInt(birthday[2]));
                picker1.setResetWhileWheel(false);
                picker1.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        tv_birthday.setText(picker1.getSelectedYear() + '-' + picker1.getSelectedMonth() + '-' + picker1.getSelectedDay());
                    }
                });
                picker1.setOnWheelListener(new DatePicker.OnWheelListener() {
                    @Override
                    public void onYearWheeled(int index, String year) {
                        picker1.setTitleText(year + "-" + picker1.getSelectedMonth() + "-" + picker1.getSelectedDay());
                    }

                    @Override
                    public void onMonthWheeled(int index, String month) {
                        picker1.setTitleText(picker1.getSelectedYear() + "-" + month + "-" + picker1.getSelectedDay());
                    }

                    @Override
                    public void onDayWheeled(int index, String day) {
                        picker1.setTitleText(picker1.getSelectedYear() + "-" + picker1.getSelectedMonth() + "-" + day);
                    }
                });
                picker1.show();

                break;
            case R.id.layout_heart_word:
                Intent intent1 = new Intent(this, MadeItemActivity.class);
                intent1.putExtra("title", "个性签名");
                if (tv_heart_word.getText() != null) {
                    intent1.putExtra("content", tv_heart_word.getText());
                }
                //startActivityForResult(intent,MadeItemActivity.REQUEST_CODE);
                MadeItemActivity.tv_static = tv_heart_word;
                startActivity(intent1);
                break;

            case R.id.btn_commit:
                postData();
                break;
        }
    }


    public void postData(){
        HttpParams httpParams=new HttpParams();

        if(tv_nickname.getText()==null||tv_nickname.getText().toString().isEmpty()){
            showToast("请输入昵称");
            return;
        }else{
            if(tv_nickname.getText().length()>=12){
                showToast("昵称不能超过12个字符");
            }else {
                httpParams.put("nickname",tv_nickname.getText().toString());
            }
        }

        if(headPicPath!=null){
            httpParams.put("headPic",new File(headPicPath));
        }

        if(tv_gender.getText().equals("男")){
            httpParams.put("gender",1);
        }else{
            httpParams.put("gender",0);
        }

        if(tv_heart_word.getText()!=null){
            httpParams.put("heartWord",tv_heart_word.getText().toString());
        }
        if(tv_birthday.getText()!=null){
            httpParams.put("birthday",tv_birthday.getText().toString());
        }
        final PromptDialog promptDialog=new PromptDialog(this);
        promptDialog.showLoading("提交中...");
        OkGo.post(HttpUtils.EDIT_INFO)
                .tag(this)
                .isMultipart(true)
                .params("token",ToolUtils.getString("token"))
                .params(httpParams)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e(TAG,s);
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            if(jsonObject.getInt("code")==200){
                                promptDialog.showSuccess("提交成功");

                                Intent intent=new Intent(EditInfoActivity.this,MainActivity.class);
                                intent.putExtra("user_info",jsonObject.getString("data"));
                                startActivity(intent);
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
                        headPicPath = selectList.get(0).getCompressPath();
                        Glide.with(this).load(headPicPath).into(iv_head);
                    }
                }
        }
    }
}

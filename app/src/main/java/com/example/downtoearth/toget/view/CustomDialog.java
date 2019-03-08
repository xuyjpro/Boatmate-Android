package com.example.downtoearth.toget.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.downtoearth.toget.R;
import com.example.downtoearth.toget.impl.onDialogItemClickListener;

/**
 * 创建自定义的dialog，主要学习其实现原理
 * Created by chengguo on 2016/3/22.
 */
public class CustomDialog extends Dialog implements View.OnClickListener {



    private TextView tv_comment;
    private TextView tv_delete;

    private onDialogItemClickListener listener;


    public CustomDialog(Context context) {
        super(context, R.style.MyDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(true);

        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();
        
    }
    public void setOnItemClick(onDialogItemClickListener listener){
        this.listener=listener;
    }
    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        tv_comment.setOnClickListener(this);
        tv_delete.setOnClickListener(this);
    }

    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {

    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        tv_comment=findViewById(R.id.tv_comment);
        tv_delete=findViewById(R.id.tv_delete);
    }

    @Override
    public void onClick(View view) {
        if(listener!=null){
            switch (view.getId()){
                case R.id.tv_comment:
                    listener.onDetail();
                    break;
                case R.id.tv_delete:
                    listener.onDelete();
                    break;
            }
            dismiss();
        }
    }

    /**

     * 设置确定按钮和取消被点击的接口
     */


}


package com.example.downtoearth.toget.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.downtoearth.toget.R;
import com.example.downtoearth.toget.adapter.SelectDialogAdapter;
import com.example.downtoearth.toget.impl.OnItemClickListener;
import com.example.downtoearth.toget.impl.onDialogItemClickListener;
import com.example.downtoearth.toget.utils.ToolUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建自定义的dialog，主要学习其实现原理
 * Created by chengguo on 2016/3/22.
 */
public class SelectDialog extends Dialog {




    private RecyclerView recyclerView;
    private SelectDialogAdapter mAdapter;
    public SelectDialog(Context context) {
        super(context, R.style.MyDialog);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(true);
        recyclerView=findViewById(R.id.recycler_view);
        ViewGroup.LayoutParams lp=recyclerView.getLayoutParams();
        lp.width=ToolUtils.dip2px(getContext(),280);
        recyclerView.setLayoutParams(lp);
        initData();

/*        Window window=getWindow();
        WindowManager.LayoutParams lp=getWindow().getAttributes();

        //lp.width=ViewGroup.LayoutParams.MATCH_PARENT;
    //    lp.horizontalMargin=ToolUtils.dip2px(getContext(),12);
//        lp.width=ToolUtils.getWidth(getContext())-ToolUtils.dip2px(getContext(),12);
        Toast.makeText(getContext(),lp.width+"",Toast.LENGTH_SHORT).show();
        window.setAttributes(lp);*/

    }
    public void initData(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

       recyclerView.setAdapter(mAdapter=new SelectDialogAdapter());
       mAdapter.setListener(new OnItemClickListener() {
           @Override
           public void onItemClick(int position) {
               SelectItem item= (SelectItem) mAdapter.getList().get(position);
               item.getListener().onItemClick();

               dismiss();

           }
       });

    }

    public void showSelectDialog(SelectItem... selectItems){
        show();

        List list=new ArrayList();
        for(SelectItem item:selectItems){
            list.add(item);
        }
        if(mAdapter==null){
            initData();
        }
        mAdapter.addAll(list,false);
    }

    public static class SelectItem{

        private String itemName;
        private OnSelectItemClickListener listener;

        private SelectItem(){}

        public SelectItem(String itemName, OnSelectItemClickListener listener) {
            this.itemName = itemName;
            this.listener = listener;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public OnSelectItemClickListener getListener() {
            return listener;
        }

        public void setListener(OnSelectItemClickListener listener) {
            this.listener = listener;
        }
    }
    public interface OnSelectItemClickListener{
        void onItemClick();
    }

    /**

     * 设置确定按钮和取消被点击的接口
     */


}


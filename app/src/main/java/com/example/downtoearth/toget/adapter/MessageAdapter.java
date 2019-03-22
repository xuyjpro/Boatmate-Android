package com.example.downtoearth.toget.adapter;

import android.app.Notification;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.downtoearth.toget.R;
import com.google.gson.Gson;

import java.io.File;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private Context context;
    private List<Message> list;
    private UserInfo mMyInfo,targetInfo;

    public MessageAdapter(List list) {
        this.list = list;
        mMyInfo=JMessageClient.getMyInfo();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private ViewGroup layout_left,layout_right;
        private ImageView head_left,head_right;
        private TextView message_left,message_right;
        public ViewHolder(View itemView) {
            super(itemView);
            layout_left=itemView.findViewById(R.id.layout_left);
            layout_right=itemView.findViewById(R.id.layout_right);
            head_left=itemView.findViewById(R.id.head_left);
            head_right=itemView.findViewById(R.id.head_right);
            message_left=itemView.findViewById(R.id.message_left);
            message_right=itemView.findViewById(R.id.message_right);

        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view=LayoutInflater.from(context).inflate(R.layout.message_item,null);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Message message=list.get(position);

        if(position==0){
            JMessageClient.getUserInfo(((UserInfo) message.getTargetInfo()).getUserName(), new GetUserInfoCallback() {
                @Override
                public void gotResult(int i, String s, UserInfo userInfo) {
                    targetInfo=userInfo;
                }
            });
        }
       TextContent textContent= (TextContent) message.getContent();
        if(message.getDirect().toString().equals("send")){
            Glide.with(context).load(mMyInfo.getAvatarFile()).into(holder.head_right);
            holder.message_right.setText(textContent.getText());


            holder.layout_right.setVisibility(View.VISIBLE);
            holder.layout_left.setVisibility(View.GONE);
        }else{
            if(targetInfo==null){
                JMessageClient.getUserInfo(((UserInfo) message.getTargetInfo()).getUserName(), new GetUserInfoCallback() {
                    @Override
                    public void gotResult(int i, String s, UserInfo userInfo) {
                        Glide.with(context).load(userInfo.getAvatarFile()).into(holder.head_left);

                    }
                });
            }else{
                Glide.with(context).load(targetInfo.getAvatarFile()).into(holder.head_left);

            }

            holder.message_left.setText(textContent.getText());


            holder.layout_left.setVisibility(View.VISIBLE);
            holder.layout_right.setVisibility(View.GONE);


        }

        message.setHaveRead(new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
            }
        });
    }

    public void setList(List<Message> list) {
        this.list = list;
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}

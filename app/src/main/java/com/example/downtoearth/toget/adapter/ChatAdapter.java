package com.example.downtoearth.toget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.downtoearth.toget.R;

import java.util.List;

import cn.jpush.im.android.api.model.Conversation;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private Context context;
    private OnItemClickListener listener;
    private List<Conversation> list;

    public ChatAdapter(List<Conversation> list) {
        this.list = list;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_nickname;
        private TextView tv_last_message;
        private ImageView iv_head;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_last_message=itemView.findViewById(R.id.tv_last_message);
            tv_nickname=itemView.findViewById(R.id.tv_nickname);
            iv_head=itemView.findViewById(R.id.iv_head);

        }
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context=parent.getContext();
        View view=LayoutInflater.from(context).inflate(R.layout.chat_item,null);
        final ViewHolder holder=new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    int position=holder.getAdapterPosition();
                    listener.onItemClick(position);
                }
            }
        });
        holder.iv_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    int position=holder.getAdapterPosition();
                    listener.onHeadClick(position);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Conversation conversation=list.get(position);
        Glide.with(context).load(conversation.getAvatarFile()).into(holder.iv_head);
        //holder.tv_last_message.setText();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public  interface OnItemClickListener{
        void onItemClick(int position);
        void onHeadClick(int position);
    }
}

package com.example.downtoearth.toget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.downtoearth.toget.R;
import com.example.downtoearth.toget.fragment.ChatFragment;

import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.UserInfo;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private Context context;
    private OnItemClickListener listener;
    private List<Conversation> mDataList;

    private int unReadCount;

    private ChatFragment.OnGetUnReadCount onGetUnReadCount;
    public ChatAdapter(List<Conversation> list) {
        this.mDataList = list;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_nickname;
        private TextView tv_last_message;
        private ImageView iv_head;
        private TextView tv_unread;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_last_message = itemView.findViewById(R.id.tv_last_message);
            tv_nickname = itemView.findViewById(R.id.tv_nickname);
            iv_head = itemView.findViewById(R.id.iv_head);
            tv_unread = itemView.findViewById(R.id.tv_unread);

        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.chat_item, null);
        final ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    int position = holder.getAdapterPosition();
                    listener.onItemClick(position);
                }
            }
        });
        holder.iv_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    int position = holder.getAdapterPosition();
                    listener.onHeadClick(position);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Conversation conversation = mDataList.get(position);
        if (conversation.getUnReadMsgCnt() != 0) {
            holder.tv_unread.setVisibility(View.VISIBLE);
            holder.tv_unread.setText(conversation.getUnReadMsgCnt() + "");
            unReadCount += conversation.getUnReadMsgCnt();
        } else {
            holder.tv_unread.setVisibility(View.GONE);
        }
//        holder.iv_head.setImageResource(R.mipmap.default_help_pic);
        JMessageClient.getUserInfo(((UserInfo) conversation.getTargetInfo()).getUserName(), new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                Glide.with(context).load(userInfo.getAvatarFile()).into(holder.iv_head);
                holder.tv_nickname.setText(userInfo.getNickname());

            }
        });
        if (conversation.getLatestMessage() != null) {
            if (conversation.getLatestMessage().getContent() != null) {
                TextContent textContent = (TextContent) conversation.getLatestMessage().getContent();
                if (textContent != null && textContent.getText() != null) {
                    holder.tv_last_message.setText(textContent.getText());

                }
            }
        }
        if(position==mDataList.size()-1&&onGetUnReadCount!=null){
            onGetUnReadCount.getUnReadCount(unReadCount);
        }

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onHeadClick(int position);
    }

    public void setDataList(List list, ChatFragment.OnGetUnReadCount getUnReadCount) {
        this.unReadCount = 0;
        this.mDataList = list;
        this.onGetUnReadCount=getUnReadCount;
        notifyDataSetChanged();

    }

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}

package com.example.downtoearth.toget.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.downtoearth.toget.R;
import com.example.downtoearth.toget.bean.Comment;
import com.example.downtoearth.toget.utils.HttpUtils;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by DownToEarth on 2018/10/18.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private Context mContext;
    private List mDataList;
    private OnItemClickListener mListener;

    public void setOnItemClikcListener(OnItemClickListener listener){
        this.mListener=listener;
    }
    public CommentAdapter(List list) {
        this.mDataList = list;
    }
    public List getDataList(){
        return mDataList;
    }
    public void setDataList(List list){
        this.mDataList=list;
    }
    public interface OnItemClickListener{
        void onLikeClick(int position);
        void onItemClick(int position);
        void onCommentClick(int position);
        void onDetailClick(int position);
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_head;
        private TextView tv_name;
        private TextView tv_content;

        private TextView tv_to_nickname;
        private TextView tv_time;
        private LinearLayout layout_like;
        private TextView tv_comment;

        private CheckBox cb_like;
        public ViewHolder(View itemView) {
            super(itemView);
            iv_head = itemView.findViewById(R.id.iv_head);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_to_nickname=itemView.findViewById(R.id.tv_to_nickname);

//            layout_comment = itemView.findViewById(R.id.layout_comment);
            layout_like = itemView.findViewById(R.id.layout_like);
            tv_comment=itemView.findViewById(R.id.tv_comment);

            cb_like=itemView.findViewById(R.id.cb_like);


            Drawable[] drawables=cb_like.getCompoundDrawables();
            drawables[2].setBounds(0,0,60,60);
            cb_like.setCompoundDrawables(null,null,drawables[2],null);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.subdynamic_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.cb_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=holder.getAdapterPosition();
                if(mListener!=null){
                    mListener.onLikeClick(position);
                }
            }
        });
        holder.tv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=holder.getAdapterPosition();
                if(mListener!=null){
                    mListener.onDetailClick(position);
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                if(mListener!=null){
                    mListener.onItemClick(position);
                }
            }
        });
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Comment.DataBean dataBean= (Comment.DataBean) mDataList.get(position);
        Glide.with(mContext).load(HttpUtils.DOWNLOAD_URL+dataBean.getHeadPic()).into(holder.iv_head);
        holder.tv_name.setText(dataBean.getNickname());
        holder.cb_like.setText(dataBean.getAwesome()+"");
        holder.cb_like.setChecked(dataBean.isLike());
        holder.tv_time.setText(getTime(dataBean.getTime()));
        holder.tv_content.setText(dataBean.getContent());
        if(dataBean.getComment()>0){
            holder.tv_comment.setVisibility(View.VISIBLE);

            holder.tv_comment.setText(dataBean.getComment()+"条回复  >");
        }else{
            holder.tv_comment.setText("");
            holder.tv_comment.setVisibility(View.GONE);
        }
        if(dataBean.getToNickname()!=null){
            holder.tv_to_nickname.setText(" @"+dataBean.getToNickname());
        }
    }

    public String getTime(long date) {
        long time=System.currentTimeMillis()-date;
        time=time/1000;
        if (time < 60) {
            return time+1 + "秒前";
        } else if (time < 60 * 60) {
            return time / 60 + "分钟前";
        }else if(time<60*60*24){
            return time/60/60+"小时前";
        }else if(time<60*60*24*30){
            return time/60/60/24+"天前";
        }else{
            return getDate(date,"yyyy-MM-dd");
        }
    }
    public String getDate(long time,String patten){
        SimpleDateFormat format = new SimpleDateFormat(patten);
        return  format.format(time);
    }
    @Override
    public int getItemCount() {

        return mDataList.size();
    }
}

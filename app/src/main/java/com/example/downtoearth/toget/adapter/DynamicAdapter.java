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
import com.example.downtoearth.toget.bean.DynamicListBean;
import com.example.downtoearth.toget.utils.DisplayUtils;
import com.example.downtoearth.toget.utils.HttpUtils;
import com.example.downtoearth.toget.utils.ToolUtils;

import java.text.SimpleDateFormat;
import java.util.List;

import static com.example.downtoearth.toget.utils.ToolUtils.getDate;

/**
 * Created by DownToEarth on 2018/10/18.
 */

public class DynamicAdapter extends RecyclerView.Adapter<DynamicAdapter.ViewHolder> {
    private Context mContext;
    private List mDataList;
    private OnItemClickListener mListener;

    public void setOnItemClikcListener(OnItemClickListener listener){
        this.mListener=listener;
    }
    public DynamicAdapter(List list) {
        this.mDataList = list;
    }
    public interface OnItemClickListener{
        void onLikeClick(int position);
        void onItemClick(int position);
        void onCommentClick(int position);
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_head;
        private TextView tv_name;
        private RelativeLayout layout_more;
        private TextView tv_content;
        private CheckBox cb_like;
        private TextView tv_comment;
        private TextView tv_share;

        private TextView tv_time;
        private LinearLayout layout_like;
        private LinearLayout layout_comment;
        private LinearLayout layout_share;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_head = itemView.findViewById(R.id.iv_head);
            tv_name = itemView.findViewById(R.id.tv_name);
            layout_more = itemView.findViewById(R.id.layout_more);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_time = itemView.findViewById(R.id.tv_time);

            layout_share = itemView.findViewById(R.id.layout_share);
            layout_comment = itemView.findViewById(R.id.layout_comment);
            layout_like = itemView.findViewById(R.id.layout_like);
            tv_comment = itemView.findViewById(R.id.tv_comment);
            cb_like = itemView.findViewById(R.id.cb_like);
            tv_share = itemView.findViewById(R.id.tv_share);

            Drawable[] drawables=cb_like.getCompoundDrawables();
            drawables[0].setBounds(0,0,ToolUtils.dip2px(22),ToolUtils.dip2px(22));
            cb_like.setCompoundDrawables(drawables[0],null,null,null);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.dynamic_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cb_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               int position=holder.getAdapterPosition();

                if(mListener!=null){
                    mListener.onLikeClick(position);
                }
            }
        });
        holder.layout_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();

                if(mListener!=null){
                    mListener.onCommentClick(position);
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
        DynamicListBean.DataBean dataBean= (DynamicListBean.DataBean) mDataList.get(position);
        Glide.with(mContext).load(HttpUtils.DOWNLOAD_URL+dataBean.getHeadPic()).into(holder.iv_head);
        holder.tv_content.setText(dataBean.getContent());
        holder.tv_name.setText(dataBean.getNickname());

        holder.cb_like.setText(dataBean.getAwesome()+"");
        if(dataBean.isLike()){
            holder.cb_like.setChecked(true);
        }else{
            holder.cb_like.setChecked(false);
        }
        holder.tv_comment.setText(dataBean.getComment()+"");
        holder.tv_time.setText(ToolUtils.getDate(dataBean.getTime(),"yyyy-MM-dd HH:mm:ss"));

    }



    @Override
    public int getItemCount() {
        return mDataList.size();

    }
}

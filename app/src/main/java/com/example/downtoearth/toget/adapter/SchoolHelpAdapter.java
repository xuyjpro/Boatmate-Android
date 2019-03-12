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
import com.example.downtoearth.toget.bean.SchoolHelp;
import com.example.downtoearth.toget.impl.OnItemClickListener;
import com.example.downtoearth.toget.utils.HttpUtils;
import com.example.downtoearth.toget.utils.ToolUtils;

import java.util.List;

public class SchoolHelpAdapter extends RecyclerView.Adapter<SchoolHelpAdapter.ViewHolder> {
    private Context context;

    private OnItemClickListener listener;

    private List mDataList;
    public SchoolHelpAdapter(List list){
        this.mDataList=list;
    }
    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_head;
        private TextView tv_nickname;
        private TextView tv_title;
        private TextView tv_content;
        private TextView tv_time;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_head=itemView.findViewById(R.id.iv_head);
            tv_nickname=itemView.findViewById(R.id.tv_nickname);
            tv_title=itemView.findViewById(R.id.tv_title);
            tv_content=itemView.findViewById(R.id.tv_content);
            tv_time=itemView.findViewById(R.id.tv_time);


        }
    }

    public void setOnItemListener(OnItemClickListener listener){
        this.listener=listener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.help_item, null);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                   int position= viewHolder.getAdapterPosition();
                    listener.onItemClick(position);
                }

            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SchoolHelp.DataBean dataBean= (SchoolHelp.DataBean) mDataList.get(position);
//        if(dataBean.getPicture()!=null){
//            ToolUtils.loadImage(holder.iv_head,dataBean.getPicture());
//        }else{
//            ToolUtils.loadImage(holder.iv_head,dataBean.getHeadPic());
//        }
        Glide.with(context).load(HttpUtils.DOWNLOAD_URL+dataBean.getHeadPic()).into(holder.iv_head);

        holder.tv_content.setText(dataBean.getContent());
        holder.tv_nickname.setText(dataBean.getNickname());
        holder.tv_time.setText(ToolUtils.getDate(dataBean.getTime(),"yyyy-MM-dd HH:mm:ss"));
        holder.tv_title.setText(dataBean.getTitle());
    }

    @Override
    public int getItemCount() {

        return mDataList.size();
    }

}



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
import com.example.downtoearth.toget.bean.Stuff;
import com.example.downtoearth.toget.utils.HttpUtils;
import com.example.downtoearth.toget.utils.ToolUtils;

import java.util.List;


public class StuffLossAdapter extends RecyclerView.Adapter<StuffLossAdapter.ViewHolder> {
    private Context context;
    private onItemClickListener listener;
    private List list;
    public interface  onItemClickListener{
        public void onItemClick(int position);
        public void onMoreClick(int position);
        void onPictureClick(int position,int index);
    }
    public StuffLossAdapter(List list){
        this.list=list;
    }
    public void setOnItemListener(onItemClickListener listener){
        this.listener=listener;
    }
    static class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv_head,picture1,picture2;
        private TextView tv_nickname;
        private TextView tv_time;
        private TextView tv_content;
        private ViewGroup layout_more;
        private ViewGroup layout_picture;
        public ViewHolder(View itemView) {
            super(itemView);
            iv_head=itemView.findViewById(R.id.iv_head);
            picture1=itemView.findViewById(R.id.picture1);
            picture2=itemView.findViewById(R.id.picture2);
            tv_nickname=itemView.findViewById(R.id.tv_nickname);
            tv_content=itemView.findViewById(R.id.tv_content);
            layout_more=itemView.findViewById(R.id.layout_more);
            tv_time=itemView.findViewById(R.id.tv_time);
            layout_picture=itemView.findViewById(R.id.layout_picture);

        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view=LayoutInflater.from(context).inflate(R.layout.stuff_loss_item,null);
        final ViewHolder holder=new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    int positon=holder.getAdapterPosition();
                    listener.onItemClick(positon);
                }
            }
        });
        holder.layout_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    int positon=holder.getAdapterPosition();
                    listener.onMoreClick(positon);
                }
            }
        });
        holder.picture1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    int positon=holder.getAdapterPosition();

                    listener.onPictureClick(positon,0);

                }
            }
        });
        holder.picture2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    int positon=holder.getAdapterPosition();

                    listener.onPictureClick(positon,1);

                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Stuff.DataBean stuff= (Stuff.DataBean) list.get(position);
        ToolUtils.loadImage(context,holder.iv_head,stuff.getHeadPic());
        holder.tv_nickname.setText(stuff.getNickname());
        holder.tv_content.setText(stuff.getStuff().getContent());
        holder.tv_time.setText(ToolUtils.getDate(stuff.getStuff().getTime(),"yyyy-MM-dd HH:mm:ss"));
        if(stuff.getStuff().getPicture1()!=null){
            holder.layout_picture.setVisibility(View.VISIBLE);

            Glide.with(context).load(HttpUtils.DOWNLOAD_URL+stuff.getStuff().getPicture1()).into(holder.picture1);
            if(stuff.getStuff().getPicture2()!=null){
                holder.picture2.setVisibility(View.VISIBLE);

                Glide.with(context).load(HttpUtils.DOWNLOAD_URL+stuff.getStuff().getPicture2()).into(holder.picture2);

            }else{
                holder.picture2.setVisibility(View.GONE);
            }
        }else{
            holder.layout_picture.setVisibility(View.GONE);

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}

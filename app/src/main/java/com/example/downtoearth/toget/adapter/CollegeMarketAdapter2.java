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

public class CollegeMarketAdapter2 extends BaseAdapter<CollegeMarketAdapter2.ViewHolder> {

    private Context context;
    private CollegeMarketAdapter.onItemClickListener listener;
    public void setOnItemListener(CollegeMarketAdapter.onItemClickListener listener){
        this.listener=listener;
    }

    public interface  onItemClickListener{
        void onItemClick(int position);
        void onMoreClick(int position);
        void onPictureClick(int position);
    }
    static class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv_head,iv_picture;
        private TextView tv_nickname;
        private TextView tv_time;
        private TextView tv_title;
        private TextView tv_price;
        public ViewHolder(View itemView) {
            super(itemView);
            iv_head=itemView.findViewById(R.id.iv_head);
            iv_picture=itemView.findViewById(R.id.iv_picture);
            tv_nickname=itemView.findViewById(R.id.tv_nickname);
            tv_title=itemView.findViewById(R.id.tv_title);
            tv_time=itemView.findViewById(R.id.tv_time);
            tv_price=itemView.findViewById(R.id.tv_price);

        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view=LayoutInflater.from(context).inflate(R.layout.college_market_item,null);
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


        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Stuff.DataBean stuff= (Stuff.DataBean) list.get(position);
        ToolUtils.loadImage(context,holder.iv_head,stuff.getHeadPic());
        holder.tv_nickname.setText(stuff.getNickname());
        holder.tv_time.setText(ToolUtils.getDate(stuff.getStuff().getTime(),"yyyy-MM-dd HH:mm:ss"));
        holder.tv_title.setText(stuff.getStuff().getTitle());
        holder.tv_price.setText("￥:"+stuff.getStuff().getPrice());
        if(stuff.getStuff().getPicture1()!=null) {

            Glide.with(context).load(HttpUtils.DOWNLOAD_URL + stuff.getStuff().getPicture1()).into(holder.iv_picture);
        }else{
            holder.iv_picture.setVisibility(View.GONE);
        }
    }
}

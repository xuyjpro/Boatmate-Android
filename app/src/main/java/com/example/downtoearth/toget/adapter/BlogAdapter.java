package com.example.downtoearth.toget.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.downtoearth.toget.R;
import com.example.downtoearth.toget.bean.Blog;
import com.example.downtoearth.toget.impl.OnItemClickListener;

public class BlogAdapter extends BaseAdapter <BlogAdapter.ViewHolder> {
    private OnItemClickListener listener;

    public OnItemClickListener getListener() {
        return listener;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_title;
        private TextView tv_nickname;
        private TextView tv_content;
        private TextView tv_desc;

        private ImageView iv_head;
        private ImageView iv_picture;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_title=itemView.findViewById(R.id.tv_title);
            tv_nickname=itemView.findViewById(R.id.tv_nickname);
            tv_content=itemView.findViewById(R.id.tv_content);
            tv_desc=itemView.findViewById(R.id.tv_desc);
            iv_head=itemView.findViewById(R.id.iv_head);
            iv_picture=itemView.findViewById(R.id.iv_picture);

        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_item,null);
        final ViewHolder viewHolder=new ViewHolder(view);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    int position=viewHolder.getAdapterPosition();
                    listener.onItemClick(position);
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Blog.DataBean dataBean= (Blog.DataBean) list.get(position);
        holder.tv_nickname.setText("城北客运站徐公");
        holder.tv_title.setText(dataBean.getTitle());
        holder.tv_content.setText(dataBean.getSummary());
        holder.tv_desc.setText(dataBean.getDescrip());
        holder.iv_picture.setVisibility(View.GONE);
    }
}

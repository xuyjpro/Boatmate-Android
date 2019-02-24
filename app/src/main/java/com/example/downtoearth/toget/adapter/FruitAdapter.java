package com.example.downtoearth.toget.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.downtoearth.toget.R;
import com.example.downtoearth.toget.activity.FruitActivity;
import com.example.downtoearth.toget.bean.Fruit;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by DownToEarth on 2018/10/17.
 */

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder> {
    private List<Fruit> mFruitList;
    private Context mContext;
    static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView image;
        private TextView text;
        public ViewHolder(View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.image);
            text=itemView.findViewById(R.id.text);
        }
    }
    public FruitAdapter(List<Fruit> list){
        this.mFruitList=list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext== null){
            mContext=parent.getContext();
        }
        View view= LayoutInflater.from(mContext).inflate(R.layout
                .fruit_item,parent,false);
        final ViewHolder viewHolder=new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=viewHolder.getAdapterPosition();
                Log.e("postion",position+":"+mFruitList.size());
                Fruit fruit=mFruitList.get(position);
                Intent intent=new Intent(mContext, FruitActivity.class);
                intent.putExtra(FruitActivity.FRUIT_NAME,fruit.getName());
                intent.putExtra(FruitActivity.FRUIT_IMAGE_ID,fruit.getImageId());
                mContext.startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Fruit fruit=mFruitList.get(position);
        holder.text.setText(fruit.getName());
        Glide.with(mContext).load(fruit.getImageId()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        if(mFruitList==null){
            return 0;
        }
        return mFruitList.size();
    }


}

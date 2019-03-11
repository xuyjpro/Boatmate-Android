package com.example.downtoearth.toget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.downtoearth.toget.R;
import com.example.downtoearth.toget.impl.OnItemClickListener;

public class SchoolHelpAdapter extends RecyclerView.Adapter<SchoolHelpAdapter.ViewHolder> {
    private Context context;

    private OnItemClickListener listener;
    static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
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
                    viewHolder.getAdapterPosition();
                    listener.onItemClick();
                }

            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 6;
    }

}



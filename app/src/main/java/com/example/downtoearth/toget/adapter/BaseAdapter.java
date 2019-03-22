package com.example.downtoearth.toget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    List list;
    public BaseAdapter(){
        list=new ArrayList();
    }
    public void remove(int position){
        list.remove(position);
        notifyItemRemoved(position);
    }
    public void add(Object ob){
        list.add(ob);
        notifyItemInserted(list.size()-1);
    }


    public void addAll(List list,boolean isClear){
        if(isClear){
            this.list.clear();
            this.list.addAll(list);
            notifyDataSetChanged();
        }else{
            this.list.addAll(list);
            notifyItemRangeChanged(this.list.size()-list.size(),list.size());

        }
    }

    public void setList(List list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List getList() {
        return list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

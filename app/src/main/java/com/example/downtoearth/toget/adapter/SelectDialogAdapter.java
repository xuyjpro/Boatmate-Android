package com.example.downtoearth.toget.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.downtoearth.toget.R;
import com.example.downtoearth.toget.adapter.BaseAdapter;
import com.example.downtoearth.toget.impl.OnItemClickListener;
import com.example.downtoearth.toget.view.SelectDialog;

public class SelectDialogAdapter extends BaseAdapter<SelectDialogAdapter.ViewHolder> {

    private OnItemClickListener listener;
    static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tv;
        public ViewHolder(View itemView) {
            super(itemView);
            tv=itemView.findViewById(R.id.tv);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.select_dialog_item,null);
        final ViewHolder holder=new ViewHolder(view);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    int position=holder.getAdapterPosition();
                    listener.onItemClick(position);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SelectDialog.SelectItem item= (SelectDialog.SelectItem) list.get(position);
        holder.tv.setText(item.getItemName());
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}

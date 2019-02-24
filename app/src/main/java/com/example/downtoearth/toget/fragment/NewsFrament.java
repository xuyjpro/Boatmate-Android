package com.example.downtoearth.toget.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.downtoearth.toget.R;
import com.example.downtoearth.toget.adapter.FruitAdapter;
import com.example.downtoearth.toget.bean.Fruit;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DownToEarth on 2018/10/18.
 */

public class NewsFrament extends Fragment {
    private SmartRefreshLayout mSmartRefresh;
    private FruitAdapter mAdapter;
    private List<Fruit> mFruitList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=LayoutInflater.from(getContext()).inflate(R.layout.fragment_home,container,false);
        FloatingActionButton btnFloating=view.findViewById(R.id.btn_floating);
        btnFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Snackbar.make(v,"Data deleted!",Snackbar.LENGTH_SHORT)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getContext(),"You clicked Undo!",Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }).show();
            }
        });
        RecyclerView recyclerView=view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        mFruitList=new ArrayList<>();
        mAdapter=new FruitAdapter(mFruitList);
        recyclerView.setAdapter(mAdapter);
        mSmartRefresh=view.findViewById(R.id.smart_refresh);

        return view;
    }
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    break;
            }
            return false;
        }
    });


}

package com.example.downtoearth.toget.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.example.downtoearth.toget.R;

import java.text.SimpleDateFormat;

/**
 * Created by DownToEarth on 2018/10/19.
 */

public class ToolUtils {
    private static Context context;

    public static void setContext(Context c) {
        context = c;
    }

    public static String getString( String key){
        SharedPreferences sp=context.getSharedPreferences("boamate",Context.MODE_PRIVATE);

        return  sp.getString(key,"");
    }
    public static int getInt(String key){
        SharedPreferences sp=context.getSharedPreferences("boamate",Context.MODE_PRIVATE);

        return  sp.getInt(key,0);
    }
    public static void putString(String key,String value){
        SharedPreferences sp=context.getSharedPreferences("boamate",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString(key,value);
        editor.commit();
    }
    public static void putInt(String key,int value){
        SharedPreferences sp=context.getSharedPreferences("boamate",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putInt(key,value);
        editor.commit();
    }
    public static String getTime(long date) {
        long time=System.currentTimeMillis()-date;
        time=time/1000;
        if (time < 60) {
            return time+1 + "秒前";
        } else if (time < 60 * 60) {
            return time / 60 + "分钟前";
        }else if(time<60*60*24){
            return time/60/60+"小时前";
        }else if(time<60*60*24*30){
            return time/60/60/24+"天前";
        }else{
            return getDate(date,"yyyy-MM-dd");
        }
    }
    public static String getDate(long time,String patten){
        SimpleDateFormat format = new SimpleDateFormat(patten);
        return  format.format(time);
    }

    public static int px2dip(float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     *            （DisplayMetrics类中属性density）
     * @return
     */
    public static int dip2px( float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
    public static void loadImage(ImageView imageView,String url){

        //Glide.with(context).load(HttpUtils.DOWNLOAD_URL+url).error(R.mipmap.person).into(imageView);
    }
}

package com.example.downtoearth.toget.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.example.downtoearth.toget.R;
import com.example.downtoearth.toget.activity.LoginActivity;

import java.text.SimpleDateFormat;

/**
 * Created by DownToEarth on 2018/10/19.
 */

public class ToolUtils {


    private static int width;
    private static int height;
    public static String getString(Context context,String key){

        if(context!=null){
            SharedPreferences sp=context.getSharedPreferences("boamate",Context.MODE_PRIVATE);

            return  sp.getString(key,"");
        }
        return "";

    }
    public static int getInt(Context context,String key){
        SharedPreferences sp=context.getSharedPreferences("boamate",Context.MODE_PRIVATE);
        return  sp.getInt(key,0);
    }
    public static void putString(Context context,String key,String value){
        SharedPreferences sp=context.getSharedPreferences("boamate",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString(key,value);
        editor.commit();
    }
    public static void putInt(Context context,String key,int value){
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

    public static int px2dip(Context context,float pxValue) {
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
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
    public static int getWidth(Context context){

        if(width==0){
            Resources resources = context.getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            width = dm.widthPixels;
        }

        return width;
    }
    public static int getHeight(Context context){
        if(height==0){
            Resources resources = context.getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            height = dm.heightPixels;
        }

        return height;
    }
    public static void loadImage(Context context,ImageView imageView,String url){

        Glide.with(context).load(HttpUtils.DOWNLOAD_URL+url).into(imageView);
    }

    public  static  void downloadByWeb(Context context, String apkPath) {
        Uri uri = Uri.parse(apkPath);
        //String android.intent.action.VIEW 比较通用，会根据用户的数据类型打开相应的Activity。如:浏览器,电话,播放器,地图
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    public static int getVersionCode(Context mContext) {
        int versionCode = 0;
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }
    public static void doHttpError(Context context){
        if(context!=null){
            Toast.makeText(context,"服务器异常，请联系客服：15252478436",Toast.LENGTH_SHORT).show();
//            Intent intent=new Intent(context,LoginActivity.class);
//            context.startActivity(intent);
        }

    }
    public static String getAppKey(){
        return "48147ad8cbf1e43998ba7fd9";
    }
}

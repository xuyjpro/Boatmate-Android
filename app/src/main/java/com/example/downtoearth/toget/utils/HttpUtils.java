package com.example.downtoearth.toget.utils;

/**
 * Created by DownToEarth on 2018/10/17.
 */

public class HttpUtils {

 //   public static final String IP="23.95.216.81";
    public static final String IP = "192.168.1.5:8080";
    public static final String REGISTER = "http://" + IP + "/boamate/" + "register";
    public static final String LOGIN = "http://" + IP + "/boamate/" + "login";
    public static final String EDIT_INFO = "http://" + IP + "/boamate/" + "editUserInfo";
    public static final String GET_USER_INFO = "http://" + IP + "/boamate/" + "getUserInfo";
    public static final String DOWNLOAD_URL = "http://" + IP + "/boamate/download/fileLoad_load?fileName=";
    public static final String PUBLISH_DYNAMIC = "http://" + IP + "/boamate/" + "publishDynamic";
    public static final String DYNAMIC_LIST = "http://" + IP + "/boamate/" + "getDynamics";
    public static final  String PUBLISH_COMMENT="http://"+IP+"/boamate/"+"publishComment";
   public static final  String PUBLISH_SUB_COMMENT="http://"+IP+"/boamate/"+"publishSubComment";

    public static final String GET_COMMENTS="http://"+IP+"/boamate/"+"getComments";
    public static final String LIKE_CLICK="http://"+IP+"/boamate/"+"awesomeDynamic";
    public static final String DYNAMIC_DETAIL="http://"+IP+"/boamate/"+"dynamicDetail";
    public static final String COMMENT_DETAIL="http://"+IP+"/boamate/"+"commentDetail";
    public static final String GET_SUB_COMMENTS="http://"+IP+"/boamate/"+"getSubComments";
    public static final String DYNAMIC_DELETE="http://"+IP+"/boamate/"+"deleteDynamic";

}

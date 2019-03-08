package com.example.downtoearth.toget.utils;

/**
 * Created by DownToEarth on 2018/10/17.
 */

public class HttpUtils {

 //   public static final String IP="23.95.216.81";
    public static final String IP = "47.111.163.217:8080";
    public static final String REGISTER = "http://" + IP + "/boatmate/" + "register";
    public static final String LOGIN = "http://" + IP + "/boatmate/" + "login";
    public static final String EDIT_INFO = "http://" + IP + "/boatmate/" + "editUserInfo";
    public static final String GET_USER_INFO = "http://" + IP + "/boatmate/" + "getUserInfo";
    public static final String DOWNLOAD_URL = "http://" + IP + "/boatmate/download/fileLoad_load?fileName=";
    public static final String PUBLISH_DYNAMIC = "http://" + IP + "/boatmate/" + "publishDynamic";
    public static final String DYNAMIC_LIST = "http://" + IP + "/boatmate/" + "getDynamics";
    public static final  String PUBLISH_COMMENT="http://"+IP+"/boatmate/"+"publishComment";
   public static final  String PUBLISH_SUB_COMMENT="http://"+IP+"/boatmate/"+"publishSubComment";

    public static final String GET_COMMENTS="http://"+IP+"/boatmate/"+"getComments";
    public static final String LIKE_CLICK="http://"+IP+"/boatmate/"+"awesomeDynamic";
    public static final String AWESOME_COMMENT="http://"+IP+"/boatmate/"+"awesomeComment";
    public static final String DYNAMIC_DETAIL="http://"+IP+"/boatmate/"+"dynamicDetail";
    public static final String COMMENT_DETAIL="http://"+IP+"/boatmate/"+"commentDetail";
    public static final String GET_SUB_COMMENTS="http://"+IP+"/boatmate/"+"getSubComments";
    public static final String DYNAMIC_DELETE="http://"+IP+"/boatmate/"+"deleteDynamic";
    public static final String DELETE_COMMENT="http://"+IP+"/boatmate/"+"deleteComment";
    public static final String DELETE_SUB_COMMENT="http://"+IP+"/boatmate/"+"deleteSubComment";

}

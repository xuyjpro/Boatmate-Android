package com.example.downtoearth.toget.utils;

/**
 * Created by DownToEarth on 2018/10/17.
 */

public class HttpUtils {


 //   public static final String IP="23.95.216.81";
    private  static final String IP = "47.111.163.217:8080";
    private static final String PROJECT_URL="http://" + IP + "/boatmate/";

    /*
    * 版本控制*/
    

    public static final String GET_APP_VERSION=PROJECT_URL+"getAppVersion";



    public static final String REGISTER = PROJECT_URL + "register";
    public static final String LOGIN = PROJECT_URL + "login";
    public static final String EDIT_INFO = PROJECT_URL + "editUserInfo";
    public static final String GET_USER_INFO = PROJECT_URL + "getUserInfo";
    public static final String DOWNLOAD_URL = "http://" + IP + "/boatmate/download/fileLoad_load?fileName=";
    public static final String PUBLISH_DYNAMIC = PROJECT_URL + "publishDynamic";
    public static final String DYNAMIC_LIST = PROJECT_URL + "getDynamics";
    public static final  String PUBLISH_COMMENT=PROJECT_URL+"publishComment";
   public static final  String PUBLISH_SUB_COMMENT=PROJECT_URL+"publishSubComment";

    public static final String GET_COMMENTS=PROJECT_URL+"getComments";
    public static final String LIKE_CLICK=PROJECT_URL+"awesomeDynamic";
    public static final String AWESOME_COMMENT=PROJECT_URL+"awesomeComment";
    public static final String DYNAMIC_DETAIL=PROJECT_URL+"dynamicDetail";
    public static final String COMMENT_DETAIL=PROJECT_URL+"commentDetail";
    public static final String GET_SUB_COMMENTS=PROJECT_URL+"getSubComments";
    public static final String DYNAMIC_DELETE=PROJECT_URL+"deleteDynamic";
    public static final String DELETE_COMMENT=PROJECT_URL+"deleteComment";
    public static final String DELETE_SUB_COMMENT=PROJECT_URL+"deleteSubComment";


    /*
        校园帮帮
     */
    public static final String GET_SCHOOL_HELPS=PROJECT_URL+"getSchoolHelps";
    public static final String SCHOOL_HELPS_DETAIL=PROJECT_URL+"schoolHelpDetail";
    public static final String MODIFY_STATUS_HELP=PROJECT_URL+"modifyStatusHelp";
    public static final String PUBLISH_SCHOOL_HELP=PROJECT_URL+"publishSchoolHelp";
    public static final String DELETE_SCHOOL_HELP=PROJECT_URL+"deleteSchoolHelp";

    /*
    * 物品
    * */
    public static final String GET_STUFFS=PROJECT_URL+"getStuffs";
    public static final String PUBLISH_STUFF=PROJECT_URL+"publishStuff";
    public static final String GET_STUFF_DETAIL=PROJECT_URL+"stuffDetail";
    public static final String STUFF_DELETE=PROJECT_URL+"stuffDelete";

    public static final String PUBLISH_FEEDBACK=PROJECT_URL+"publishFeedback";

}

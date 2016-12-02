package com.qingchengfit.fitcoach;

import android.os.Environment;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/8/5 2015.
 */
public class Configs {

    public static final int TYPE_PRIVATE = 1;  //私教
    public static final int TYPE_GROUP = 2;    //团课
    public static final String[] STRINGS_WEEKDAY = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

    public static final String SIGN_PAUSE = "、";
    public static  String APP_ID ;
    public static  String SEPARATOR = ",";
    /**
     * http config
     */
//    public static boolean isDebug = true;
    public static boolean isDebug = false;
    public static String ServerIp = BuildConfig.DEBUG ? "http://dev.qingchengfit.cn:7777/" : "http://cloud.qingchengfit.cn/";
//        public static String ServerIp = isDebug ? "http://gravityccy.qingchengfit.cn" : "http://cloudtest.qingchengfit.cn/";
    public static String ServerPort = isDebug ? ":7777/" : "";
    public static String Server = ServerIp + ServerPort;
    public static final String ORDER_PRIVATE_URL = Server + "mobile/coach/privatelesson/list/";
    public static final String ORDER_GROUP_URL = Server + "mobile/coach/grouplesson/list/";
    public static String HOST_NAMESPACE_0 = "http://.qingchengfit.cn";
    public static String HOST_NAMESPACE_1 = "http://.qingchengfit.com";
    public static String PRIVATE_PRIVEIW = Configs.ServerIp + "fitness/redirect/user/private/";
    public static String GROUP_PRIVEIW = Configs.ServerIp+"fitness/redirect/user/group/";

    /**
     * 会员预览页面
     */
    public static String HOST_STUDENT_PREVIEW = "http://www.qingchengfit.cn";
    public static final String WECHAT_GUIDE = "mobile/weixin/guide/";

    //app名称
    public static String APPNAME = "QingChengCoach";
    //私有外部路径
    public static String ExternalPath = Environment.getExternalStorageDirectory().getPath() + "/" + APPNAME + "/";
    public static String ExternalCache = Environment.getExternalStorageDirectory().getPath() + "/" + APPNAME + "/Cache/";
    //Camera图片位置
    public static String CameraPic = Environment.getExternalStorageDirectory().getPath() + "/" + APPNAME + "/Cache/camera_tmp.jpg";
    public static String CameraCrop = Environment.getExternalStorageDirectory().getPath() + "/" + APPNAME + "/Cache/crop_tmp.jpg";
}

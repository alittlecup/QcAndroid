package com.qingchengfit.fitcoach.Utils;

/**
 * Created by peggy on 16/5/26.
 */

public class PhotoUtils {
    public static String getSmall(String url){
        if (!url.contains("!")){
            return url+"!small";
        }else return url;

    }
}

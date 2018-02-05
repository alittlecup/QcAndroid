package com.tencent.qcloud.timchat.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import com.tencent.qcloud.timchat.chatmodel.GroupInfo;
import com.tencent.qcloud.timchat.chatmodel.UserInfo;
import com.tencent.qcloud.timchat.event.MessageEvent;

/**
 * Created by fb on 2017/4/15.
 */

public class AppData {


    private static SharedPreferences sharedPreferences;
    public static final String defaultAvatar = "http://zoneke-img.b0.upaiyun.com/75656eb980b79e7748041f830332cc62.png!120x120";
    public static final String defaultGroupAvatar = "http://zoneke-img.b0.upaiyun.com/4ca8948c8e2cc5d0874d163001fa2267.png";

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(Configs.PREFRENCE_USERSIG, 0);
    }

    public static String getUSerSig(Context context){
        return getSharedPreferences(context).getString(Configs.VALUE_USERSIG, "");
    }

    public static void putUserSig(Context context , String userSig) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(Configs.VALUE_USERSIG, userSig);
        applyCompat(editor);
    }

    public static void putUserAvatar(Context context, String avatar){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(Configs.VALUE_AVATAR, avatar);
        applyCompat(editor);
    }

    //传入会话列表头像
    public static void putConversationAvatar(Context context, String key, String avatar){
        SharedPreferences.Editor editor =
            context.getSharedPreferences(Configs.PREFRENCE_CONVERSATION_AVATAR, 0).edit();
        editor.putString(key, avatar);
        applyCompat(editor);
    }

    public static String getConversationAvatar(Context context, String key){
        return context.getSharedPreferences(Configs.PREFRENCE_CONVERSATION_AVATAR, 0)
            .getString(key, "");
    }

    //传入会话列表名称
    public static void putConversationName(Context context, String key, String avatar){
        SharedPreferences.Editor editor =
            context.getSharedPreferences(Configs.PREFRENCE_CONVERSATION_NAME, 0).edit();
        editor.putString(key, avatar);
        applyCompat(editor);
    }

    public static String getConversationName(Context context, String key){
        return context.getSharedPreferences(Configs.PREFRENCE_CONVERSATION_NAME, 0)
            .getString(key, "");
    }

    //TODO 返回默认头像
    public static String getAvatar(Context context){
        return getSharedPreferences(context).getString(Configs.VALUE_AVATAR, "");
    }

    public static void putIdentify(Context context, String identify){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(Configs.VALUE_IDENTIFY, identify);
        applyCompat(editor);
    }

    public static String getIdentify(Context context){
        return getSharedPreferences(context).getString(Configs.VALUE_IDENTIFY, "");
    }

    private static void applyCompat(SharedPreferences.Editor editor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

    public static void clear(Context context){
        UserInfo.getInstance().setId(null);
        MessageEvent.getInstance().clear();
        GroupInfo.getInstance().clear();
        getSharedPreferences(context).edit().clear().apply();
    }

}

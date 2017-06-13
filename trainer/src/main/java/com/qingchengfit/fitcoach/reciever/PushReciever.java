package com.qingchengfit.fitcoach.reciever;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.PreferenceUtils;
import com.baidu.android.pushservice.PushMessageReceiver;
import com.google.gson.Gson;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.activity.Main2Activity;
import com.qingchengfit.fitcoach.activity.MainActivity;
import com.qingchengfit.fitcoach.activity.WebActivity;
import com.qingchengfit.fitcoach.bean.NewPushMsg;
import java.util.List;

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
 * Created by Paper on 15/8/27 2015.
 */
public class PushReciever extends PushMessageReceiver {

    /**
     * @param context
     * @param i     errorcode
     * @param s     appid
     * @param s1    userId
     * @param s2    channelId
     * @param s3    requestId
     */
    public static String BD_CHANNELID = "bd_channelid";
    public static String BD_USERLID = "bd_userid";

    @Override public void onBind(Context context, int i, String s, String s1, String s2, String s3) {
        LogUtil.e("error:" + i + "   " + s + "  " + s1 + "  channelid:" + s2 + "    " + s3);
        PreferenceUtils.setPrefString(context, BD_USERLID, s1);
        PreferenceUtils.setPrefString(context, BD_CHANNELID, s2);
    }

    @Override public void onUnbind(Context context, int i, String s) {

    }

    @Override public void onSetTags(Context context, int i, List<String> list, List<String> list1, String s) {

    }

    @Override public void onDelTags(Context context, int i, List<String> list, List<String> list1, String s) {

    }

    @Override public void onListTags(Context context, int i, List<String> list, String s) {

    }

    @Override public void onMessage(Context context, String s, String s1) {
        LogUtil.e(s + "  " + s1);
        RxBus.getBus().post(new NewPushMsg());
    }

    @Override public void onNotificationClicked(Context context, String s, String s1, String s2) {
        LogUtil.d("title:" + s + "   content:" + s1 + "   self:" + s2);

        if (!TextUtils.isEmpty(s2)) {
            PushBean bean = new Gson().fromJson(s2, PushBean.class);
            if (bean == null || TextUtils.isEmpty(bean.url)) {
                return;
            }
            if (bean.url.startsWith("http")) {
                if (App.gMainAlive) {
                    LogUtil.e("isAlive");
                    Intent toMain = new Intent(context, WebActivity.class);
                    toMain.putExtra("url", bean.url);
                    toMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(toMain);
                } else {
                    Intent toMain = new Intent(context, Main2Activity.class);
                    toMain.putExtra("url", bean.url);
                    toMain.putExtra(MainActivity.ACTION, Main2Activity.NOTIFICATION);
                    toMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(toMain);
                }
            } else {
                try {
                    Uri uri = Uri.parse(bean.url);
                    Intent tosb = new Intent(Intent.ACTION_VIEW, uri);
                    if (uri.getQueryParameterNames() != null) {
                        for (String key : uri.getQueryParameterNames()) {
                            tosb.putExtra(key, uri.getQueryParameter(key));
                        }
                    }
                    tosb.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(tosb);
                } catch (Exception e) {
                    Intent toMain = new Intent(context, Main2Activity.class);
                    toMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(toMain);
                }
            }
        }
    }

    @Override public void onNotificationArrived(Context context, String s, String s1, String s2) {
        LogUtil.d(" recieve title:" + s + "   content:" + s1 + "   self:" + s2);
        RxBus.getBus().post(new NewPushMsg());
    }
}

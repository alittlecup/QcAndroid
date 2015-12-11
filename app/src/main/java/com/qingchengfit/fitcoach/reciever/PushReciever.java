package com.qingchengfit.fitcoach.reciever;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.baidu.android.pushservice.PushMessageReceiver;
import com.google.gson.Gson;
import com.paper.paperbaselibrary.utils.LogUtil;
import com.paper.paperbaselibrary.utils.PreferenceUtils;
import com.qingchengfit.fitcoach.RxBus;
import com.qingchengfit.fitcoach.activity.WebActivity;
import com.qingchengfit.fitcoach.bean.NewPushMsg;
import com.qingchengfit.fitcoach.http.QcCloudClient;

import java.util.List;

import rx.schedulers.Schedulers;

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
     *
     * @param s     appid
     * @param s1    userId
     * @param s2    channelId
     * @param s3    requestId
     */
    public static String BD_CHANNELID = "bd_channelid";
    public static String BD_USERLID = "bd_userid";
    @Override
    public void onBind(Context context, int i, String s, String s1, String s2, String s3) {
        LogUtil.e(s + "  " + s1 + "    " + s2 + "    " + s3);
        PreferenceUtils.setPrefString(context, BD_USERLID, s1);
        PreferenceUtils.setPrefString(context, BD_CHANNELID, s2);
    }

    @Override
    public void onUnbind(Context context, int i, String s) {

    }

    @Override
    public void onSetTags(Context context, int i, List<String> list, List<String> list1, String s) {

    }

    @Override
    public void onDelTags(Context context, int i, List<String> list, List<String> list1, String s) {

    }

    @Override
    public void onListTags(Context context, int i, List<String> list, String s) {

    }

    @Override
    public void onMessage(Context context, String s, String s1) {
        LogUtil.e(s + "  " + s1);
        RxBus.getBus().post(new NewPushMsg());
    }

    @Override
    public void onNotificationClicked(Context context, String s, String s1, String s2) {
        LogUtil.d("title:" + s + "   content:" + s1 + "   self:" + s2);

        if (!TextUtils.isEmpty(s2)){
            PushBean bean = new Gson().fromJson(s2, PushBean.class);
            QcCloudClient.getApi().postApi.qcClearOneNotification(bean.notification_id).subscribeOn(Schedulers.io()).subscribe();
            Intent toMain = new Intent(context, WebActivity.class);
            toMain.putExtra("url",bean.url);
            toMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(toMain);
        }
//        toMain.putExtra(MainActivity.ACTION, MainActivity.NOTIFICATION);
//        if (s2 == null)
//            s2 = "";
//        toMain.putExtra("content", s2);
//        toMain.putExtra("url",)


    }

    @Override
    public void onNotificationArrived(Context context, String s, String s1, String s2) {
        LogUtil.d(" recieve title:" + s + "   content:" + s1 + "   self:" + s2);
        RxBus.getBus().post(new NewPushMsg());
    }


}

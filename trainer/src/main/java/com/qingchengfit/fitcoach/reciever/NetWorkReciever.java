package com.qingchengfit.fitcoach.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.events.EventNetWorkError;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.bean.NetworkBean;

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
 * Created by Paper on 15/11/13 2015.
 */
public class NetWorkReciever extends BroadcastReceiver {

    private NetworkInfo info;

    @Override public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            LogUtil.e("网络状态已经改变");
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            info = connectivityManager.getActiveNetworkInfo();
            if (info != null && info.isAvailable()) {
                String name = info.getTypeName();
                LogUtil.e("当前网络名称：" + name);
                RxBus.getBus().post(new NetworkBean(true));
            } else {
                RxBus.getBus().post(new NetworkBean(false));
                RxBus.getBus().post(new EventNetWorkError());
                ToastUtils.show("没有可用网络");
            }
        }
    }
}

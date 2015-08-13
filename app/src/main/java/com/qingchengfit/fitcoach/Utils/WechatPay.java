package com.qingchengfit.fitcoach.Utils;

import android.content.Context;
import android.util.Log;

import com.paper.paperbaselibrary.utils.Hash;
import com.paper.paperbaselibrary.utils.LogUtil;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.LinkedList;
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
 * Created by Paper on 15/8/13 2015.
 */
public class WechatPay {

    //appid
    //请同时修改  androidmanifest.xml里面，.PayActivityd里的属性<data android:scheme="wxb4ba3c02aa476ea1"/>为新设置的appid
    public static final String APP_ID = "wxf2f565574a968187";
    //商户号
    public static final String MCH_ID = "1233848001";
    //  API密钥，在商户平台设置
    public static final String API_KEY = "412fde4e9c2e2bb619514ecea142e449";

    public static void pay(Context context) {
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);
        PayReq req = new PayReq();
        msgApi.registerApp(APP_ID);
        req.appId = APP_ID;
        req.partnerId = MCH_ID;
        req.prepayId = "prepay_id";
        req.packageValue = "Sign=WXPay";
        req.nonceStr = "1231";
        req.timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

        req.sign = genAppSign(signParams);

        LogUtil.e("sign\n" + req.sign + "\n\n");


        Log.e("orion", signParams.toString());
        msgApi.registerApp(APP_ID);
        msgApi.sendReq(req);
    }

    private static String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(API_KEY);

        String appSign = Hash.MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        Log.e("orion", appSign);
        return appSign;
    }
}

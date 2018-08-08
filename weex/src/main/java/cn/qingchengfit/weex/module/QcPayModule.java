package cn.qingchengfit.weex.module;

import android.net.Uri;
import android.util.Log;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.events.EventNativePay;
import cn.qingchengfit.events.EventRePay;
import cn.qingchengfit.router.QCResult;
import cn.qingchengfit.router.qc.IQcRouteCallback;
import cn.qingchengfit.router.qc.QcRouteUtil;
import cn.qingchengfit.router.qc.RouteOptions;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.views.fragments.WebFragment;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import java.util.HashMap;
import java.util.Map;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static java.security.AccessController.getContext;

public class QcPayModule extends WXSDKEngine.DestroyableModule {
  Subscription subscribe;
  JSCallback successCallback;
  JSCallback createOrderCallback;

  @JSMethod
  public void pay(String json, final JSCallback successCallback, JSCallback createOrderCallback) {
    JSONObject jsonObject = JSON.parseObject(json);

    final Map<String, Object> params = new HashMap<>();
    params.put("price", jsonObject.getString("price"));
    params.put("out_trade_no", jsonObject.getString("out_trade_no"));
    params.put("qrCodeUrl", jsonObject.getString("qrCodeUrl"));
    params.put("brand_id", jsonObject.getString("brand_id"));
    params.put("shop_id", jsonObject.getString("shop_id"));
    params.put("channel", jsonObject.getString("channel"));
    params.put("moduleName", "qcBase");
    params.put("actionName", "/web/repay");
    String channel = jsonObject.getString("channel");
    String type = "";
    if (channel.contains("WEIXIN")) {
      type = "微信";
    } else {
      type = "支付宝";
    }
    QcRouteUtil.setRouteOptions(new RouteOptions("checkout").setActionName("/checkout/pay/params")
        .addParam("type", type)
        .addParam("params", params)).callAsync(new IQcRouteCallback() {
      @Override public void onResult(QCResult qcResult) {
        LogUtil.d(qcResult.toString());
        if (qcResult.isSuccess()) {
          QcPayModule.this.successCallback.invoke(new Object());
        }
      }
    });

    if (this.createOrderCallback == null) {
      this.createOrderCallback = createOrderCallback;
      subscribe = RxBus.getBus()
          .register(EventNativePay.class)
          .observeOn(AndroidSchedulers.mainThread())
          .onBackpressureBuffer()
          .subscribeOn(Schedulers.io())
          .subscribe(new Action1<EventNativePay>() {
            @Override public void call(EventNativePay eventNativePay) {
              Object channel = eventNativePay.getParams().get("channel");
              if (channel != null) {
                JSONObject jsonObject1 = new JSONObject(eventNativePay.getParams());
                QcPayModule.this.createOrderCallback.invoke(jsonObject1);
              }
            }
          });
    }
    this.createOrderCallback = createOrderCallback;
    this.successCallback = successCallback;
  }

  @Override public void destroy() {
    subscribe.unsubscribe();
  }

  @JSMethod
  public void createOrder(String json, JSCallback successCallback, JSCallback createOrderCallback) {
    JSONObject jsonObject = JSON.parseObject(json);
    Map<String, Object> params = new HashMap<>();
    params.put("out_trade_no", jsonObject.getString("out_trade_no"));
    params.put("pay_trade_no", jsonObject.getString("pay_trade_no"));
    RxBus.getBus().post(new EventRePay(params));

    this.createOrderCallback = createOrderCallback;
    this.successCallback = successCallback;
  }
}

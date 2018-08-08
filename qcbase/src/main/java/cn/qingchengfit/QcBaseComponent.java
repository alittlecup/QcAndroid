package cn.qingchengfit;

import android.util.Log;
import cn.qingchengfit.events.EventNativePay;
import cn.qingchengfit.events.EventRePay;
import cn.qingchengfit.router.IComponent;
import cn.qingchengfit.router.QC;
import cn.qingchengfit.router.QCResult;
import java.util.HashMap;
import java.util.Map;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class QcBaseComponent implements IComponent {
  @Override public String getName() {
    return "qcBase";
  }

  @Override public boolean onCall(QC qc) {
    String actionName = qc.getActionName();
    switch (actionName) {
      case "/web/repay":
        Map<String, Object> params = qc.getParams();
        Log.d("TAG", "onCall: "+params.toString());
        String channel = (String) params.get("channel");
        if (channel.equals("ALIPAY_QRCODE")) {
          params.put("channel", "ALIPAY_BARCODE");
        } else if (channel.equals("WEIXIN_QRCODE")) {
          params.put("channel", "WEIXIN_BARCODE");
        }
        RxBus.getBus().post(new EventNativePay(params));
        RxBus.getBus()
            .register(EventRePay.class)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<EventRePay>() {
              @Override public void call(EventRePay eventRePay) {
                Log.d("TAG", "call: "+eventRePay.getParams().toString());
                Map<String, Object> params1 = eventRePay.getParams();
                QC.sendQCResult(qc.getCallId(), QCResult.success(params1));
              }
            });
        return true;
    }
    return false;
  }
}

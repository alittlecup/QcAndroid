package cn.qingchengfit;

import cn.qingchengfit.events.EventNativePay;
import cn.qingchengfit.events.EventRePay;
import cn.qingchengfit.router.IComponent;
import cn.qingchengfit.router.QC;
import cn.qingchengfit.router.QCResult;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.Map;
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

        String params1 = (String) params.get("params");
        JsonObject jsonObject = new Gson().fromJson(params1, JsonObject.class);
        String channel = jsonObject.get("channel").getAsString();
        Object type = params.get("type");
        if(type instanceof String){
          channel= (String) type;
        }
        params.clear();
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
                Map<String, Object> params1 = eventRePay.getParams();
                QC.sendQCResult(qc.getCallId(), QCResult.success(params1));
              }
            });
        return true;
    }
    return false;
  }
}

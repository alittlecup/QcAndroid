package cn.qingchengfit.checkout.component;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import cn.qingchengfit.checkout.repository.ICheckoutModel;
import cn.qingchengfit.recruit.item.ResumeTitleItem;
import cn.qingchengfit.router.IComponent;
import cn.qingchengfit.router.QC;
import cn.qingchengfit.router.QCResult;
import cn.qingchengfit.saasbase.utils.RouterUtils;
import cn.qingchengfit.saascommon.utils.RouteUtil;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.CrashUtils;
import cn.qingchengfit.utils.LogUtil;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CheckoutComponent implements IComponent {

  public void setCheckoutModel(ICheckoutModel checkoutModel) {
    this.checkoutModel = checkoutModel;
  }

  ICheckoutModel checkoutModel;

  @Override public String getName() {
    return "checkout";
  }

  @Override public boolean onCall(QC qc) {
    String actionName = qc.getActionName();
    switch (actionName) {
      case "/checkout/pay":
        Map<String, Object> params1 = qc.getParams();
        Bundle bundle = new Bundle();
        bundle.putParcelable("orderData", (Parcelable) params1.get("orderData"));
        bundle.putString("type", (String) params1.get("type"));
        RouteUtil.routeTo(qc.getContext(), getName(), qc.getActionName(), bundle);
        QC.sendQCResult(qc.getCallId(), QCResult.success());
        return false;
      case "reOrder":
        Map<String, Object> params = qc.getParams();
        String prices = (String) params.get("prices");
        String channel = (String) params.get("channel");
        Log.d("TAG", "onCall: " + params);

        Flowable.just(1).delay(200, TimeUnit.SECONDS).subscribe(new Consumer<Integer>() {
          @Override public void accept(Integer integer) throws Exception {
            Map<String, Object> params = new HashMap<>();
            params.put("orderNumber", "123123123");
            params.put("pollingNumber", "123123123");
            QC.sendQCResult(qc.getCallId(), QCResult.success(params));
          }
        });
        return true;
    }
    return false;
  }
}


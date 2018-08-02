package com.qingchengfit.fitcoach.fragment.checkout;

import android.os.Bundle;
import android.os.Parcelable;
import cn.qingchengfit.checkout.repository.ICheckoutModel;
import cn.qingchengfit.router.IComponent;
import cn.qingchengfit.router.QC;
import cn.qingchengfit.router.QCResult;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.saascommon.utils.RouteUtil;
import java.util.HashMap;
import java.util.Map;

public  class CheckoutComponent implements IComponent {

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
        String channel = (String) params.get("channel");
        if (channel.equals("ALIPAY_QRCODE")) {
          params.put("channel", "ALIPAY_BARCODE");
        } else if (channel.equals("WEIXIN_QRCODE")) {
          params.put("channel", "WEIXIN_BARCODE");
        }
        ICheckoutModel checkoutModel = CheckoutModel.getInstance();
        checkoutModel
            .qcPostCashierOrder(params)
            .compose(RxHelper.schedulersTransformerFlow())
            .subscribe(cashierBeanQcDataResponse -> {
              Map<String, Object> map = new HashMap<>();
              map.put("cashierBean", cashierBeanQcDataResponse.data);
              QC.sendQCResult(qc.getCallId(), QCResult.success(map));
            }, throwable -> QC.sendQCResult(qc.getCallId(),
                QCResult.error(throwable.getMessage())));
        return true;
      case "/checkout/home":
        RouteUtil.routeTo(qc.getContext(), getName(), qc.getActionName(), null);
        QC.sendQCResult(qc.getCallId(), QCResult.success());
        return false;
    }
    return false;
  }

}


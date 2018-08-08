package cn.qingchengfit.component;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import cn.qingchengfit.checkout.repository.CheckoutModel;
import cn.qingchengfit.checkout.repository.ICheckoutModel;
import cn.qingchengfit.router.IComponent;
import cn.qingchengfit.router.QC;
import cn.qingchengfit.router.QCResult;
import cn.qingchengfit.saascommon.bean.CashierBean;
import cn.qingchengfit.saascommon.bean.CashierBeanWrapper;
import cn.qingchengfit.saascommon.bean.ScanRepayInfo;
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
        bundle.putString("qcCallId", qc.getCallId());
        RouteUtil.routeTo(qc.getContext(), getName(), qc.getActionName(), bundle);
        return true;
      case "/checkout/pay/params":
        Map<String, Object> params2 = qc.getParams();
        Map<String,Object> paramInfo = (Map<String, Object>) params2.get("params");
        CashierBean cashierBean=new CashierBean();
        cashierBean.setOut_trade_no((String) paramInfo.get("out_trade_no"));
        cashierBean.setUrl(Uri.decode((String) paramInfo.get("qrCodeUrl")));
        CashierBeanWrapper wrapper=new CashierBeanWrapper(cashierBean);
        wrapper.setPrices((String) paramInfo.get("price"));
        ScanRepayInfo info=new ScanRepayInfo();
        info.setModuleName((String) paramInfo.get("moduleName"));
        info.setActionName((String) paramInfo.get("actionName"));
        Map<String,String> scanParams=new HashMap<>();
        scanParams.put("channel", (String) paramInfo.get("channel"));
        info.setParams(scanParams);
        wrapper.setInfo(info);
        Bundle bundle2 = new Bundle();
        bundle2.putParcelable("orderData", wrapper);
        bundle2.putString("type", (String) params2.get("type"));
        bundle2.putString("qcCallId", qc.getCallId());
        bundle2.putString("brand_id", (String) paramInfo.get("brand_id"));
        bundle2.putString("shop_id", (String) paramInfo.get("shop_id"));
        RouteUtil.routeTo(qc.getContext(), getName(), "/checkout/pay", bundle2);
        return true;
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
      default:
        RouteUtil.routeTo(qc.getContext(), getName(), qc.getActionName(), null);
        QC.sendQCResult(qc.getCallId(), QCResult.success());
        return false;
    }
  }

}


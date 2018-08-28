package cn.qingchengfit.checkout.component;

import android.net.Uri;
import android.os.Bundle;
import cn.qingchengfit.checkout.repository.ICheckoutModel;
import cn.qingchengfit.model.ComponentModuleManager;
import cn.qingchengfit.router.IComponent;
import cn.qingchengfit.router.QC;
import cn.qingchengfit.router.QCResult;
import cn.qingchengfit.checkout.bean.CashierBeanWrapper;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.saascommon.utils.RouteUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;

/**
 * 修改调起支付二维码界面的数据传输方式为json
 * {
 * price:"",价格
 * type:""(WEIIN_QRCODE|ALIPAY_QRCODE) 支付方式
 * bean:{
 * pay_trade_no:"",订单号
 * out_trade_no:"",轮询单号
 * url:""二维码内容
 * },
 * info:{
 * moduleName:"",二次下单调用的模块
 * actionName:"",二次下单调用的action
 * params:{},二次下单需要的信息
 *
 * }
 *
 * }
 */
public class CheckoutComponent implements IComponent {

  @Override public String getName() {
    return "checkout";
  }

  @Override public boolean onCall(QC qc) {
    String actionName = qc.getActionName();
    switch (actionName) {
      case "/checkout/pay":
        Map<String, Object> params1 = qc.getParams();
        Bundle bundle = new Bundle();
        String data = (String) params1.get("data");
        CashierBeanWrapper cashierBeanWrapper = new Gson().fromJson(data, CashierBeanWrapper.class);
        bundle.putParcelable("orderData", cashierBeanWrapper);
        bundle.putString("type", cashierBeanWrapper.getType());
        bundle.putString("qcCallId", qc.getCallId());
        RouteUtil.routeTo(qc.getContext(), getName(), qc.getActionName(), bundle);
        return true;
      case "reOrder":
        Map<String, Object> params = qc.getParams();
        String scanInfoParams = (String) params.get("params");
        JsonObject jsonObject = new Gson().fromJson(scanInfoParams, JsonObject.class);
        String channel = jsonObject.get("channel").getAsString();
        String price = jsonObject.get("price").getAsString();
        params.clear();
        if (channel.equals("ALIPAY_QRCODE")) {
          params.put("channel", "ALIPAY_BARCODE");
        } else if (channel.equals("WEIXIN_QRCODE")) {
          params.put("channel", "WEIXIN_BARCODE");
        }

        params.put("price", price);
        ICheckoutModel checkoutModel = ComponentModuleManager.get(ICheckoutModel.class);
        checkoutModel.qcPostCashierOrder(params)
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


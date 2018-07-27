package com.qingchengfit.fitcoach.fragment.card;

import cn.qingchengfit.router.IComponent;
import cn.qingchengfit.router.QC;
import cn.qingchengfit.router.QCResult;
import cn.qingchengfit.saasbase.cards.network.body.CardBuyBody;
import cn.qingchengfit.saascommon.bean.CashierBean;
import cn.qingchengfit.saascommon.utils.RouteUtil;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;

public class CardComponent implements IComponent {

  @Override public String getName() {
    return "card";
  }

  @Override public boolean onCall(QC qc) {
    String actionName = qc.getActionName();
    switch (actionName) {
      case "/cardtpl/nonew":
        RouteUtil.routeTo(qc.getContext(), getName(), actionName, null);
        QC.sendQCResult(qc.getCallId(),QCResult.success());
        return false;
      case "/list/nobalance":
        RouteUtil.routeTo(qc.getContext(), getName(), actionName, null);
        QC.sendQCResult(qc.getCallId(),QCResult.success());
        return false;
      case "/repay/newcard":
        CardModel cardModel = CardModel.getInstance();
        Map<String, Object> params = qc.getParams();
        String json = (String) params.get("json");
        CardBuyBody cardBuyBody = new Gson().fromJson(json, CardBuyBody.class);
        String type = (String) params.get("type");
        switch (type) {
          case "支付宝":
            cardBuyBody.setCharge_type(14);
            break;
          case "微信":
            cardBuyBody.setCharge_type(13);
            break;
        }
        cardModel.buyCard(cardBuyBody)
            .onBackpressureBuffer()
            .compose(cn.qingchengfit.saascommon.network.RxHelper.schedulersTransformer())
            .subscribe(jsonObjectQcDataResponse -> {
              CashierBean cashierBean =
                  new Gson().fromJson(jsonObjectQcDataResponse.data.toString(), CashierBean.class);
              Map<String, Object> map = new HashMap<>();
              map.put("orderNumber", cashierBean.getOut_trade_no());
              map.put("pollingNumber", cashierBean.getPay_trade_no());
              QC.sendQCResult(qc.getCallId(), QCResult.success(map));
            }, throwable -> QC.sendQCResult(qc.getCallId(),
                QCResult.error(throwable.getMessage())));
        return true;
      case "/repay/balance":
        CardModel cardRepository = CardModel.getInstance();
        Map<String, Object> info = qc.getParams();
        String cardId = (String) info.get("cardId");
        String chargeBody = (String) info.get("chargeBody");
        CardBuyBody body = new Gson().fromJson(chargeBody, CardBuyBody.class);
        String baType = (String) info.get("type");
        switch (baType) {
          case "支付宝":
            body.setCharge_type(14);
            break;
          case "微信":
            body.setCharge_type(13);
            break;
        }
        cardRepository.qcChargeCard(cardId, body)
            .onBackpressureBuffer()
            .compose(cn.qingchengfit.saascommon.network.RxHelper.schedulersTransformer())
            .subscribe(jsonObjectQcDataResponse -> {
              CashierBean cashierBean =
                  new Gson().fromJson(jsonObjectQcDataResponse.data.toString(), CashierBean.class);
              Map<String, Object> map = new HashMap<>();
              map.put("orderNumber", cashierBean.getOut_trade_no());
              map.put("pollingNumber", cashierBean.getPay_trade_no());
              QC.sendQCResult(qc.getCallId(), QCResult.success(map));
            }, throwable -> QC.sendQCResult(qc.getCallId(),
                QCResult.error(throwable.getMessage())));
        return true;
    }
    return false;
  }
}

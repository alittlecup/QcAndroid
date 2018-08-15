package cn.qingchengfit.card.component;

import android.os.Bundle;
import cn.qingchengfit.router.IComponent;
import cn.qingchengfit.router.QC;
import cn.qingchengfit.router.QCResult;
import cn.qingchengfit.saasbase.cards.network.body.CardBuyBody;
import cn.qingchengfit.saasbase.repository.ICardModel;
import cn.qingchengfit.saascommon.network.ComponentModuleManager;
import cn.qingchengfit.saascommon.utils.RouteUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
        Bundle bundle = new Bundle();
        bundle.putString("qcCallId", qc.getCallId());
        RouteUtil.routeTo(qc.getContext(), getName(), actionName, bundle);
        return true;
      case "/list/nobalance":
        Bundle bundle1 = new Bundle();
        bundle1.putString("qcCallId", qc.getCallId());
        RouteUtil.routeTo(qc.getContext(), getName(), actionName, bundle1);
        return true;
      case "/repay/newcard":
        ICardModel cardModel = ComponentModuleManager.get(ICardModel.class);
        Map<String, Object> params = qc.getParams();
        String json = (String) params.get("params");
        CardBuyBody cardBuyBody = new Gson().fromJson(json, CardBuyBody.class);
        String type = (String) params.get("type");
        switch (type) {
          case "ALIPAY_QRCODE":
            cardBuyBody.setCharge_type(14);
            break;
          case "WEIXIN_QRCODE":
            cardBuyBody.setCharge_type(13);
            break;
        }
        cardModel.buyCardFromCheckout(cardBuyBody)
            .onBackpressureBuffer()
            .compose(cn.qingchengfit.saascommon.network.RxHelper.schedulersTransformer())
            .subscribe(jsonObjectQcDataResponse -> {
              Map<String, Object> map = new HashMap<>();
              map.put("cashierBean", jsonObjectQcDataResponse.data.toString());
              QC.sendQCResult(qc.getCallId(), QCResult.success(map));
            }, throwable -> QC.sendQCResult(qc.getCallId(),
                QCResult.error(throwable.getMessage())));
        return true;
      case "/repay/balance":
        ICardModel cardRepository = ComponentModuleManager.get(ICardModel.class);
        Map<String, Object> info = qc.getParams();
        String params1 = (String) info.get("params");
        JsonObject jsonObject = new Gson().fromJson(params1, JsonObject.class);
        String cardId = jsonObject.get("cardId").getAsString();
        String chargeBody = jsonObject.get("chargeBody").getAsString();
        CardBuyBody body = new Gson().fromJson(chargeBody, CardBuyBody.class);
        String baType = (String) info.get("type");
        switch (baType) {
          case "ALIPAY_QRCODE":
            body.setCharge_type(14);
            break;
          case "WEIXIN_QRCODE":
            body.setCharge_type(13);
            break;
        }
        cardRepository.qcChargeCardFromCheckout(cardId, body)
            .onBackpressureBuffer()
            .compose(cn.qingchengfit.saascommon.network.RxHelper.schedulersTransformer())
            .subscribe(jsonObjectQcDataResponse -> {
              Map<String, Object> map = new HashMap<>();
              map.put("cashierBean", jsonObjectQcDataResponse.data.toString());
              QC.sendQCResult(qc.getCallId(), QCResult.success(map));
            }, throwable -> QC.sendQCResult(qc.getCallId(),
                QCResult.error(throwable.getMessage())));
        return true;
    }
    return false;
  }
}

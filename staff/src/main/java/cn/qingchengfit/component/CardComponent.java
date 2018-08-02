package cn.qingchengfit.component;

import android.os.Bundle;
import cn.qingchengfit.model.CardModel;
import cn.qingchengfit.network.RxHelper;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.router.IComponent;
import cn.qingchengfit.router.QC;
import cn.qingchengfit.router.QCResult;
import cn.qingchengfit.saasbase.cards.network.body.CardBuyBody;
import cn.qingchengfit.saascommon.bean.CashierBean;
import cn.qingchengfit.saascommon.utils.RouteUtil;
import cn.qingchengfit.subscribes.NetSubscribe;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dagger.Component;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import rx.functions.Action1;

public class CardComponent implements IComponent {

  @Override public String getName() {
    return "card";
  }

  @Override public boolean onCall(QC qc) {
    String actionName = qc.getActionName();
    switch (actionName) {
      case "/cardtpl/nonew":
        Object qcCallId = qc.getParams().get("qcCallId");
        Bundle bundle=null;
        if(qcCallId!=null){
          bundle=new Bundle();
          bundle.putString("qcCallId", (String) qcCallId);
        }
        RouteUtil.routeTo(qc.getContext(), getName(), actionName, bundle);
        return true;
      case "/list/nobalance":
        Object qcCallId1 = qc.getParams().get("qcCallId");
        Bundle bundle1=null;
        if(qcCallId1!=null){
          bundle1=new Bundle();
          bundle1.putString("qcCallId", (String) qcCallId1);
        }
        RouteUtil.routeTo(qc.getContext(), getName(), actionName, bundle1);
        return true;
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
              map.put("cashierBean",cashierBean);
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
              map.put("cashierBean",cashierBean);
              QC.sendQCResult(qc.getCallId(), QCResult.success(map));
            }, throwable -> QC.sendQCResult(qc.getCallId(),
                QCResult.error(throwable.getMessage())));
        return true;
    }
    return false;
  }
}

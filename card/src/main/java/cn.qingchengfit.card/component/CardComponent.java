package cn.qingchengfit.card.component;

import android.os.Bundle;
import cn.qingchengfit.card.network.CardApi;
import cn.qingchengfit.card.view.student.SimpleCardListFragment;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.router.IComponent;
import cn.qingchengfit.router.QC;
import cn.qingchengfit.router.QCResult;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.saasbase.cards.network.body.CardBuyBody;
import cn.qingchengfit.saasbase.cards.network.response.CardTplListWrap;
import cn.qingchengfit.saasbase.repository.ICardModel;
import cn.qingchengfit.model.ComponentModuleManager;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.saascommon.utils.RouteUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Retrofit;
import rx.Observable;

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
      case "/load/cardtpl":
        ICardModel iCardModel = ComponentModuleManager.get(ICardModel.class);
        Map<String, Object> params2 = qc.getParams();
        String cardType = (String) params2.get("type");
        String isEnable = (String) params2.get("isEnable");
        String staff_id = (String) params2.get("staff_id");
        Object params3 = params2.get("params");
        Observable<QcDataResponse<CardTplListWrap>> qcDataResponseObservable = null;
        if (iCardModel == null) {
          Retrofit retrofit = ComponentModuleManager.get(Retrofit.class);
          CardApi cardApi = retrofit.create(CardApi.class);
          qcDataResponseObservable =
              cardApi.qcGetCardTpls(staff_id, (HashMap<String, Object>) params3, cardType,
                  isEnable);
        } else {
          qcDataResponseObservable = iCardModel.qcGetCardTpls(cardType, isEnable);
        }
        qcDataResponseObservable.onBackpressureBuffer()
            .compose(RxHelper.schedulersTransformer())
            .subscribe(response -> {
              if (response.status == 200) {
                List<CardTpl> card_tpls1 = response.data.card_tpls;
                Gson gson = new Gson();
                JsonElement element = gson.toJsonTree(card_tpls1, new TypeToken<List<CardTpl>>() {
                }.getType());
                if (!element.isJsonArray()) {
                  QC.sendQCResult(qc.getCallId(), QCResult.error("not json"));
                }
                QC.sendQCResult(qc.getCallId(), QCResult.success("cardtpls", element.toString()));
              } else {
                QC.sendQCResult(qc.getCallId(), QCResult.error(response.msg));
              }
            }, throwable -> QC.sendQCResult(qc.getCallId(),
                QCResult.error(throwable.getMessage())));
        return true;
      case "/card/fragment":
        QC.sendQCResult(qc.getCallId(), QCResult.success("fragment", new SimpleCardListFragment()));
        return false;
    }

    return false;
  }
}

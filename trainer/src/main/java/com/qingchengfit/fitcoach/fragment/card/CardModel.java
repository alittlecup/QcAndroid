package com.qingchengfit.fitcoach.fragment.card;

import cn.qingchengfit.card.network.CardApi;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.SellerWrapper;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.cards.bean.BalanceCount;
import cn.qingchengfit.saasbase.cards.bean.DayOffs;
import cn.qingchengfit.saasbase.cards.bean.QcResponseRealcardHistory;
import cn.qingchengfit.saasbase.cards.bean.UUIDModel;
import cn.qingchengfit.saasbase.cards.network.body.AddDayOffBody;
import cn.qingchengfit.saasbase.cards.network.body.AheadOffDayBody;
import cn.qingchengfit.saasbase.cards.network.body.CardBalanceNotifyBody;
import cn.qingchengfit.saasbase.cards.network.body.CardBuyBody;
import cn.qingchengfit.saasbase.cards.network.body.CardtplBody;
import cn.qingchengfit.saasbase.cards.network.body.ChargeBody;
import cn.qingchengfit.saasbase.cards.network.body.OptionBody;
import cn.qingchengfit.saasbase.cards.network.body.ShopsBody;
import cn.qingchengfit.saasbase.cards.network.body.UpdateCardValidBody;
import cn.qingchengfit.saasbase.cards.network.response.BalanceConfigs;
import cn.qingchengfit.saasbase.cards.network.response.CardListWrap;
import cn.qingchengfit.saasbase.cards.network.response.CardTplListWrap;
import cn.qingchengfit.saasbase.cards.network.response.CardTplOptionListWrap;
import cn.qingchengfit.saasbase.cards.network.response.CardTplWrapper;
import cn.qingchengfit.saasbase.cards.network.response.CardWrap;
import cn.qingchengfit.saasbase.cards.network.response.NotityIsOpenConfigs;
import cn.qingchengfit.saasbase.cards.network.response.Shops;
import cn.qingchengfit.saasbase.repository.ICardModel;
import cn.qingchengfit.saasbase.student.network.body.StudentListWrapper;
import com.google.gson.JsonObject;
import java.util.HashMap;
import javax.inject.Inject;
import rx.Observable;

public class CardModel implements ICardModel {

  CardTrainerApi posApi;
  @Inject GymWrapper gymWrapper;
  @Inject LoginStatus loginStatus;

  public static CardModel getInstance() {
    return INSTANCE;
  }

  private static CardModel INSTANCE;

  @Inject public CardModel(QcRestRepository repository) {
    posApi = repository.createGetApi(CardTrainerApi.class);
    INSTANCE = this;
  }

  @Override
  public Observable<QcDataResponse<CardTplListWrap>> qcGetCardTpls(String type, String isEnable) {
    return posApi.qcGetCardTpls(loginStatus.staff_id(), gymWrapper.getParams(), type, isEnable);
  }

  @Override public Observable<QcDataResponse<CardTplListWrap>> qcGetCardTplsPermission(
      HashMap<String, Object> params) {
    return null;
  }

  @Override public Observable<QcDataResponse<CardTplWrapper>> qcGetCardTplsDetail(String cardid) {
    return null;
  }

  @Override
  public Observable<QcDataResponse<CardTplOptionListWrap>> qcGetOptions(String cardtps_id) {
    return posApi.qcGetOptions(loginStatus.staff_id(), cardtps_id, gymWrapper.getParams());
  }

  @Override
  public Observable<QcDataResponse<CardTplListWrap>> qcGetCardFilterTpls(boolean is_active) {
    return null;
  }

  @Override public Observable<QcDataResponse<CardWrap>> qcGetCardDetail(String card_id) {
    return null;
  }

  @Override public Observable<QcDataResponse<SellerWrapper>> qcGetDefineSeller(String card_id) {
    return null;
  }

  @Override public Observable<QcDataResponse> qcCreateCardtpl(CardtplBody body) {
    return null;
  }

  @Override
  public Observable<QcDataResponse> qcUpdateCardtpl(String card_tpl_id, CardtplBody body) {
    return null;
  }

  @Override public Observable<QcDataResponse> qcDelCardtpl(String card_tpl_id) {
    return null;
  }

  @Override public Observable<QcDataResponse> qcResumeCardtpl(String card_tpl_id) {
    return null;
  }

  @Override public Observable<QcDataResponse> qcDelCardStandard(String option_id) {
    return null;
  }

  @Override
  public Observable<QcDataResponse> qcUpdateCardStandard(String option_id, OptionBody body) {
    return null;
  }

  @Override
  public Observable<QcDataResponse> qcCreateStandard(String card_tpl_id, OptionBody body) {
    return null;
  }

  @Override public Observable<QcDataResponse<JsonObject>> qcChargeCard(String cardId,
      CardBuyBody ochargeBody) {
    CardBuyBody chargeBody = (CardBuyBody) ochargeBody.clone();
    chargeBody.setType(null);
    if (chargeBody.getSeller_id() != null && chargeBody.getSeller_id().equalsIgnoreCase("0")) {
      chargeBody.setSeller_id(null);
    }
    return posApi.qcCardCharge(loginStatus.staff_id(), cardId, gymWrapper.getParams(), chargeBody);
  }

  @Override public Observable<QcDataResponse<JsonObject>> buyCard(CardBuyBody obody) {
    CardBuyBody body = (CardBuyBody) obody.clone();
    body.setType(null);
    if (body.getSeller_id() != null && body.seller_id.equalsIgnoreCase("0")) {
      body.setSeller_id(null);
    }
    return posApi.qcCreateRealcard(loginStatus.staff_id(), body, gymWrapper.getParams());
  }

  @Override
  public Observable<QcDataResponse<CardListWrap>> qcGetAllCard(HashMap<String, Object> params) {
    params.putAll(gymWrapper.getParams());
    return posApi.getAllCards(loginStatus.staff_id(), params);
  }

  @Override
  public Observable<QcDataResponse<CardListWrap>> qcGetBalanceCard(HashMap<String, Object> params) {
    return null;
  }

  @Override public Observable<QcDataResponse<CardListWrap>> qcGetBalanceCard() {
    return null;
  }

  @Override public Observable<QcDataResponse<StudentListWrapper>> qcGetBindStudent(String cardid) {
    return null;
  }

  @Override
  public Observable<QcDataResponse> editCardInfo(String cardid, HashMap<String, Object> p) {
    return null;
  }

  @Override public Observable<QcDataResponse> qcChangeAutoNotify(CardBalanceNotifyBody body) {
    return null;
  }

  @Override public Observable<QcDataResponse<NotityIsOpenConfigs>> qcGetNotifySetting(
      HashMap<String, Object> params) {
    return null;
  }

  @Override public Observable<QcDataResponse<BalanceConfigs>> qcGetBalanceCondition(
      HashMap<String, Object> params, String keys) {
    return null;
  }

  @Override public Observable<QcDataResponse> qcPostBalanceCondition(CardBalanceNotifyBody body) {
    return null;
  }

  @Override public Observable<QcDataResponse<Shops>> qcGetBrandShops(String brand_id) {
    return null;
  }

  @Override public Observable<QcDataResponse<UUIDModel>> qcStashNewCardTpl(CardtplBody body) {
    return null;
  }

  @Override
  public Observable<QcDataResponse<JsonObject>> qcChargeRefund(String cardId, ChargeBody body) {
    return null;
  }

  @Override public Observable<QcDataResponse> qcAddDayOff(AddDayOffBody body) {
    return null;
  }

  @Override public Observable<QcDataResponse> qcDelDayOff(String leaveId) {
    return null;
  }

  @Override public Observable<QcDataResponse<DayOffs>> qcGetDayOffList(String cardId) {
    return null;
  }

  @Override public Observable<QcDataResponse> qcAheadOffDay(String leaveId, AheadOffDayBody body) {
    return null;
  }

  @Override public Observable<QcDataResponse> qcStopCard(String cardId) {
    return null;
  }

  @Override
  public Observable<QcDataResponse> qcModifyValidate(String cardId, UpdateCardValidBody body) {
    return null;
  }

  @Override public Observable<QcDataResponse> qcResumeCard(String cardId) {
    return null;
  }

  @Override
  public Observable<QcDataResponse<QcResponseRealcardHistory>> qcConsumeRecord(String cardId,
      int page, String start, String end) {
    return null;
  }

  @Override public Observable<QcDataResponse> qcFixGyms(String cardId, ShopsBody body) {
    return null;
  }

  @Override public Observable<QcDataResponse<BalanceCount>> qcGetBalanceCount() {
    return null;
  }
}

package cn.qingchengfit.model;

import android.support.v4.util.ArrayMap;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.events.EventNetWorkError;
import cn.qingchengfit.model.responese.CacluScore;
import cn.qingchengfit.model.responese.SellerWrapper;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.apis.CardApi;
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
import retrofit2.http.Body;
import retrofit2.http.Path;
import rx.Observable;

public class CardModel implements ICardModel {

  QcRestRepository repository;
  GymWrapper gymWrapper;
  LoginStatus loginStatus;
  CardApi posApi;
  cn.qingchengfit.card.network.CardApi cardApi;

  public static CardModel getInstance() {
    return INSTANCE;
  }

  private static CardModel INSTANCE;

  public CardModel(QcRestRepository repository, GymWrapper gymWrapper, LoginStatus loginStatus) {
    this.repository = repository;
    this.gymWrapper = gymWrapper;
    this.loginStatus = loginStatus;
    posApi = repository.createGetApi(CardApi.class);
    cardApi = repository.createGetApi(cn.qingchengfit.card.network.CardApi.class);
    INSTANCE = this;
  }

  @Override
  public Observable<QcDataResponse<CardTplListWrap>> qcGetCardTpls(String type, String isEnable) {
    return posApi.qcGetCardTpls(loginStatus.staff_id(), gymWrapper.getParams(), type, isEnable);
  }

  @Deprecated @Override public Observable<QcDataResponse<CardTplListWrap>> qcGetCardTplsPermission(
      HashMap<String, Object> params) {
    params.putAll(gymWrapper.getParams());
    return posApi.qcGetCardTplsNoPermission(loginStatus.staff_id(), params);
  }

  @Override public Observable<QcDataResponse<CardTplWrapper>> qcGetCardTplsDetail(String cardid) {
    return posApi.qcGetCardTplsDetail(loginStatus.staff_id(), cardid, gymWrapper.getParams());
  }

  @Override
  public Observable<QcDataResponse<CardTplOptionListWrap>> qcGetOptions(String cardtps_id) {
    return posApi.qcGetOptions(loginStatus.staff_id(), cardtps_id, gymWrapper.getParams());
  }

  @Override
  public Observable<QcDataResponse<CardTplListWrap>> qcGetCardFilterTpls(boolean is_active) {
    return posApi.qcGetCardFilterCondition(loginStatus.staff_id(), gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse<CardWrap>> qcGetCardDetail(String card_id) {
    return posApi.qcGetCardDetail(loginStatus.staff_id(), card_id, gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse<SellerWrapper>> qcGetDefineSeller(String card_id) {
    return posApi.qcGetDefineSeller(loginStatus.staff_id(), card_id, gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse> qcCreateCardtpl(@Body CardtplBody body) {
    int retCode = body.checkInPos();
    if (retCode > 0) {
      RxBus.getBus().post(new EventNetWorkError(retCode));
      return Observable.just(new QcDataResponse());
    }
    return posApi.qcCreateCardtpl(loginStatus.staff_id(), body, gymWrapper.getParams());
  }

  @Override
  public Observable<QcDataResponse> qcUpdateCardtpl(@Path("card_tpl_id") String card_tpl_id,
      @Body CardtplBody body) {
    return posApi.qcUpdateCardtpl(loginStatus.staff_id(), card_tpl_id, body,
        gymWrapper.getParams());
  }

  @Override
  public Observable<QcDataResponse> qcDelCardtpl(@Path("card_tpl_id") String card_tpl_id) {
    return posApi.qcDelCardtpl(loginStatus.staff_id(), card_tpl_id, gymWrapper.getParams());
  }

  @Override
  public Observable<QcDataResponse> qcResumeCardtpl(@Path("card_tpl_id") String card_tpl_id) {
    return posApi.qcResumeCardtpl(loginStatus.staff_id(), card_tpl_id, gymWrapper.getParams());
  }

  @Override
  public Observable<QcDataResponse> qcDelCardStandard(@Path("option_id") String option_id) {
    return posApi.qcDelCardtplOption(loginStatus.staff_id(), option_id, gymWrapper.getParams());
  }

  @Override
  public Observable<QcDataResponse> qcUpdateCardStandard(@Path("option_id") String option_id,
      @Body OptionBody body) {
    return posApi.qcUpdateCardtplOption(loginStatus.staff_id(), option_id, gymWrapper.getParams(),
        body);
  }

  @Override
  public Observable<QcDataResponse> qcCreateStandard(@Path("card_tpl_id") String card_tpl_id,
      @Body OptionBody body) {

    return posApi.qcCreateCardtplOption(loginStatus.staff_id(), card_tpl_id, gymWrapper.getParams(),
        body);
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

  @Override public Observable<QcDataResponse<JsonObject>> buyCard(@Body CardBuyBody obody) {
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
    params.putAll(gymWrapper.getParams());
    return posApi.qcGetBalanceCard(loginStatus.staff_id(), params);
  }

  @Override public Observable<QcDataResponse<CardListWrap>> qcGetBalanceCard() {
    return null;
  }

  @Override public Observable<QcDataResponse<StudentListWrapper>> qcGetBindStudent(String cardid) {
    return null;
  }

  @Override
  public Observable<QcDataResponse> editCardInfo(String cardid, HashMap<String, Object> p) {
    p.putAll(gymWrapper.getParams());
    return posApi.editCardInfo(loginStatus.staff_id(), cardid, p);
  }

  @Override public Observable<QcDataResponse> qcChangeAutoNotify(CardBalanceNotifyBody body) {
    return posApi.qcChangeAutoNotify(loginStatus.staff_id(), gymWrapper.getParams(), body);
  }

  @Override public Observable<QcDataResponse<NotityIsOpenConfigs>> qcGetNotifySetting(
      HashMap<String, Object> params) {
    params.putAll(gymWrapper.getParams());
    return posApi.qcGetNotifySetting(loginStatus.staff_id(), params);
  }

  @Override public Observable<QcDataResponse<BalanceConfigs>> qcGetBalanceCondition(
      HashMap<String, Object> params, String keys) {
    params.putAll(gymWrapper.getParams());
    return posApi.qcGetBalanceCondition(loginStatus.staff_id(), params, keys);
  }

  @Override public Observable<QcDataResponse> qcPostBalanceCondition(CardBalanceNotifyBody body) {
    return posApi.qcPostBalanceCondition(loginStatus.staff_id(), gymWrapper.getParams(), body);
  }

  public Observable<QcDataResponse<Shops>> qcGetBrandShops(String brand_id) {
    return posApi.qcGetBrandShops(loginStatus.staff_id(), brand_id);
  }

  @Override public Observable<QcDataResponse<UUIDModel>> qcStashNewCardTpl(CardtplBody body) {
    HashMap<String, Object> params = new HashMap<>();
    if (gymWrapper.getCoachService() == null) {
      params.put("brand_id", gymWrapper.brand_id());
    } else {
      params.putAll(gymWrapper.getParams());
    }
    return posApi.qcStashNewCardTpl(loginStatus.staff_id(), body, params);
  }

  @Override
  public Observable<QcDataResponse<JsonObject>> qcChargeRefund(String cardId, ChargeBody body) {
    HashMap<String, Object> params = new HashMap<>();
    if (!gymWrapper.inBrand()) {
      params = gymWrapper.getParams();
      params.putAll(gymWrapper.getShopParams());
    }
    return posApi.qcMinusMoney(loginStatus.staff_id(), cardId, params, body);
  }

  @Override public Observable<QcDataResponse> qcAddDayOff(AddDayOffBody body) {
    return posApi.qcAddDayOff(loginStatus.staff_id(), gymWrapper.brand_id(), gymWrapper.id(),
        gymWrapper.model(), body);
  }

  @Override public Observable<QcDataResponse> qcDelDayOff(String leaveId) {
    return posApi.qcDelDayOff(loginStatus.staff_id(), leaveId, gymWrapper.brand_id(),
        gymWrapper.id(), gymWrapper.model());
  }

  @Override public Observable<QcDataResponse<DayOffs>> qcGetDayOffList(String cardId) {
    return posApi.qcGetDayOff(loginStatus.staff_id(), gymWrapper.brand_id(), cardId,
        gymWrapper.id(), gymWrapper.model());
  }

  @Override public Observable<QcDataResponse> qcAheadOffDay(String leaveId, AheadOffDayBody body) {
    return posApi.qcAheadDayOff(loginStatus.staff_id(), leaveId, gymWrapper.getParams(), body);
  }

  @Override public Observable<QcDataResponse> qcStopCard(String cardId) {
    return posApi.qcUnregisteCard(loginStatus.staff_id(), cardId, gymWrapper.brand_id(),
        gymWrapper.id(), gymWrapper.model());
  }

  @Override
  public Observable<QcDataResponse> qcModifyValidate(String cardId, UpdateCardValidBody body) {
    return posApi.qcUndateCardValid(loginStatus.staff_id(), cardId, gymWrapper.getParams(), body);
  }

  @Override public Observable<QcDataResponse> qcResumeCard(String cardId) {
    return posApi.qcResumeCard(loginStatus.staff_id(), cardId, gymWrapper.brand_id(),
        gymWrapper.id(), gymWrapper.model());
  }

  @Override
  public Observable<QcDataResponse<QcResponseRealcardHistory>> qcConsumeRecord(String cardId,
      int page, String start, String end) {
    HashMap<String, Object> params = gymWrapper.getParams();
    params.put("brand_id", gymWrapper.brand_id());
    return posApi.qcGetCardhistory(loginStatus.staff_id(), cardId, params, start, end, page);
  }

  @Override public Observable<QcDataResponse> qcFixGyms(String cardId, ShopsBody body) {
    return posApi.qcFixGyms(loginStatus.staff_id(), cardId, body, gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse<CacluScore>> cacluScore(String staff_id, String type,
      String money, ArrayMap<String, String> params) {
    return cardApi.qcGetScoreCalu(staff_id, type, money, params);
  }

  @Override public Observable<QcDataResponse<BalanceCount>> qcGetBalanceCount() {
    return posApi.qcGetCardCount(loginStatus.staff_id(), gymWrapper.getParams());
  }
}
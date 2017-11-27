package cn.qingchengfit.model;

import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.events.EventNetWorkError;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saasbase.cards.network.body.CardBalanceNotifyBody;
import cn.qingchengfit.saasbase.cards.network.body.CardBuyBody;
import cn.qingchengfit.saasbase.cards.network.body.CardtplBody;
import cn.qingchengfit.saasbase.cards.network.body.ChargeBody;
import cn.qingchengfit.saasbase.cards.network.body.OptionBody;
import cn.qingchengfit.saasbase.cards.network.response.CardListWrap;
import cn.qingchengfit.saasbase.cards.network.response.CardTplListWrap;
import cn.qingchengfit.saasbase.cards.network.response.CardTplOptionListWrap;
import cn.qingchengfit.saasbase.cards.network.response.CardTplWrapper;
import cn.qingchengfit.saasbase.cards.network.response.CardWrap;
import cn.qingchengfit.saasbase.cards.network.response.NotityIsOpenConfigs;
import cn.qingchengfit.saasbase.cards.network.response.PayBusinessResponseWrap;
import cn.qingchengfit.saasbase.repository.ICardModel;
import cn.qingchengfit.saasbase.student.network.body.StudentListWrapper;
import cn.qingchengfit.staffkit.rest.CardApi;
import java.util.HashMap;
import retrofit2.http.Body;
import retrofit2.http.Path;
import rx.Observable;

public class CardModel implements ICardModel {

  QcRestRepository repository;
  GymWrapper gymWrapper;
  LoginStatus loginStatus;
  CardApi posApi;

  public CardModel(QcRestRepository repository, GymWrapper gymWrapper, LoginStatus loginStatus) {
    this.repository = repository;
    this.gymWrapper = gymWrapper;
    this.loginStatus = loginStatus;
    posApi = repository.createGetApi(CardApi.class);
  }

  @Override
  public Observable<QcDataResponse<CardTplListWrap>> qcGetCardTpls(String type, String isEnable) {
    return posApi.qcGetCardTpls(loginStatus.staff_id(), gymWrapper.getParams(), null, isEnable);
  }

  @Deprecated @Override public Observable<QcDataResponse<CardTplListWrap>> qcGetCardTplsPermission() {
    return null;
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
    return posApi.qcUpdateCardtpl(loginStatus.staff_id(), card_tpl_id, body, gymWrapper.getParams());
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
  public Observable<QcDataResponse> qcFixGyms(@Path("cardtpl_id") String card_tpl, String shops) {
    return null;
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

  @Override public Observable<QcDataResponse<PayBusinessResponseWrap>> qcChargeCard(ChargeBody ochargeBody) {
    ChargeBody chargeBody = (ChargeBody) ochargeBody.clone();
    if (chargeBody.getSeller_id().equalsIgnoreCase("0")) {
      chargeBody.setSeller_id(null);
    }
    return posApi.qcCardCharge(loginStatus.staff_id(), chargeBody.getCard_id(), gymWrapper.getParams(),
      chargeBody);
  }

  @Override public Observable<QcDataResponse<PayBusinessResponseWrap>> buyCard(@Body CardBuyBody obody) {
    CardBuyBody body = (CardBuyBody) obody.clone();
    if (body.getSeller_id().equalsIgnoreCase("0")) {
      body.setSeller_id(null);
    }
    return posApi.qcCreateRealcard(loginStatus.staff_id(), body, gymWrapper.getParams());
  }

  @Override
  public Observable<QcDataResponse<CardListWrap>> qcGetAllCard(HashMap<String, Object> params) {
    params.putAll(gymWrapper.getParams());
    return posApi.getAllCards(loginStatus.staff_id(), params);
  }

  @Override public Observable<QcDataResponse<CardListWrap>> qcGetBalanceCard() {
    return null;
  }

  @Override public Observable<QcDataResponse<StudentListWrapper>> qcGetBindStudent(String cardid) {
    return null;
  }

  @Override public Observable<QcDataResponse> editCardInfo(String cardid, HashMap<String, Object> p) {
    return posApi.editCardInfo(loginStatus.staff_id(), cardid, p);
  }

  @Override public Observable<QcResponse> qcChangeAutoNotify(CardBalanceNotifyBody body) {
    return null;
  }

  @Override public Observable<QcDataResponse<NotityIsOpenConfigs>> qcGetNotifySetting(
    HashMap<String, Object> params) {
    return null;
  }
}
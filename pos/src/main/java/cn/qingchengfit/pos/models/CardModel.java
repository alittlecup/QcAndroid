package cn.qingchengfit.pos.models;

import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.events.EventNetWorkError;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.pos.net.CardApi;
import cn.qingchengfit.saasbase.cards.network.body.CardBuyBody;
import cn.qingchengfit.saasbase.cards.network.body.CardtplBody;
import cn.qingchengfit.saasbase.cards.network.body.ChargeBody;
import cn.qingchengfit.saasbase.cards.network.body.OptionBody;
import cn.qingchengfit.saasbase.cards.network.response.CardListWrap;
import cn.qingchengfit.saasbase.cards.network.response.CardTplListWrap;
import cn.qingchengfit.saasbase.cards.network.response.CardTplOptionListWrap;
import cn.qingchengfit.saasbase.cards.network.response.CardTplWrapper;
import cn.qingchengfit.saasbase.cards.network.response.CardWrap;
import cn.qingchengfit.saasbase.cards.network.response.PayBusinessResponseWrap;
import cn.qingchengfit.saasbase.repository.ICardModel;
import cn.qingchengfit.saasbase.student.network.body.StudentListWrapper;
import java.util.HashMap;
import retrofit2.http.Body;
import retrofit2.http.Path;
import rx.Observable;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/9/25.
 */

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
    return posApi
        .qcGetCardTpls(gymWrapper.getGymId(), gymWrapper.getParams(), null, isEnable);
  }

  @Override public Observable<QcDataResponse<CardTplListWrap>> qcGetCardTplsPermission() {
    // TODO: 2017/10/10
    return null;
  }

  @Override public Observable<QcDataResponse<CardTplWrapper>> qcGetCardTplsDetail(String cardid) {
    return posApi
        .qcGetCardTplsDetail(gymWrapper.getGymId(), cardid, gymWrapper.getParams());
  }

  @Override
  public Observable<QcDataResponse<CardTplOptionListWrap>> qcGetOptions(String cardtps_id) {
    return posApi
        .qcGetOptions(gymWrapper.getGymId(), cardtps_id, gymWrapper.getParams());
  }

  @Override
  public Observable<QcDataResponse<CardTplListWrap>> qcGetCardFilterTpls(boolean is_active) {
    return posApi
        .qcGetCardFilterCondition(gymWrapper.getGymId(), gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse<CardWrap>> qcGetCardDetail(String card_id) {
    return posApi.qcGetCardDetail(gymWrapper.getGymId(), card_id, gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse> qcCreateCardtpl(@Body CardtplBody body) {
    int retCode = body.checkInPos();
    if (retCode > 0) {
      RxBus.getBus().post(new EventNetWorkError(retCode));
      return Observable.just(new QcDataResponse());
    }
    return posApi.qcCreateCardtpl(gymWrapper.getGymId(), body, gymWrapper.getParams());
  }

  @Override
  public Observable<QcDataResponse> qcUpdateCardtpl(@Path("card_tpl_id") String card_tpl_id,
      @Body CardtplBody body) {
    return posApi.qcUpdateCardtpl(gymWrapper.getGymId(), card_tpl_id, body,
        gymWrapper.getParams());
  }

  @Override
  public Observable<QcDataResponse> qcDelCardtpl(@Path("card_tpl_id") String card_tpl_id) {
    return posApi.qcDelCardtpl(gymWrapper.getGymId(), card_tpl_id, gymWrapper.getParams());
  }

  @Override
  public Observable<QcDataResponse> qcResumeCardtpl(@Path("card_tpl_id") String card_tpl_id) {
    return posApi.qcResumeCardtpl(gymWrapper.getGymId(), card_tpl_id, gymWrapper.getParams());
  }

  @Override
  public Observable<QcDataResponse> qcFixGyms(@Path("cardtpl_id") String card_tpl, String shops) {
    return null;
  }

  @Override
  public Observable<QcDataResponse> qcDelCardStandard(@Path("option_id") String option_id) {
    return posApi.qcDelCardtplOption(gymWrapper.getGymId(), option_id, gymWrapper.getParams());
  }

  @Override
  public Observable<QcDataResponse> qcUpdateCardStandard(@Path("option_id") String option_id,
      @Body OptionBody body) {
    return posApi.qcUpdateCardtplOption(gymWrapper.getGymId(),option_id,gymWrapper.getParams(),body);
  }

  @Override
  public Observable<QcDataResponse> qcCreateStandard(@Path("card_tpl_id") String card_tpl_id,
      @Body OptionBody body) {
    
    return posApi.qcCreateCardtplOption(gymWrapper.getGymId(),card_tpl_id,gymWrapper.getParams(),body);
  }

  @Override public Observable<QcDataResponse<PayBusinessResponseWrap>> qcChargeCard(ChargeBody chargeBody) {
    return posApi.qcCardCharge(gymWrapper.getGymId(),chargeBody.getCard_id(),gymWrapper.getParams(),chargeBody);
  }

  @Override public Observable<QcDataResponse<PayBusinessResponseWrap>> buyCard(@Body CardBuyBody body) {
    return posApi.qcCreateRealcard(gymWrapper.getGymId(),body,gymWrapper.getParams());
  }

  @Override
  public Observable<QcDataResponse<CardListWrap>> qcGetAllCard(HashMap<String, Object> params) {
    params.putAll(gymWrapper.getParams());
    return posApi.getAllCards(gymWrapper.getGymId(), params);
  }

  @Override public Observable<QcDataResponse<CardListWrap>> qcGetBalanceCard() {
    return null;
  }

  @Override public Observable<QcDataResponse<StudentListWrapper>> qcGetBindStudent(String cardid) {
    return null;
  }

  @Override public Observable<QcDataResponse> editCardInfo(String cardid,HashMap<String,Object> p) {
    return posApi.editCardInfo(gymWrapper.getGymId(),cardid ,p);
  }
}

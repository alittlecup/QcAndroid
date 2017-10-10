package cn.qingchengfit.pos.models;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.pos.net.PosApi;
import cn.qingchengfit.saasbase.cards.network.body.CardBuyBody;
import cn.qingchengfit.saasbase.cards.network.body.CardtplBody;
import cn.qingchengfit.saasbase.cards.network.body.ChargeBody;
import cn.qingchengfit.saasbase.cards.network.body.OptionBody;
import cn.qingchengfit.saasbase.cards.network.response.CardListWrap;
import cn.qingchengfit.saasbase.cards.network.response.CardTplListWrap;
import cn.qingchengfit.saasbase.cards.network.response.CardTplOptionListWrap;
import cn.qingchengfit.saasbase.cards.network.response.CardTplWrapper;
import cn.qingchengfit.saasbase.cards.network.response.CardWrap;
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
  PosApi posApi;

  public CardModel(QcRestRepository repository, GymWrapper gymWrapper, LoginStatus loginStatus) {
    this.repository = repository;
    this.gymWrapper = gymWrapper;
    this.loginStatus = loginStatus;
    posApi = repository.createGetApi(PosApi.class);
  }

  @Override
  public Observable<QcDataResponse<CardTplListWrap>> qcGetCardTpls(String type, String isEnable) {
    return repository.createGetApi(PosApi.class)
        .qcGetCardTpls(loginStatus.staff_id(), gymWrapper.getParams(), null, "1");
  }

  @Override public Observable<QcDataResponse<CardTplListWrap>> qcGetCardTplsPermission() {
    return null;
  }

  @Override public Observable<QcDataResponse<CardTplWrapper>> qcGetCardTplsDetail(String cardid) {
    return repository.createGetApi(PosApi.class)
        .qcGetCardTplsDetail(loginStatus.staff_id(), cardid, gymWrapper.getParams());
  }

  @Override
  public Observable<QcDataResponse<CardTplOptionListWrap>> qcGetOptions(String cardtps_id) {
    return repository.createGetApi(PosApi.class)
        .qcGetOptions(loginStatus.staff_id(), cardtps_id, gymWrapper.getParams());
  }

  @Override
  public Observable<QcDataResponse<CardTplListWrap>> qcGetCardFilterTpls(boolean is_active) {
    return repository.createGetApi(PosApi.class)
        .qcGetCardFilterCondition(loginStatus.staff_id(), gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse<CardWrap>> qcGetCardDetail(String card_id) {
    return posApi.qcGetCardDetail(loginStatus.staff_id(), card_id, gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse> qcCreateCardtpl(@Body CardtplBody body) {
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
    return posApi.qcUpdateCardtplOption(loginStatus.staff_id(),option_id,gymWrapper.getParams(),body);
  }

  @Override
  public Observable<QcDataResponse> qcCreateStandard(@Path("card_tpl_id") String card_tpl_id,
      @Body OptionBody body) {
    return posApi.qcCreateCardtplOption(loginStatus.staff_id(),card_tpl_id,gymWrapper.getParams(),body);
  }

  @Override public Observable<QcResponse> qcChargeCard(ChargeBody chargeBody) {
    return null;
  }

  @Override public Observable<QcResponse> buyCard(@Body CardBuyBody body) {
    return null;
  }

  @Override
  public Observable<QcDataResponse<CardListWrap>> qcGetAllCard(HashMap<String, Object> params) {
    params.putAll(gymWrapper.getParams());
    return repository.createGetApi(PosApi.class).getAllCards(loginStatus.staff_id(), params);
  }

  @Override public Observable<QcDataResponse<CardListWrap>> qcGetBalanceCard() {
    return null;
  }

  @Override public Observable<QcDataResponse<StudentListWrapper>> qcGetBindStudent(String cardid) {
    return null;
  }
}

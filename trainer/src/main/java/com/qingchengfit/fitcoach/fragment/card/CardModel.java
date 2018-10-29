package com.qingchengfit.fitcoach.fragment.card;

import android.support.v4.util.ArrayMap;
import cn.qingchengfit.card.network.CardApi;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.ComponentModuleManager;
import cn.qingchengfit.model.responese.CacluScore;
import cn.qingchengfit.model.responese.QcResponseStudentCards;
import cn.qingchengfit.model.responese.SellerWrapper;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.cards.bean.BalanceCount;
import cn.qingchengfit.saasbase.cards.bean.Card;
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
import cn.qingchengfit.saasbase.report.bean.QcResponseSaleDetail;
import cn.qingchengfit.saasbase.repository.ICardModel;
import cn.qingchengfit.saasbase.student.network.body.StudentListWrapper;
import com.google.gson.JsonObject;
import com.qingchengfit.fitcoach.http.bean.StudentCarsResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.functions.Func1;

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
    ComponentModuleManager.register(ICardModel.class, this);
  }

  @Override
  public Observable<QcDataResponse<CardTplListWrap>> qcGetCardTpls(String type, String isEnable) {
    return posApi.qcGetCardTpls(loginStatus.staff_id(), gymWrapper.getParams(), type, isEnable);
  }

  @Override public Observable<QcDataResponse<CardTplListWrap>> qcGetCardTplsPermission(
      HashMap<String, Object> params) {
    //这里之前传过来的参数是管理哦端使用的
    return posApi.qcGetCardTplsNoPermission(loginStatus.staff_id(), gymWrapper.getParams());
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

  @Override public Observable<QcResponseStudentCards> qcGetStudentCards(String studentID) {
    return posApi.qcGetStuedntCard(studentID, gymWrapper.getParams())
        .map(new Func1<StudentCarsResponse, QcResponseStudentCards>() {
          @Override public QcResponseStudentCards call(StudentCarsResponse studentCarsResponse) {
            List<Card> cards = new ArrayList<>();
            if (studentCarsResponse != null
                && studentCarsResponse.data != null
                && studentCarsResponse.data.cards != null
                && studentCarsResponse.data.cards.size() > 0) {
              for (StudentCarsResponse.Card remote : studentCarsResponse.data.cards) {
                Card copy = new Card();
                copy.setId(remote.id);
                copy.setType(remote.type);
                copy.setTotal_account(Float.valueOf(remote.account));
                copy.setTotal_times(Float.valueOf(remote.times));
                copy.url = remote.url;
                copy.setCheck_valid(remote.check_valid);
                copy.setStart(remote.start);
                copy.setEnd(remote.end);
                copy.setValid_from(remote.valid_from);
                copy.setValid_to(remote.valid_to);
                copy.setUserNames(remote.users);
                copy.setIs_locked(remote.is_locked);
                copy.setIs_active(remote.is_active);
                copy.setExpired(remote.expired);
                copy.setBalance(remote.balance);
                copy.setCard_tpl_id(remote.card_tpl_id);
                copy.setCard_tpl(remote.card_tpl);
                cards.add(copy);
              }
            }
            QcResponseStudentCards qcResponseStudentCards=new QcResponseStudentCards();
            QcResponseStudentCards.Data data = new QcResponseStudentCards.Data();
            data.cards=cards;
            qcResponseStudentCards.data=data;
            qcResponseStudentCards.status=studentCarsResponse.status;
            qcResponseStudentCards.msg=studentCarsResponse.msg;
            qcResponseStudentCards.error_code=studentCarsResponse.error_code;
            qcResponseStudentCards.info=studentCarsResponse.info;
            qcResponseStudentCards.level=studentCarsResponse.level;
            return qcResponseStudentCards;
          }
        });
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

  @Override public Observable<QcDataResponse<JsonObject>> qcChargeCardFromCheckout(String cardId,
      CardBuyBody ochargeBody) {
    CardBuyBody chargeBody = (CardBuyBody) ochargeBody.clone();
    chargeBody.setType(null);
    chargeBody.staff_id = null;
    chargeBody.setCard_tpl_id(null);
    chargeBody.seller_id = null;
    return posApi.qcCardCharge(loginStatus.staff_id(), cardId, gymWrapper.getParams(), chargeBody);
  }

  @Override public Observable<QcDataResponse<JsonObject>> qcChargeCard(String cardId,
      CardBuyBody chargeBody) {
    return null;
  }

  @Override public Observable<QcDataResponse<JsonObject>> buyCardFromCheckout(CardBuyBody obody) {
    CardBuyBody body = (CardBuyBody) obody.clone();
    body.setType(null);
    if (body.getSeller_id() != null && body.seller_id.equalsIgnoreCase("0")) {
      body.setSeller_id(null);
    }
    return posApi.qcCreateRealcard(loginStatus.staff_id(), body, gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse<JsonObject>> buyCard(CardBuyBody body) {
    return null;
  }

  @Override
  public Observable<QcDataResponse<CardListWrap>> qcGetAllCard(HashMap<String, Object> params) {
    params.putAll(gymWrapper.getParams());
    return posApi.qcGetAllCards(loginStatus.staff_id(), params);
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

  @Override public Observable<QcDataResponse<CacluScore>> cacluScore(String staff_id, String type,
      String money, ArrayMap<String, String> params) {
    return posApi.qcGetScoreCalu(staff_id, type, money, params);
  }

  @Override public Observable<QcDataResponse<BalanceCount>> qcGetBalanceCount() {
    return null;
  }
}

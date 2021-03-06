package cn.qingchengfit.saasbase.repository;

import android.support.v4.util.ArrayMap;
import cn.qingchengfit.model.responese.CacluScore;
import cn.qingchengfit.model.responese.QcResponseStudentCards;
import cn.qingchengfit.model.responese.SellerWrapper;
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
import cn.qingchengfit.saasbase.student.network.body.StudentListWrapper;
import com.google.gson.JsonObject;
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

public interface ICardModel {
  /**
   * 获取所有卡种类
   *
   * @param type 卡类型（储值 次卡 期限）
   * @param isEnable 是否开启
   */
  rx.Observable<QcDataResponse<CardTplListWrap>> qcGetCardTpls(String type, String isEnable);

  /**
   * 购卡时 使用不被权限控制的卡
   */
  rx.Observable<QcDataResponse<CardTplListWrap>> qcGetCardTplsPermission(
      HashMap<String, Object> params);

  /**
   * 获取卡种类详情
   */
  rx.Observable<QcDataResponse<CardTplWrapper>> qcGetCardTplsDetail(String cardid);

  /**
   * 获取会员卡种类的支付规格
   *
   * @param cardtps_id 卡种类ID
   */
  rx.Observable<QcDataResponse<CardTplOptionListWrap>> qcGetOptions(String cardtps_id);

  /**
   * 拥有的所有会员卡种类列表，（会员卡列表 页面筛选项）
   */
  rx.Observable<QcDataResponse<CardTplListWrap>> qcGetCardFilterTpls(boolean is_active);

  /**
   * 获取会员卡详情
   */
  rx.Observable<QcDataResponse<CardWrap>> qcGetCardDetail(String card_id);
  /**
   *获取某个学员的cardlist
   */
  rx.Observable<QcResponseStudentCards> qcGetStudentCards(String studentID);

  /**
   * 获取会员卡扣费时默认的销售信息
   */
  rx.Observable<QcDataResponse<SellerWrapper>> qcGetDefineSeller(String card_id);

  /**
   * 卡类型
   */
  //新建卡模板
  rx.Observable<QcDataResponse> qcCreateCardtpl(@Body CardtplBody body);

  /**
   * @param card_tpl_id 卡模板id
   * @param body 卡模板body
   */
  rx.Observable<QcDataResponse> qcUpdateCardtpl(@Path("card_tpl_id") String card_tpl_id,
      @Body CardtplBody body);

  /**
   * 停用会员卡种类
   */
  rx.Observable<QcDataResponse> qcDelCardtpl(@Path("card_tpl_id") String card_tpl_id);

  /**
   * 恢复会员卡种类
   */
  rx.Observable<QcDataResponse> qcResumeCardtpl(@Path("card_tpl_id") String card_tpl_id);

  /**
   * 卡规格删除操作
   */
  rx.Observable<QcDataResponse> qcDelCardStandard(@Path("option_id") String option_id);

  /**
   * 修改卡模板规格
   */
  rx.Observable<QcDataResponse> qcUpdateCardStandard(@Path("option_id") String option_id,
      @Body OptionBody body);

  /**
   * 新建卡模板规格
   */
  rx.Observable<QcDataResponse> qcCreateStandard(@Path("card_tpl_id") String card_tpl_id,
      @Body OptionBody body);

  /**
   * 充卡
   */
  rx.Observable<QcDataResponse<JsonObject>> qcChargeCard(String cardId, CardBuyBody chargeBody);
  rx.Observable<QcDataResponse<JsonObject>> qcChargeCardFromCheckout(String cardId, CardBuyBody chargeBody);

  /**
   * 购卡操作
   */
  rx.Observable<QcDataResponse<JsonObject>> buyCard(CardBuyBody body);
  rx.Observable<QcDataResponse<JsonObject>> buyCardFromCheckout(CardBuyBody body);

  /**
   * 会员卡
   */
  //获取真实卡列表
  rx.Observable<QcDataResponse<CardListWrap>> qcGetAllCard(HashMap<String, Object> params);

  //获取余额不足的会员卡
  rx.Observable<QcDataResponse<CardListWrap>> qcGetBalanceCard(HashMap<String, Object> params);

  //获取余额不足会员卡
  rx.Observable<QcDataResponse<CardListWrap>> qcGetBalanceCard();

  /**
   * 获取某张卡绑定的会员
   */
  rx.Observable<QcDataResponse<StudentListWrapper>> qcGetBindStudent(String cardid);

  /**
   * 修改卡信息
   */
  rx.Observable<QcDataResponse> editCardInfo(String cardid, HashMap<String, Object> p);

  rx.Observable<QcDataResponse> qcChangeAutoNotify(CardBalanceNotifyBody body);

  rx.Observable<QcDataResponse<NotityIsOpenConfigs>> qcGetNotifySetting(
      HashMap<String, Object> params);

  rx.Observable<QcDataResponse<BalanceConfigs>> qcGetBalanceCondition(
      HashMap<String, Object> params, String keys);

  rx.Observable<QcDataResponse> qcPostBalanceCondition(CardBalanceNotifyBody body);

  Observable<QcDataResponse<Shops>> qcGetBrandShops(String brand_id);

  /**
   * 会员卡服务协议暂存接口
   */
  rx.Observable<QcDataResponse<UUIDModel>> qcStashNewCardTpl(@Body CardtplBody body);

  /**
   * 扣费
   */
  rx.Observable<QcDataResponse<JsonObject>> qcChargeRefund(String cardId, @Body ChargeBody body);

  /**
   * 请假
   */
  rx.Observable<QcDataResponse> qcAddDayOff(@Body AddDayOffBody body);

  rx.Observable<QcDataResponse> qcDelDayOff(String leaveId);

  rx.Observable<QcDataResponse<DayOffs>> qcGetDayOffList(String cardId);

  rx.Observable<QcDataResponse> qcAheadOffDay(String leaveId, AheadOffDayBody body);

  rx.Observable<QcDataResponse> qcStopCard(String cardId);

  rx.Observable<QcDataResponse> qcModifyValidate(String cardId, UpdateCardValidBody body);

  rx.Observable<QcDataResponse> qcResumeCard(String cardId);

  rx.Observable<QcDataResponse<QcResponseRealcardHistory>> qcConsumeRecord(String cardId, int page,
      String start, String end);

  rx.Observable<QcDataResponse> qcFixGyms(String cardId, ShopsBody body);

  rx.Observable<QcDataResponse<CacluScore>> cacluScore(String staff_id, String type, String money,
      ArrayMap<String, String> params);

  rx.Observable<QcDataResponse<BalanceCount>> qcGetBalanceCount();

}

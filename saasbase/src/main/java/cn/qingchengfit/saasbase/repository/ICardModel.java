package cn.qingchengfit.saasbase.repository;

import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saasbase.cards.network.body.CardBuyBody;
import cn.qingchengfit.saasbase.cards.network.body.CardtplBody;
import cn.qingchengfit.saasbase.cards.network.body.ChargeBody;
import cn.qingchengfit.saasbase.cards.network.body.OptionBody;
import cn.qingchengfit.saasbase.cards.network.response.CardListWrap;
import cn.qingchengfit.saasbase.cards.network.response.CardTplListWrap;
import cn.qingchengfit.saasbase.cards.network.response.CardTplOptionListWrap;
import cn.qingchengfit.saasbase.cards.network.response.CardTplWrapper;
import cn.qingchengfit.saasbase.cards.network.response.CardWrap;
import cn.qingchengfit.saasbase.student.network.body.StudentListWrapper;
import java.util.HashMap;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

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
   * @param type 卡类型（储值 次卡 期限）
   * @param isEnable 是否开启
   */
  rx.Observable<QcDataResponse<CardTplListWrap>> qcGetCardTpls(String type, String isEnable);

  /**
   * 购卡时 使用不被权限控制的卡
   */
  rx.Observable<QcDataResponse<CardTplListWrap>> qcGetCardTplsPermission();

  /**
   * 获取卡种类详情
   */
  rx.Observable<QcDataResponse<CardTplWrapper>> qcGetCardTplsDetail(String cardid);

  /**
   * 获取会员卡种类的支付规格
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
   * 修改适用场馆 { shops:12,12,123 }
   */
  rx.Observable<QcDataResponse> qcFixGyms(@Path("cardtpl_id") String card_tpl, String shops);

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
  rx.Observable<QcResponse> qcChargeCard(ChargeBody chargeBody);

  /**
   * 购卡操作
   */
  @POST("/api/staffs/{id}/cards/create/") rx.Observable<QcResponse> buyCard(
      @Body CardBuyBody body);

  /**
   * 会员卡
   */
  //获取真实卡列表
  rx.Observable<QcDataResponse<CardListWrap>> qcGetAllCard(HashMap<String,Object> params);


  //获取余额不足会员卡
  rx.Observable<QcDataResponse<CardListWrap>> qcGetBalanceCard();

  /**
   * 获取某张卡绑定的会员
   */
  rx.Observable<QcDataResponse<StudentListWrapper>> qcGetBindStudent(String cardid);


}

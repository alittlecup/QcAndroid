package cn.qingchengfit.saasbase.repository;

import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saasbase.cards.cardtypes.network.body.ChargeBody;
import cn.qingchengfit.saasbase.cards.cardtypes.network.response.CardTplListWrap;
import cn.qingchengfit.saasbase.cards.cardtypes.network.response.CardTplOptionListWrap;
import cn.qingchengfit.saasbase.cards.cardtypes.network.response.CardTplWrapper;
import cn.qingchengfit.saasbase.cards.cardtypes.network.response.CardWrap;

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



  rx.Observable<QcResponse> qcChargeCard(ChargeBody chargeBody);

}

package cn.qingchengfit.saasbase.bill.beans;

import cn.qingchengfit.model.base.User;
import cn.qingchengfit.saasbase.cards.bean.Card;

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
 * Created by Paper on 2017/10/20.
 */
public class BusinessBill {
  public String id;//
  //"order_type:" 1/2/.., //交易类型 1：购买会员卡 2. 充值会员卡 3.活动报名 4.课程预约 5.退款 6.提现
  public int type; //
  //"pay_type": 1/2/3..., //支付方式: "WEIXIN", "WEIXIN_QR_CODE", "ALIPAY", "ALIPAY_QR_CODE", "CARD"
  public String pay_type;
  //"order_no": "", //青橙业务订单ID
  public String order_no;
  //"trade_flow": "xxxx", //融数流水号
  public String trade_flow_id;
  public String pay_time;
  public String created_at;
  public User created_by;
  // "origin": //平台: "WEB", "MEMBER", "POS", "APP"
  public String origin;
  public String bank_no;
  public String bank_logo;
  public String bank_username;

  public String remarks;
  public Extra extra;         //根据业务订单变

  public int status;           // // 状态 1：待结算，2：已结算，3：处理中, 4：处理失败, 5.处理完成
  public float price;          //
  public class Extra {
    public Card card;
    public BillScheduleOrder schedule_order;
  }
}

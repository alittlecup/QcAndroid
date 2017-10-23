package cn.qingchengfit.saasbase.bill.beans;

import android.content.Context;
import cn.qingchengfit.model.base.User;
import cn.qingchengfit.saasbase.R;
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
  public User created_by;    //操作员
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

  public String getPrice(float price, int type){
    switch (type){
      case 1:
      case 2:
      case 3:
      case 4:
        return "+"  + price;
      case 5:
      case 6:
        return "-"  + price;
    }
    return "";
  }

  public String getTradeType(int type){
    switch (type){
      case 1:
        return "购买会员卡";
      case 2:
        return "充值会员卡";
      case 3:
        return "活动报名";
      case 4:
        return "课程预约";
      case 5:
        return "退款";
      case 6:
        return "提现";
    }
    return "";
  }

  public String getPayType(Context context, String payType){
    switch (payType){
      case "1":
        return context.getResources().getString(R.string.pay_wx_success);
      case "2":
        return context.getResources().getString(R.string.pay_wx_scan_success);
      case "3":
        return context.getResources().getString(R.string.pay_ali_success);
      case "4":
        return context.getResources().getString(R.string.pay_ali_scan_success);
      case "5":
        return context.getResources().getString(R.string.pay_card_success);
    }
    return "";
  }

  public String getStatus(Context context, int status, int type){
    switch (status){
      case 1:
        return context.getResources().getString(R.string.bill_settlement);
      case 2:
        return context.getResources().getString(R.string.bill_already_settlement);
      case 3:
        return type == 5 ? context.getResources().getString(R.string.bill_withdraw)
            : context.getResources().getString(R.string.bill_back_cash);
      case 4:
        return type == 5 ? context.getResources().getString(R.string.bill_withdraw_failed)
            : context.getResources().getString(R.string.bill_cash_failed);
      case 5:
        return type == 5 ? context.getResources().getString(R.string.bill_withdraw_already)
            : context.getResources().getString(R.string.bll_back_already);
    }

    return "";
  }

}

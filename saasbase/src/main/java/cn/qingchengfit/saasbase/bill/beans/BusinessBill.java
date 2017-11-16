package cn.qingchengfit.saasbase.bill.beans;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import cn.qingchengfit.model.base.User;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.SaasConstant;
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
public class BusinessBill implements Parcelable{
  public String id;//
  //"order_type:" 1/2/.., //交易类型 1：购买会员卡 2. 充值会员卡 3.活动报名 4.课程预约 5.退款 6.提现 7.直接收银
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
  public User seller;       //销售
  // "origin": //平台: "WEB", "MEMBER", "POS", "APP"
  public String origin;
  public String bank_no;
  public String bank_logo;
  public String bank_username;
  public String bank_name;

  public String remarks;
  public Extra extra;         //根据业务订单变

  // 状态status 1：待结算，2：已结算，3：退款中, 4：退款失败, 5.已退款、6：提现中、7：提现失败、8：提现完成、9：退款完成
  public int status;
  public float price;
  public class Extra implements Parcelable{
    public Card card;
    public BillScheduleOrder schedule_order;

    @Override public int describeContents() {
      return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
      dest.writeParcelable(this.card, flags);
      dest.writeParcelable(this.schedule_order, flags);
    }

    public Extra() {
    }

    protected Extra(Parcel in) {
      this.card = in.readParcelable(Card.class.getClassLoader());
      this.schedule_order = in.readParcelable(BillScheduleOrder.class.getClassLoader());
    }

    public final Creator<Extra> CREATOR = new Creator<Extra>() {
      @Override public Extra createFromParcel(Parcel source) {
        return new Extra(source);
      }

      @Override public Extra[] newArray(int size) {
        return new Extra[size];
      }
    };
  }

  public String getPrice(float price, int type) {
    String symbol = "";
    switch (type) {
      case 1:
      case 2:
      case 3:
      case 4:
      case 7:
        symbol = "+";
        break;
      case 5:
      case 6:
        symbol = "-";
        break;
    }
    return symbol + String.format("%.2f", price / 100);
  }

  public String getTradeType(Context context, int type, String payType){
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
        return context.getString(getBackCashTip(payType));
      case 6:
        return "提现";
      case 7:
        return "直接收银";
    }
    return "其它";
  }

  @StringRes public int getBackCashTip(String pay_type){
    switch (pay_type) {
      case SaasConstant.PAY_TYPE_WX:
        return R.string.back_cash_way_weixin;
      case SaasConstant.PAY_TYPE_QQPAY:
        return R.string.back_cash_way_qq;
      case SaasConstant.PAY_TYPE_ALIPAY:
        return R.string.back_cash_way_ali;
      default:
        return R.string.pay_back_cash;
    }
  }

  public String getOrigin(Context context, String origin) {
    switch (origin){
      case "WEB":
        return context.getResources().getString(R.string.bill_pay_origin_web);
      case "MEMBER":
        return context.getResources().getString(R.string.bill_pay_origin_member);
      case "POS":
        return context.getResources().getString(R.string.bill_pay_origin_pos);
      case "APP":
        return context.getResources().getString(R.string.bill_pay_origin_app);

    }
    return "其它";
  }

  @StringRes public int getPayType(int type, String s) {
    if (type == 6){
      return R.string.pay_withdraw;
    }
    if (type == 5){
      return R.string.pay_back_cash;
    }
    switch (s) {
      case SaasConstant.PAY_TYPE_WX:
        return R.string.pay_wx_success;
      case SaasConstant.PAY_TYPE_QQPAY:
        return R.string.pay_qq_success;
      case SaasConstant.PAY_TYPE_ALIPAY:
        return R.string.pay_ali_success;
      case SaasConstant.PAY_TYPE_UNIONPAY:
        return R.string.pay_union_success;
      //case SaasConstant.PAY_TYPE_CARD:
      default:
        return R.string.pay_other_success;
    }
  }

  @DrawableRes public int getTradeDrawable(String type){
    switch (type) {
      case SaasConstant.PAY_TYPE_WX:
        return R.drawable.ic_order_wechat;
      case SaasConstant.PAY_TYPE_QQPAY:
        return R.drawable.ic_order_qq;
      case SaasConstant.PAY_TYPE_ALIPAY:
        return R.drawable.ic_order_alipay;
      case SaasConstant.PAY_TYPE_UNIONPAY:
        return R.drawable.ic_order_bank;
      //case SaasConstant.PAY_TYPE_CARD:
      default:
        return R.drawable.ic_default_header;
    }
  }

  public String getStatus(Context context, int status){
    switch (status){
      case 1:
        return context.getResources().getString(R.string.bill_settlement);
      case 2:
        return context.getResources().getString(R.string.bill_already_settlement);
      case 3:
        return context.getResources().getString(R.string.bill_back_cash);
      case 4:
        return context.getResources().getString(R.string.bill_cash_failed);
      case 5:
        return context.getResources().getString(R.string.bll_back_already);
      case 6:
        return context.getResources().getString(R.string.bill_withdraw);
      case 7:
        return context.getResources().getString(R.string.bill_withdraw_failed);
      case 8:
        return context.getResources().getString(R.string.bill_withdraw_already);
      case 9:
        return context.getResources().getString(R.string.bill_back_cash_finish);
    }

    return context.getResources().getString(R.string.other);
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeInt(this.type);
    dest.writeString(this.pay_type);
    dest.writeString(this.order_no);
    dest.writeString(this.trade_flow_id);
    dest.writeString(this.pay_time);
    dest.writeString(this.created_at);
    dest.writeParcelable(this.created_by, flags);
    dest.writeString(this.origin);
    dest.writeString(this.bank_no);
    dest.writeString(this.bank_logo);
    dest.writeString(this.bank_username);
    dest.writeString(this.bank_name);
    dest.writeString(this.remarks);
    dest.writeParcelable(this.extra, flags);
    dest.writeInt(this.status);
    dest.writeFloat(this.price);
  }

  public BusinessBill() {
  }

  protected BusinessBill(Parcel in) {
    this.id = in.readString();
    this.type = in.readInt();
    this.pay_type = in.readString();
    this.order_no = in.readString();
    this.trade_flow_id = in.readString();
    this.pay_time = in.readString();
    this.created_at = in.readString();
    this.created_by = in.readParcelable(User.class.getClassLoader());
    this.origin = in.readString();
    this.bank_no = in.readString();
    this.bank_logo = in.readString();
    this.bank_username = in.readString();
    this.bank_name = in.readString();
    this.remarks = in.readString();
    this.extra = in.readParcelable(Extra.class.getClassLoader());
    this.status = in.readInt();
    this.price = in.readFloat();
  }

  public static final Creator<BusinessBill> CREATOR = new Creator<BusinessBill>() {
    @Override public BusinessBill createFromParcel(Parcel source) {
      return new BusinessBill(source);
    }

    @Override public BusinessBill[] newArray(int size) {
      return new BusinessBill[size];
    }
  };
}

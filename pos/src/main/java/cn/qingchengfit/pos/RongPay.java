package cn.qingchengfit.pos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.qingchengfit.utils.LogUtil;

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

public class RongPay {
  private static final String ACTION_PAY = "com.rongcapital.pay";
  private static final String ACTION_PRINT = "com.rongcapital.print";

  private String merOrderId;
  private String title;
  private String operator;
  private String customerNo;
  private String phoneNo;
  private long amount;
  //private int payType;

  private RongPay(Builder builder) {
    merOrderId = builder.merOrderId;
    title = builder.title;
    operator = builder.operator;
    customerNo = builder.customerNo;
    phoneNo = builder.phoneNo;
    amount = builder.amount;
  }



  public Intent pay(Context context) {
    int payType = 0;

    String bizAndOrder = "10";

    try {
      Intent intent = new Intent();
      Bundle args = new Bundle();
      args.putString("merOrderId", merOrderId);              // 客户订单号
      args.putInt("payType", payType);                    // 指定收款方式（默认0，微信支付 10， 银商支付30）
      args.putLong("amount", amount);                          // 收款金额 单位分
      args.putString("title", title);                   // 订单标题
      args.putString("operator", operator);                // 操作员
      args.putString("packageName", context.getPackageName());    // applicationId 应用标示
      args.putString("tableId", "11");                 // 台位
      args.putString("funCode", "10");                 // 支付功能码
      args.putString("bizAndOrder", bizAndOrder);
      args.putString("customerNo", customerNo);
      //args.putString("phoneNo", phoneNo);
      intent.putExtra("data", args);
      intent.setAction(ACTION_PAY);
      return intent;
    } catch (Exception e) {
      LogUtil.e("TAG", e.getMessage());
      return null;
    }

  }

  public Intent print(Context context) {
    String msg = getPrintFormatMsg("测试打印");
    try {
      Intent intent = new Intent();
      Bundle args = new Bundle();
      args.putString("msg", msg);   // 打印内容规范 请参考《全民付收银台线下插件商户销售单据打印规范》
      args.putString("packageName", context.getPackageName());  // 应用包名
      intent.putExtra("data", args);
      intent.setAction(ACTION_PRINT);  // 操作类型
      return intent;
    } catch (Exception e) {
      Log.e("TAG", e.getMessage());
      return new Intent();
    }
  }


  private String getPrintFormatMsg(String orderId) {
    String text =
      "!hz l\n" +
        "!asc l\n" +
        "!gray 6\n" +
        "!yspace 4\n" +
        "*text c 菜单\n" +
        "*line\n" +
        "!hz s\n" +
        "!asc s\n" +
        "!gray 2\n" +
        "*text c 消费名称：" + "xxxxx" + "\n" +
        "*text c" + " 订单号：" + orderId + "\n" +
        "*text c" + " 支付方式：" + "1111" + "\n" +
        "*text c" + " 支付时间：" + "11111" + "\n" +
        "*text c" + " 交易类型：消费" + "\n" +
        "*text c" + " 交易金额：" + "1111.11" + "元" + "\n" +
        "*text c" + " 本人确认以上交易" + "\n" +
        "*line\n" +
        "*text c" + " 持卡人签名\n" +
        "*text c" + "\n" +
        "*line\n";
    return text;
  }



  public static final class Builder {
    private String merOrderId;
    private String title;
    private String operator;
    private String customerNo;
    private String phoneNo;
    private long amount;

    public Builder() {
    }

    public Builder merOrderId(String val) {
      merOrderId = val;
      return this;
    }

    public Builder title(String val) {
      title = val;
      return this;
    }

    public Builder operator(String val) {
      operator = val;
      return this;
    }

    public Builder customerNo(String val) {
      customerNo = val;
      return this;
    }

    public Builder phoneNo(String val) {
      phoneNo = val;
      return this;
    }

    public Builder amount(long val) {
      amount = val;
      return this;
    }

    public RongPay build() {
      return new RongPay(this);
    }
  }
}

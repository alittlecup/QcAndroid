package cn.qingchengfit.pos.card;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.qingchengfit.pos.RongPay;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.saasbase.cards.network.response.PayBusinessResponse;
import cn.qingchengfit.saasbase.cards.views.CardBuyFragment;
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
 * Created by Paper on 2017/10/12.
 */

public class PosCardBuyFragment extends CardBuyFragment {

  public static PosCardBuyFragment newInstance(CardTpl cardTpl) {
    Bundle args = new Bundle();
    args.putParcelable("tpl", cardTpl);
    PosCardBuyFragment fragment = new PosCardBuyFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onBusinessOrder(PayBusinessResponse payBusinessResponse) {
    //// TODO: 2017/10/22 测试支付一分钱
    new RongPay.Builder()
      .amount(1).build().pay(getContext());
  }

  private static final String ACTION_PAY = "com.rongcapital.pay";
  private static final String ACTION_PRINT = "com.rongcapital.print";
  public void pay() {
    int payType = 0;

    String bizAndOrder = "10";
    //if (sp.getSelectedItemPosition() == 0) {
    //  bizAndOrder ="10";
    //} else if (sp.getSelectedItemPosition() == 1) {
    //  bizAndOrder = "20";
    //}


    //LogUtil.i("TAG", string1 + "\t" + string2 + "\t" + string3 + "\t" + string4 + "\t");
    try {
      Intent intent = new Intent();
      Bundle args = new Bundle();
      args.putString("merOrderId", System.currentTimeMillis()+"");              // 客户订单号
      args.putInt("payType", payType);                    // 指定收款方式（默认0，微信支付 10， 银商支付30）
      args.putLong("amount", 1);                          // 收款金额 单位分
      args.putString("title", "收钱标题");                   // 订单标题
      args.putString("operator", "操作员");                // 操作员
      args.putString("packageName", getContext().getPackageName());    // applicationId 应用标示
      args.putString("tableId", "11台");                 // 台位
      args.putString("funCode", "10");                 // 支付功能码
      args.putString("bizAndOrder", bizAndOrder);
      args.putString("customerNo", "7178000623LD7V840");
      args.putString("phoneNo", "15123358198");
      intent.putExtra("data", args);
      intent.setAction(ACTION_PAY);
      startActivityForResult(intent, 100);
    } catch (Exception e) {
      LogUtil.e("TAG", e.getMessage());
    }
  }

  public void print() {
    String msg = getPrintFormatMsg("测试打印");
    try {
      Intent intent = new Intent();
      Bundle args = new Bundle();
      args.putString("msg", msg);   // 打印内容规范 请参考《全民付收银台线下插件商户销售单据打印规范》
      args.putString("packageName", getContext().getPackageName());  // 应用包名
      intent.putExtra("data", args);
      intent.setAction(ACTION_PRINT);  // 操作类型
      startActivity(intent);
    } catch (Exception e) {
      Log.e("TAG", e.getMessage());
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
}

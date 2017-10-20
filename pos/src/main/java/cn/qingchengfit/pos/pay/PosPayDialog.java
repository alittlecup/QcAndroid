package cn.qingchengfit.pos.pay;

import android.content.Intent;
import android.os.Bundle;
import cn.qingchengfit.items.PayMethdItem;
import cn.qingchengfit.pos.R;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.views.fragments.PayDialog;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;

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
 * Created by Paper on 2017/10/9.
 *
 */
@Deprecated
public class PosPayDialog extends PayDialog {

  private static final String ACTION_PAY = "com.rongcapital.pay";
  private static final String ACTION_QUERY = "com.rongcapital.query";
  private static final String ACTION_WECHAT_REFUND = "com.rongcapital.refund";
  private static final String ACTION_PRINT = "com.rongcapital.print";
  private static final int WECHAT_VALUE = 10;
  private static final int UMS_VALUE = 30;

  String[] payStr = new String[]{"微信支付","支付宝支付","刷卡支付"};

  int[] payIcon = new int[]{
      R.drawable.vd_pay_wechat,
      R.drawable.vd_pay_alipay,
      R.drawable.vd_pay_card
  };





  String businessID = "001";





  @Override protected List<? extends AbstractFlexibleItem> getItems() {
    List<PayMethdItem> ret = new ArrayList<>();
    for (int i = 0; i < payIcon.length; i++) {
      ret.add(new PayMethdItem(payIcon[i],payStr[i]));
    }
    return ret;
  }

  @Override public void onBtnPayClicked() {
    pay();
  }

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
        args.putString("merOrderId", businessID);              // 客户订单号
        args.putInt("payType", payType);                    // 指定收款方式（默认0，微信支付 10， 银商支付30）
        args.putLong("amount", 1);                          // 收款金额 单位分
        args.putString("title", "收钱标题");                   // 订单标题
        args.putString("operator", "操作员");                // 操作员
        args.putString("packageName", getContext().getPackageName());    // applicationId 应用标示
        args.putString("tableId", "11台");                 // 台位
        args.putString("funCode", "10");                 // 支付功能码
        args.putString("bizAndOrder", bizAndOrder);
        intent.putExtra("data", args);
        intent.setAction(ACTION_PAY);
        startActivityForResult(intent, 100);
      } catch (Exception e) {
        LogUtil.e("TAG", e.getMessage());
      }
  }
}

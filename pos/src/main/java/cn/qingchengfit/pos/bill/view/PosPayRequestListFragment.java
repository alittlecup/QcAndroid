package cn.qingchengfit.pos.bill.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.pos.RongPay;
import cn.qingchengfit.saasbase.bill.beans.PayRequest;
import cn.qingchengfit.saasbase.bill.view.PayRequestListFragment;
import cn.qingchengfit.utils.LogUtil;
import javax.inject.Inject;

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
 * Created by Paper on 2017/10/23.
 */

public class PosPayRequestListFragment extends PayRequestListFragment {
  @Inject GymWrapper gymWrapper;
  @Inject LoginStatus loginStatus;
  @Override public void onPay(PayRequest payRequest) {
    RongPay rp = new RongPay.Builder()
      .amount(payRequest.price)
      .customerNo(gymWrapper.getCustumNo())
      .merOrderId(payRequest.order_no)
      .operator(loginStatus.staff_name())
      //.phoneNo(gymWrapper.get)
      .title(payRequest.title)
      .build();
    startActivityForResult(rp.pay(getContext()),100);
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK){
      if (requestCode == 100){

        if (data.getBundleExtra("data") == null) {
        } else {
          Bundle args = data.getBundleExtra("data");
          Long amount = args.getLong("amount", -1);
          String merorderId = args.getString("merOrderId");
          String payStatus = args.getString("payStatus");
          String title = args.getString("title");
          String operator = args.getString("operator");
          String packageName = args.getString("packageName");
          int payType = args.getInt("payType", 0);
          String tradeFlowId = args.getString("tradeFlowId");                // 交易流水号
          String dealTime = args.getString("dealTime");               // 交易时间
          LogUtil.d("PosPay","amount:" + amount + "|merorderId:" + merorderId + "|title:" + title + "|operator:" +
            operator + "|packageName:" + packageName + "|payType:" + payType
            + "|tradeFlowId:" + tradeFlowId + "|dealTime:" + dealTime + "|payStatus:" + payStatus);
          onPayDone(merorderId);
        }

      }
    }else {
      onShowError("支付取消");
    }
  }
}

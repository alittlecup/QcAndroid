package cn.qingchengfit.saasbase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import cn.qingchengfit.saasbase.bill.view.BillDetailParams;
import cn.qingchengfit.saasbase.routers.SaasbaseRouterCenter;
import cn.qingchengfit.saascommon.SaasCommonActivity;
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
 * Created by Paper on 2017/8/15.
 */

public class SaasContainerActivity extends SaasCommonActivity {
  @Inject SaasbaseRouterCenter routerCenter;

  @Override protected Fragment getRouterFragment(Intent intent) {
    return routerCenter.getFragment(intent.getData(), intent.getExtras());
  }



  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == 100) {

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
          LogUtil.d("PosPay", "amount:"
              + amount
              + "|merorderId:"
              + merorderId
              + "|title:"
              + title
              + "|operator:"
              + operator
              + "|packageName:"
              + packageName
              + "|payType:"
              + payType
              + "|tradeFlowId:"
              + tradeFlowId
              + "|dealTime:"
              + dealTime
              + "|payStatus:"
              + payStatus);
          onPayDone(merorderId);
        }
      }
    } else {

    }
  }

  protected void onPayDone(String orderNo) {
    routeTo("bill", "/pay/done/", new BillDetailParams().orderNo(orderNo).build());
  }



}

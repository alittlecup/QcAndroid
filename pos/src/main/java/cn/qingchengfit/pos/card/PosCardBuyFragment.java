package cn.qingchengfit.pos.card;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.pos.R;
import cn.qingchengfit.pos.RongPay;
import cn.qingchengfit.saasbase.bill.view.BillDetailParams;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.saasbase.cards.network.response.PayBusinessResponse;
import cn.qingchengfit.saasbase.cards.views.CardBuyFragment;
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
 * Created by Paper on 2017/10/12.
 */

public class PosCardBuyFragment extends CardBuyFragment {
  @Inject GymWrapper gymWrapper;
  @Inject LoginStatus loginStatus;
  public static PosCardBuyFragment newInstance(CardTpl cardTpl) {
    Bundle args = new Bundle();
    args.putParcelable("tpl", cardTpl);
    PosCardBuyFragment fragment = new PosCardBuyFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public void onBusinessOrder(PayBusinessResponse payBusinessResponse) {
    if (payBusinessResponse.order_amount > 0) {
      Intent toBuy = new RongPay.Builder().amount(payBusinessResponse.order_amount)
        .title(payBusinessResponse.order_title)
        .merOrderId(payBusinessResponse.order_no)
        .customerNo(gymWrapper.getCustumNo())
        .operator(getResources().getString(R.string.pay_to_operator, loginStatus.staff_name(),
            loginStatus.getLoginUser().id))
        .build()
        .pay(getActivity());
      if (getActivity() != null) {
        getActivity().startActivityForResult(toBuy, 100);
      }
    }else {
      routeTo("bill", "/pay/done/", new BillDetailParams().orderNo(payBusinessResponse.order_no).build());
    }
  }

  //@Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
  //  super.onActivityResult(requestCode, resultCode, data);
  //  if (resultCode == Activity.RESULT_OK){
  //    if (requestCode == 100){
  //
  //      if (data.getBundleExtra("data") == null) {
  //      } else {
  //        Bundle args = data.getBundleExtra("data");
  //        Long amount = args.getLong("amount", -1);
  //        String merorderId = args.getString("merOrderId");
  //        String payStatus = args.getString("payStatus");
  //        String title = args.getString("title");
  //        String operator = args.getString("operator");
  //        String packageName = args.getString("packageName");
  //        int payType = args.getInt("payType", 0);
  //        String tradeFlowId = args.getString("tradeFlowId");                // 交易流水号
  //        String dealTime = args.getString("dealTime");               // 交易时间
  //        LogUtil.d("PosPay","amount:" + amount + "|merorderId:" + merorderId + "|title:" + title + "|operator:" +
  //          operator + "|packageName:" + packageName + "|payType:" + payType
  //          + "|tradeFlowId:" + tradeFlowId + "|dealTime:" + dealTime + "|payStatus:" + payStatus);
  //        onPayDone(merorderId);
  //      }
  //
  //    }
  //  }else {
  //    onShowError("支付取消");
  //  }
  //}
  //
  //protected void onPayDone(String orderNo){
  //  routeTo("bill","/pay/done/",new BillDetailParams().orderNo(orderNo).build());
  //}


}
